package bitlap.rolls.compiler.plugin

import dotty.tools.dotc.ast.tpd
import dotty.tools.dotc.core.Contexts.{ ctx, Context }
import dotty.tools.dotc.transform.{ PickleQuotes, Staging }
import dotty.tools.dotc.ast.tpd.*
import dotty.tools.dotc.core.StdNames.nme
import dotty.tools.dotc.core.*
import dotty.tools.dotc.core.Types.*
import dotty.tools.dotc.plugins.PluginPhase
import dotty.tools.dotc.core.Flags.*
import dotty.tools.dotc.core.Names.*
import dotty.tools.dotc.core.Symbols.*

/** @author
 *    梦境迷离
 *  @version 1.0,2023/3/31
 */
class CaseClassWithMethodPhase(setting: Setting) extends PluginPhase with TypeDefPluginPhaseFilter:

  override val phaseName               = "CaseClassWithMethodPhase"
  override val runsAfter: Set[String]  = Set(Staging.name)
  override val runsBefore: Set[String] = Set(PickleQuotes.name)

  override val annotationFullNames: List[String] = List.empty

  override def existsAnnot(tree: TypeDef): ContextFunction1[Context, Boolean] = true

  override def transformTypeDef(tree: TypeDef)(using Context): Tree =
    if (tree.isClassDef && existsAnnot(tree) && isProduct(tree.symbol.asClass)) handle(tree) else tree
  end transformTypeDef

  override def handle(tree: TypeDef): ContextFunction1[Context, TypeDef] =
    val template = tree.rhs.asInstanceOf[Template]
    val cl = ClassDefWithParents(
      tree.symbol.asClass,
      template.constr,
      template.parents,
      template.body ::: generateDefDef(tree)
    )
    debug(s"Add with method", cl)
    cl

  private def generateDefDef(fields: List[Field], field: Field, tree: TypeDef)(using
    ctx: Context
  ): tpd.Tree => tpd.GenericApply = {
    implicit val clazz: ClassSymbol = tree.symbol.asClass
    val toIdx                       = (name: Name) => fields.map(_.name).indexOf(name)
    val defaultFields               = fields.map(f => f.thisDot)
    (arg: Tree) =>
      This(clazz)
        .select(nme.copy, f => !f.info.isParameterless)
        .appliedToArgs(defaultFields.updated(toIdx(field.name), arg))
  }

  private def generateDefDef(tree: TypeDef)(using ctx: Context): List[DefDef] = {
    implicit val clazz: ClassSymbol = tree.symbol.asClass
    val fields                      = tree.tpe.fields.map(_.toField).toList
    fields.map { field =>
      val copyMethod = generateDefDef(fields, field, tree)
      val meth: Symbol = newSymbol(
        clazz,
        termName(s"with${field.name.show.capitalize}"),
        Method | Synthetic,
        MethodType(
          List(field.name.asTermName),
          List(field.tpe),
          tree.tpe
        ),
        coord = clazz.coord
      )

      DefDef(meth.asTerm, rhsFn => copyMethod.apply(rhsFn.head.head))
    }
  }
