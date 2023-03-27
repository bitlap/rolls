package bitlap.rolls.compiler.plugin

import dotty.tools.dotc.ast.tpd
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
import scala.util.Using

/** @author
 *    梦境迷离
 *  @version 1.0,2023/3/20
 */
class RollsCompilerPlugin extends StandardPlugin:
  val name: String                 = "RollsCompilerPlugin"
  override val description: String = "RollsCompilerPlugin"

  def init(options: List[String]): List[PluginPhase] =
    new RollsCompilerPluginPhase :: Nil
end RollsCompilerPlugin

class RollsCompilerPluginPhase extends PluginPhase with ClassSchemaHandler with RhsHandler:
  import tpd.*

  val phaseName = "RollsCompilerPlugin"

  override val runsAfter  = Set(Staging.name)
  override val runsBefore = Set(PickleQuotes.name)

  override def transformTypeDef(tree: tpd.TypeDef)(using ctx: Context): tpd.Tree =
    super[ClassSchemaHandler].handle(tree)
  end transformTypeDef

  override def transformValDef(tree: tpd.ValDef)(using Context): tpd.Tree =
    super[RhsHandler].handle(tree)
  end transformValDef

end RollsCompilerPluginPhase
