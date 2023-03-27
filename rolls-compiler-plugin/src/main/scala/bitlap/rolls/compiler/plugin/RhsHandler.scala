package bitlap.rolls.compiler.plugin

import dotty.tools.dotc.ast.{ tpd, Trees }
import dotty.tools.dotc.ast.tpd.*
import dotty.tools.dotc.core.Constants.Constant
import dotty.tools.dotc.core.Contexts.Context
import dotty.tools.dotc.core.Decorators.*
import dotty.tools.dotc.core.StdNames.*
import dotty.tools.dotc.core.Symbols.*
import dotty.tools.dotc.plugins.{ PluginPhase, StandardPlugin }
import dotty.tools.dotc.quoted.reflect.FromSymbol
import dotty.tools.dotc.report
import dotty.tools.dotc.semanticdb.AnnotatedType
import dotty.tools.dotc.transform.{ PickleQuotes, Staging }
import jdk.internal.net.http.RequestPublishers.ByteArrayPublisher

import java.io.{ BufferedOutputStream, ByteArrayOutputStream, ObjectOutputStream }
import java.net.http.*
import java.net.*
import java.net.http.HttpRequest.BodyPublishers
import java.sql.{ Connection, DriverManager }
import java.time.Duration
import scala.jdk.CollectionConverters.*
import scala.quoted.Quotes
import scala.util.Using

/** @author
 *    梦境迷离
 *  @version 1.0,2023/3/21
 */
trait RhsHandler:

  def handle(tree: ValDef)(using ctx: Context): ValDef = {
    lazy val custAnnotCls = requiredClass("bitlap.rolls.annotations.CustomRhsMapping")
    lazy val annotCls     = requiredClass("bitlap.rolls.annotations.RhsMapping")

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
      case Literal(Constant(original: String)) if existsAnnot =>
        if (nameArgs.nonEmpty && (!_nameColumns.contains(".") || _nameColumns.split('.').length > 2)) {
          report.error(s"Rhs mapping nameColumns format was invalid:${_nameColumns}", tree.sourcePos)
        }

        if (!original.contains(".") || original.split('.').length > 2) {
          report.error(s"Rhs mapping rhs format was invalid:${_nameColumns}", tree.sourcePos)
        }

        val httpUrl =
          s"${Utils.reqUrl}?value=${original}&idColumn=${_idColumn}&nameColumns=${_nameColumns}&tableName=${_tableName}"
        val response = Utils.sendRhsMapping(httpUrl)
        report.debugwarn(s"Rhs mapping transform with $httpUrl, response:$response", tree.sourcePos)
        if response == null || response.isEmpty then {
          ValDef(tree.symbol.asTerm, tree.rhs)
        } else {
          val valdef = ValDef(tree.symbol.asTerm, Literal(Constant(response)))
          report.debugwarn(s"Rhs mapping generate new ValDef:$valdef", tree.sourcePos)
          valdef
        }
      case t => tree
  }
