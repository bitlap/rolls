package bitlap.rolls.compiler.plugin

import dotty.tools.dotc.ast.tpd
import dotty.tools.dotc.ast.tpd.*
import dotty.tools.dotc.core.Constants.Constant
import dotty.tools.dotc.core.Contexts.Context
import dotty.tools.dotc.core.Symbols
import dotty.tools.dotc.core.Symbols.requiredClass
import dotty.tools.dotc.report

trait PluginPhaseFilter[T]:

  val annotationFullNames: List[String]

  def existsAnnot(tree: T)(using ctx: Context): Boolean

  def handle(tree: T)(using ctx: Context): T

end PluginPhaseFilter

trait TypeDefPluginPhaseFilter extends PluginPhaseFilter[TypeDef]:

  def getContrAnnotations(tree: TypeDef)(using ctx: Context): List[tpd.Tree] =
    tree.tpe.typeSymbol.primaryConstructor.annotations.map(p => ref(p.symbol))

  override def existsAnnot(tree: TypeDef)(using ctx: Context): Boolean = {
    lazy val annotCls    = annotationFullNames.map(requiredClass(_))
    val contrAnns        = getContrAnnotations(tree)
    val contrAnnotExists = annotCls.forall(p => contrAnns.exists(_.symbol.name == p.name))
    lazy val exists = tree.mods.annotations.collectFirst {
      case Apply(Select(New(Ident(an)), _), Nil) if annotCls.exists(_.name.asSimpleName == an.asSimpleName) =>
        true
      case _ => false
    }.getOrElse(false)

    report.debugwarn(s"ExistsAnnot classAonns:${tree.mods.annotations}, contrAonns:$contrAnns")
    exists || contrAnnotExists
  }
