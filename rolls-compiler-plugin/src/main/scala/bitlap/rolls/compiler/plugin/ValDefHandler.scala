package bitlap.rolls.compiler.plugin

import bitlap.rolls.compiler.plugin.handler.RhsMappingValDefHandler
import dotty.tools.dotc.core.Contexts.Context
import dotty.tools.dotc.ast.tpd.*

/** @author
 *    梦境迷离
 *  @version 1.0,2023/3/27
 */
trait ValDefHandler :

  val annotationFullNames: List[String]

  def existsAnnot(tree: ValDef)(using ctx: Context): Boolean

  def handle(tree: ValDef)(using ctx: Context): ValDef

end ValDefHandler


object ValDefHandler:
  val handlers: List[ValDefHandler] = List(new RhsMappingValDefHandler)