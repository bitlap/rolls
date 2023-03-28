package bitlap.rolls.compiler.plugin

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

  def getCotrAnnotations(tree: TypeDef)(using ctx: Context) =
    tree.tpe.typeSymbol.primaryConstructor.annotations.map(p => ref(p.symbol))

  def annotParams(tree: TypeDef)(using ctx: Context): Option[Boolean] =
    if (annotationFullNames.isEmpty) None
    else
      val ctsAnnots = getCotrAnnotations(tree)
      annotationFullNames.map(requiredClass(_)).headOption.flatMap { annotCls =>
        (tree.mods.annotations ++ ctsAnnots).collectFirst {
          case Apply(Select(New(Ident(an)), _), List(NamedArg(n, Literal(Constant(v: Boolean)))))
              if an.asSimpleName == annotCls.name.asSimpleName =>
            Option(v)
          case _ => None
        }.flatten
      }

  override def existsAnnot(tree: TypeDef)(using ctx: Context): Boolean = {
    lazy val annotCls = annotationFullNames.map(requiredClass(_))
    val ctsAnnots     = getCotrAnnotations(tree)
    lazy val exists = (tree.mods.annotations ++ ctsAnnots).collectFirst {
      case Apply(Select(New(Ident(an)), _), _) if annotCls.exists(_.name.asSimpleName == an.asSimpleName) =>
        true
      case _ => false
    }.getOrElse(false)

    report.debugwarn(s"ExistsAnnot exists:$exists, ctsAnnots:$ctsAnnots")
    exists
  }
