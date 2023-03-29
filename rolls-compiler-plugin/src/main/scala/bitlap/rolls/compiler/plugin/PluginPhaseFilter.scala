package bitlap.rolls.compiler.plugin

import dotty.tools.dotc.ast.tpd
import dotty.tools.dotc.ast.tpd.*
import dotty.tools.dotc.core.Constants.Constant
import dotty.tools.dotc.core.Contexts.Context
import dotty.tools.dotc.core.Symbols
import dotty.tools.dotc.core.Symbols.requiredClass
import dotty.tools.dotc.quoted.reflect.FromSymbol
import dotty.tools.dotc.report

trait PluginPhaseFilter[T]:

  def annotationFullNames: List[String]

  def debug(prefix: String, tree: Tree): Context ?=> Unit =
    report.debugwarn(s"$prefix - ${implicitly[Context].printer.toText(tree).mkString(Int.MaxValue, true)}")

  def const(any: Any): Context ?=> tpd.Tree = Literal(Constant(any))

  def existsAnnot(tree: T)(using ctx: Context): Boolean

  def handle(tree: T)(using ctx: Context): T

end PluginPhaseFilter

trait TypeDefPluginPhaseFilter extends PluginPhaseFilter[TypeDef]:

  private def getContrAnnotations(tree: TypeDef)(using ctx: Context): List[tpd.Tree] =
    tree.tpe.typeSymbol.primaryConstructor.annotations.map(p => FromSymbol.definitionFromSym(p.symbol))

  override def existsAnnot(tree: TypeDef)(using ctx: Context): Boolean = {
    lazy val annotCls    = annotationFullNames.map(requiredClass(_))
    val contrAnns        = getContrAnnotations(tree)
    val contrAnnotExists = annotCls.forall(p => contrAnns.exists(_.symbol.name == p.name))
    lazy val exists = (tree.mods.annotations ++ contrAnns).collectFirst {
      case Apply(Select(New(Ident(an)), _), args) if annotCls.exists(_.name.asSimpleName == an.asSimpleName) =>
        debug(s"ExistsAnnot ${tree.name.show} - $args", EmptyTree)
        true
      case _ => false
    }.getOrElse(false)

    debug(s"ExistsAnnot ${tree.name.show} - ${exists || contrAnnotExists}", tree)
    exists || contrAnnotExists
  }
