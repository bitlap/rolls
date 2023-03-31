package bitlap.rolls.compiler.plugin

import bitlap.rolls.compiler.plugin.*
import dotty.tools.dotc.ast.tpd.*
import dotty.tools.dotc.core.Contexts.Context
import dotty.tools.dotc.core.Flags.*
import dotty.tools.dotc.core.Names.*
import dotty.tools.dotc.core.Symbols.*
import dotty.tools.dotc.core.*
import dotty.tools.dotc.core.Types.*
import dotty.tools.dotc.plugins.PluginPhase
import dotty.tools.dotc.transform.{ PickleQuotes, Staging }
import dotty.tools.dotc.core.Constants.Constant
import dotty.tools.dotc.core.StdNames.nme
import scala.annotation.threadUnsafe

/** @author
 *    梦境迷离
 *  @version 1.0,2023/3/28
 */
final class PrettyToStringPhase(setting: RollsSetting) extends PluginPhase with TypeDefPluginPhaseFilter:

  override val phaseName               = "PrettyToStringPhase"
  override val runsAfter: Set[String]  = Set(Staging.name)
  override val runsBefore: Set[String] = Set(PickleQuotes.name)

  override val annotationFullNames: List[String] = List(setting.config.prettyToString)

  override def transformTypeDef(tree: TypeDef)(using Context): Tree =
    if (tree.isClassDef && existsAnnot(tree)) handle(tree) else tree
  end transformTypeDef

  @threadUnsafe private lazy val methodName: Context ?=> Name = StdNames.nme.toString_.asSimpleName
  private val toStringMethodName                              = "toString_"

  @threadUnsafe private lazy val RollsRuntimeClass: Context ?=> Symbol = requiredModule(
    "bitlap.rolls.core.RollsRuntime"
  )
  @threadUnsafe private lazy val Tuple2Class: Context ?=> Symbol = requiredModule("scala.Tuple2")

  private def filterDefDef(tree: Tree)(using ctx: Context): Boolean =
    tree match
      case dd: DefDef if dd.name == methodName => true
      case _                                   => false

  override def handle(tree: TypeDef): Context ?=> TypeDef =
    val clazz    = tree.symbol.asClass
    val template = templateBody(tree)
    val annots   = tree.mods.annotations ++ getContrAnnotations(tree)
    val annotCls = getDeclarationAnnots
    val standard = annots.collectFirst {
      case Apply(Select(New(Ident(an)), _), Nil) if an.asSimpleName == annotCls.head.name.asSimpleName =>
        false
      case Apply(Select(New(Ident(an)), _), List(Literal(Constant(standard: Boolean))))
          if an.asSimpleName == annotCls.head.name.asSimpleName =>
        standard
      case Apply(Select(New(Ident(an)), _), List(NamedArg(_, Literal(Constant(standard: Boolean)))))
          if an.asSimpleName == annotCls.head.name.asSimpleName =>
        standard
      case Apply(Select(New(Ident(an)), _), List(Select(Ident(_), _)))
          if an.asSimpleName == annotCls.head.name.asSimpleName =>
        false
    }.getOrElse(false)

    if template.body.exists(filterDefDef) then
      val newBody = template.body.map { member =>
        member match
          case d: DefDef if d.name == methodName =>
            mapDefDef(standard, tree, d.symbol.asTerm)
          case o => o
      }
      val ret = ClassDefWithParents(clazz, template.constr, template.parents, newBody)
      debug(s"Modify ${tree.name.show} toString", EmptyTree)
      ret
    else
      val meth: Symbol = newSymbol(
        clazz,
        methodName,
        Synthetic | Method | Override,
        MethodType(
          Nil,
          defn.StringType
        ),
        coord = clazz.coord
      )

      val ret = ClassDefWithParents(
        clazz,
        template.constr,
        template.parents,
        template.body ::: List(
          mapDefDef(standard, tree, meth)
        )
      )
      debug(s"Add ${tree.name.show} toString", ret)
      ret

  private def mapDefDef(standard: Boolean, tree: TypeDef, ts: Symbol)(using ctx: Context): DefDef =
    implicit val clazz: ClassSymbol = tree.symbol.asClass
    if (isProduct(clazz)) {
      val body = ref(RollsRuntimeClass.requiredMethod(toStringMethodName))
        .withSpan(ctx.owner.span.focus)
        .appliedToArgs(const(standard) :: const(tree.name.show) :: This(clazz) :: Nil)
      debug(s"${tree.name.show} generate toString for case class", DefDef(ts.asTerm, body))
      DefDef(ts.asTerm, body)
    } else {
      val elements = tree.tpe.fields
        .map(_.toField)
        .filter(f => !f.isPrivate)
        .map { f =>
          ref(Tuple2Class)
            .select(nme.apply)
            .appliedToTypes(List(defn.StringType, defn.AnyType))
            .appliedToArgs(List(const(f.name.show), f.thisDot))
        }
        .toList

      val list = mkList(elements, TypeTree(defn.AnyType))
      val body = ref(RollsRuntimeClass.requiredMethod(toStringMethodName))
        .withSpan(ctx.owner.span.focus)
        .appliedToArgs(const(standard) :: const(tree.name.show) :: list :: Nil)

      debug(s"${tree.name.show} generate toString for class", DefDef(ts.asTerm, body))
      DefDef(ts.asTerm, body)
    }
