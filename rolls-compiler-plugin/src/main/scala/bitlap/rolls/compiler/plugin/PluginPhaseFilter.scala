package bitlap.rolls.compiler.plugin

import dotty.tools.dotc.ast.tpd
import dotty.tools.dotc.ast.tpd.*
import dotty.tools.dotc.core.Constants.Constant
import dotty.tools.dotc.core.Contexts.Context
import dotty.tools.dotc.core.Symbols
import dotty.tools.dotc.core.Annotations.*
import dotty.tools.dotc.core.Symbols.*
import dotty.tools.dotc.core.Types.*
import dotty.tools.dotc.quoted.reflect.FromSymbol
import dotty.tools.dotc.report

/** @author
 *    梦境迷离
 *  @version 1.0,2023/3/31
 */
trait PluginPhaseFilter[T]:

  def annotationFullNames: List[String]

  def debug(prefix: String, tree: Tree): Context ?=> Unit =
    report.debugwarn(s"$prefix - ${implicitly[Context].printer.toText(tree).mkString(Int.MaxValue, true)}")

  def const(any: Any): Context ?=> tpd.Tree = Literal(Constant(any))

  def existsAnnot(tree: T): Context ?=> Boolean

  def handle(tree: T): Context ?=> T

  def getDeclarationAnnots: Context ?=> List[ClassSymbol] = annotationFullNames.map(requiredClass(_))

end PluginPhaseFilter

trait TypeDefPluginPhaseFilter extends PluginPhaseFilter[TypeDef]:

  override def existsAnnot(tree: TypeDef): Context ?=> Boolean = {
    val declareAnnotCls = getDeclarationAnnots
    val exists = (tree.getContrAnnotations ++ tree.getAnnotations).collectFirst {
      case Apply(Select(New(Ident(an)), _), args) if declareAnnotCls.exists(_.name.asSimpleName == an.asSimpleName) =>
        debug(s"${tree.getName} - annot args:$args", EmptyTree)
        true
    }.getOrElse(false)

    debug(s"${tree.getName} - $exists - TypeDef:$tree", EmptyTree)

    exists
  }
