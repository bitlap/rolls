package bitlap.rolls.compiler.plugin

import bitlap.rolls.compiler.plugin.handler.ClassSchemaTypeDefHandler
import dotty.tools.dotc.ast.tpd.ValDef
import dotty.tools.dotc.core.Contexts.Context
import dotty.tools.dotc.ast.tpd.*

/** @author
 *    梦境迷离
 *  @version 1.0,2023/3/27
 */
trait TypeDefHandler :
  
  val annotationFullNames:List[String]

  def existsAnnot(tree: TypeDef)(using ctx: Context): Boolean
  
  def handle(tree: TypeDef)(using ctx: Context): TypeDef

end TypeDefHandler


object TypeDefHandler:
  val handlers: List[TypeDefHandler] = List(new ClassSchemaTypeDefHandler())