package bitlap.rolls.compiler.plugin

import dotty.tools.dotc.ast.tpd
import dotty.tools.dotc.ast.tpd.*
import dotty.tools.dotc.core.Constants.Constant
import dotty.tools.dotc.core.Contexts.Context
import dotty.tools.dotc.core.Symbols
import dotty.tools.dotc.core.Symbols.{ defn, requiredClass, ClassSymbol }
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

  def isProduct(clazz: ClassSymbol): Context ?=> Boolean = clazz.parentSyms.contains(defn.ProductClass)

  def getDeclarationAnnots: Context ?=> List[ClassSymbol] = annotationFullNames.map(requiredClass(_))

end PluginPhaseFilter

trait TypeDefPluginPhaseFilter extends PluginPhaseFilter[TypeDef]:

  def getContrAnnotations(tree: TypeDef): Context ?=> List[tpd.Tree] =
    if (tree.isClassDef && isProduct(tree.symbol.asClass))
      val typeContrAnnots = tree.tpe.typeConstructor.typeSymbol.annotations
      val contrAnnots     = tree.tpe.typeSymbol.primaryConstructor.annotations
      debug(
        s"${tree.name.show} - getContrAnnotations - typeContrAnnots:${typeContrAnnots.map(_.tree)} - contrAnnots:$contrAnnots",
        tree
      )
      contrAnnots.map(f => FromSymbol.definitionFromSym(f.symbol)) ++ typeContrAnnots.map(_.tree)
    else tree.tpe.typeSymbol.primaryConstructor.annotations.map(f => FromSymbol.definitionFromSym(f.symbol))

  override def existsAnnot(tree: TypeDef): Context ?=> Boolean = {
    lazy val declarAnnotCls = getDeclarationAnnots
    val contrAnnots         = getContrAnnotations(tree)
    lazy val exists = (contrAnnots ++ tree.mods.annotations).collectFirst {
      case Apply(Select(New(Ident(an)), _), args) if declarAnnotCls.exists(_.name.asSimpleName == an.asSimpleName) =>
        debug(s"${tree.name.show} - annot args:$args", EmptyTree)
        true
    }.getOrElse(false)

    debug(s"${tree.name.show} - $exists - total contrAnnots:$contrAnnots", EmptyTree)

    exists
  }
