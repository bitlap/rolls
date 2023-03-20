package bitlap.rhs.compiler.plugin

import dotty.tools.dotc.ast.Trees.*
import dotty.tools.dotc.ast.tpd.ValDef
import dotty.tools.dotc.ast.{ tpd, Trees }
import dotty.tools.dotc.core.Constants.Constant
import dotty.tools.dotc.core.Contexts.Context
import dotty.tools.dotc.core.Decorators.*
import dotty.tools.dotc.core.StdNames.*
import dotty.tools.dotc.core.Symbols.*
import dotty.tools.dotc.plugins.{ PluginPhase, StandardPlugin }
import dotty.tools.dotc.report
import dotty.tools.dotc.semanticdb.AnnotatedType
import dotty.tools.dotc.transform.{ PickleQuotes, Staging }

import java.net.http.*
import java.net.*
import java.sql.{ Connection, DriverManager }
import java.time.Duration
import scala.jdk.CollectionConverters.*

/** @author
 *    梦境迷离
 *  @version 1.0,2023/3/20
 */
class TransformValDef extends StandardPlugin:
  val name: String                 = "transformValDef"
  override val description: String = "transform ValDef by @RhsMapping or @CustomRhsMapping"

  def init(options: List[String]): List[PluginPhase] =
    new TransformValDefPhase :: Nil
end TransformValDef

class TransformValDefPhase extends PluginPhase:
  import tpd.*

  val phaseName = "transformValDef"

  override val runsAfter  = Set(Staging.name)
  override val runsBefore = Set(PickleQuotes.name)
  private val timeout     = Duration.ofSeconds(5)
  private lazy val client = HttpClient
    .newBuilder()
    .version(HttpClient.Version.HTTP_2)
    .connectTimeout(timeout)
    .followRedirects(HttpClient.Redirect.NEVER)
    .proxy(ProxySelector.getDefault)
    .build()

  private val reqUrl = "http://localhost:18000/rhs-mapping"

  private def send(url: String): String =
    val request = HttpRequest.newBuilder
      .header("Content-Type", "application/json")
      .version(HttpClient.Version.HTTP_2)
      .uri(URI.create(url))
      .GET
      .timeout(timeout)
      .build
    val response = client.send(request, HttpResponse.BodyHandlers.ofString)
    if response.statusCode == 200 then response.body else null

  override def transformValDef(tree: tpd.ValDef)(using ctx: Context): tpd.Tree =
    lazy val custAnnotCls = requiredClass("bitlap.rhs.annotations.CustomRhsMapping")
    lazy val annotCls     = requiredClass("bitlap.rhs.annotations.RhsMapping")

    if (tree.mods.hasAnnotations) {
      report.debugwarn(s"Find annotations: ${tree.mods.annotations}, ${tree.rhs}", tree.sourcePos)
    }

    val (existsAnnot, nameArgs) = tree.mods.annotations.collectFirst {
      case Apply(Select(New(Ident(an)), _), Nil) if an.asSimpleName == annotCls.name.asSimpleName =>
        true -> Map()
      case Apply(Select(New(Ident(an)), _), listNameArgs) if an.asSimpleName == custAnnotCls.name.asSimpleName =>
        true -> listNameArgs.collect { case NamedArg(n, Literal(Constant(v: String))) =>
          n.asSimpleName.toString -> v
        }.toMap
      case _ => false -> Map.empty
    }.getOrElse(false -> Map.empty)

    val _idColumn    = nameArgs.getOrElse("idColumn", "")
    val _nameColumns = nameArgs.getOrElse("nameColumns", "")
    val _tableName   = nameArgs.getOrElse("tableName", "")

    tree.rhs match
      case Trees.Literal(Constant(original: String)) if existsAnnot =>
        if (nameArgs.nonEmpty && (!_nameColumns.contains(".") || _nameColumns.split('.').length > 2)) {
          report.error(s"Rhs mapping nameColumns format was invalid:${_nameColumns}", tree.sourcePos)
        }

        if (!original.contains(".") || original.split('.').length > 2) {
          report.error(s"Rhs mapping rhs format was invalid:${_nameColumns}", tree.sourcePos)
        }

        val httpUrl =
          s"$reqUrl?value=${original}&idColumn=${_idColumn}&nameColumns=${_nameColumns}&tableName=${_tableName}"
        val response = send(httpUrl)
        report.debugwarn(s"Rhs mapping transform with $httpUrl, response:$response", tree.sourcePos)
        if response == null || response.isEmpty then {
          ValDef(tree.symbol.asTerm, tree.rhs)
        } else {
          val valdef = ValDef(tree.symbol.asTerm, Literal(Constant(response)))
          report.debugwarn(s"Rhs mapping generate new ValDef:$valdef", tree.sourcePos)
          valdef
        }
      case t => tree
end TransformValDefPhase
