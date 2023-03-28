package bitlap.rolls.compiler.plugin

import bitlap.rolls.compiler.plugin.*
import dotty.tools.dotc.ast.{ tpd, untpd, Trees }
import dotty.tools.dotc.ast.tpd.{ ta, MemberDef, * }
import dotty.tools.dotc.core.Contexts.{ ctx, Context }
import dotty.tools.dotc.core.Flags.*
import dotty.tools.dotc.core.Names.*
import dotty.tools.dotc.core.Symbols.*
import dotty.tools.dotc.core.*
import dotty.tools.dotc.plugins.PluginPhase
import dotty.tools.dotc.quoted.reflect.FromSymbol
import dotty.tools.dotc.report
import dotty.tools.dotc.transform.{ LambdaLift, PickleQuotes, Staging }
import dotty.tools.dotc.core.Constants.Constant
import dotty.tools.dotc.core.DenotTransformers.{ DenotTransformer, IdentityDenotTransformer }
import dotty.tools.dotc.core.StdNames.nme
import dotty.tools.dotc.core.SymDenotations.SymDenotation
import dotty.tools.dotc.core.Types.MethodType
import dotty.tools.dotc.util.Spans.NoCoord

import scala.annotation.threadUnsafe

/** @author
 *    梦境迷离
 *  @version 1.0,2023/3/28
 */
final class PrettyToStringPhase extends PluginPhase with TypeDefPluginPhaseFilter with IdentityDenotTransformer:
  thisPhase =>

  override val phaseName               = "PrettyToStringPhase"
  override val runsAfter: Set[String]  = Set(Staging.name)
  override val runsBefore: Set[String] = Set(PickleQuotes.name)

  override def transformTypeDef(tree: tpd.TypeDef)(using ctx: Context): tpd.Tree =
    if (existsAnnot(tree)) handle(tree) else tree
  end transformTypeDef

  override val annotationFullNames: List[String] = List("bitlap.rolls.annotations.prettyToString")

  private lazy val methodName: Context ?=> Name = StdNames.nme.toString_.asSimpleName

  private def filterDefDef(tree: tpd.Tree)(using ctx: Context): Boolean =
    tree match
      case dd: DefDef if dd.name == methodName => true
      case _                                   => false

  override def handle(tree: TypeDef)(using ctx: Context): tpd.TypeDef =
    if tree.isClassDef then
      val template = tree.rhs.asInstanceOf[Template]
      if template.body.exists(filterDefDef) then
        val newToString = template.body.map { member =>
          member match
            case d: DefDef if d.name == methodName => mapDefDef(tree, d.symbol.asTerm)
            case o                                 => o
        }
        tpd.ClassDefWithParents(tree.symbol.asClass, template.constr, template.parents, newToString)
      else
        tpd.ClassDefWithParents(
          tree.symbol.asClass,
          template.constr,
          template.parents,
          template.body ::: List(
            mapDefDef(tree, defn.syntheticCoreMethods.find(_.name == methodName).get)
          )
        )
    else tree

  private val toStringMethodName                  = "toString_"
  private lazy val UtilsClass: Context ?=> Symbol = requiredModule("bitlap.rolls.annotations.Utils")

  private def mapDefDef(tree: TypeDef, ts: TermSymbol)(using ctx: Context): tpd.DefDef = {
    val defdef = ref(UtilsClass.requiredMethod(toStringMethodName))
      .withSpan(ctx.owner.span.focus)
      .appliedTo(This(tree.symbol.asClass))
    DefDef(ts, defdef)
  }
