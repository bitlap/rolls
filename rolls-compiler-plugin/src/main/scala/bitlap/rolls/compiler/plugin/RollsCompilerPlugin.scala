package bitlap.rolls.compiler.plugin

import dotty.tools.dotc.ast.tpd
import dotty.tools.dotc.core.Contexts.Context
import dotty.tools.dotc.plugins.{ PluginPhase, StandardPlugin }
import dotty.tools.dotc.transform.{ PickleQuotes, Staging }

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

class RollsCompilerPluginPhase extends PluginPhase:

  val phaseName = "RollsCompilerPlugin"

  override val runsAfter  = Set(Staging.name)
  override val runsBefore = Set(PickleQuotes.name)

  override def transformTypeDef(tree: tpd.TypeDef)(using ctx: Context): tpd.Tree =
    TypeDefHandler.handlers.collectFirst {
      case c if c.existsAnnot(tree) => c.handle(tree)
    }.getOrElse(tree)
  end transformTypeDef

  override def transformValDef(tree: tpd.ValDef)(using Context): tpd.Tree =
    ValDefHandler.handlers.collectFirst {
      case c if c.existsAnnot(tree) => c.handle(tree)
    }.getOrElse(tree)
  end transformValDef

end RollsCompilerPluginPhase
