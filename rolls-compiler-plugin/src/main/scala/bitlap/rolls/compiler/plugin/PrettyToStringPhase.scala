package bitlap.rolls.compiler.plugin

import bitlap.rolls.compiler.plugin.*
import dotty.tools.dotc.ast.{ tpd, untpd, Trees }
import dotty.tools.dotc.ast.tpd.*
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
import dotty.tools.dotc.core.StdNames.nme
import dotty.tools.dotc.core.SymDenotations.SymDenotation
import dotty.tools.dotc.core.Types.MethodType
import dotty.tools.dotc.util.Spans.NoCoord

import scala.annotation.threadUnsafe

/** @author
 *    梦境迷离
 *  @version 1.0,2023/3/28
 */
final class PrettyToStringPhase extends PluginPhase with TypeDefPluginPhaseFilter:

  override val phaseName               = "PrettyToStringPhase"
  override val runsAfter: Set[String]  = Set(Staging.name)
  override val runsBefore: Set[String] = Set(PickleQuotes.name)

  override val annotationFullNames: List[String] = List("bitlap.rolls.annotations.prettyToString")

  override def transformTypeDef(tree: tpd.TypeDef)(using ctx: Context): tpd.Tree =
    if (existsAnnot(tree)) handle(tree) else tree
  end transformTypeDef

  private lazy val methodName: Context ?=> Name = StdNames.nme.toString_.asSimpleName
  private val toStringMethodName                = "toString_"

  private lazy val UtilsClass: Context ?=> Symbol  = requiredModule("bitlap.rolls.annotations.Utils")
  private lazy val Tuple2Class: Context ?=> Symbol = requiredModule("scala.Tuple2")

  private def filterDefDef(tree: tpd.Tree)(using ctx: Context): Boolean =
    tree match
      case dd: DefDef if dd.name == methodName => true
      case _                                   => false

  override def handle(tree: TypeDef)(using ctx: Context): tpd.TypeDef =
    if tree.isClassDef then
      val clazz    = tree.symbol.asClass
      val template = tree.rhs.asInstanceOf[Template]
      if template.body.exists(filterDefDef) then
        val newBody = template.body.map { member =>
          member match
            case d: DefDef if d.name == methodName =>
              mapDefDef(tree, d.symbol.asTerm)
            case o => o
        }
        val ret = tpd.ClassDefWithParents(clazz, template.constr, template.parents, newBody)
        debug("Exists toString", ret)
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

        val ret = tpd.ClassDefWithParents(
          clazz,
          template.constr,
          template.parents,
          template.body ::: List(
            mapDefDef(tree, meth)
          )
        )
        debug("Not exists toString", ret)
        ret
    else tree

  private def mapDefDef(tree: TypeDef, ts: Symbol)(using ctx: Context): tpd.DefDef =
    val clazz = tree.symbol.asClass
    if (clazz.parentSyms.contains(defn.ProductClass)) {
      val body = ref(UtilsClass.requiredMethod(toStringMethodName))
        .withSpan(ctx.owner.span.focus)
        .appliedTo(This(clazz))
      debug("ToString for case class", DefDef(ts.asTerm, body))
      DefDef(ts.asTerm, body)
    } else {
      val elements = tree.tpe.fields
        .filter(f => !f.symbol.isPrivate)
        .map(f =>
          This(clazz)
            .select(f.name, f => f.info.isParameterless)
        )
        .map { f =>
          ref(Tuple2Class)
            .select(nme.apply)
            .appliedToTypes(List(defn.StringType, defn.AnyType))
            .appliedToArgs(List(const(f.name.show), f))
        }
        .toList

      val list = mkList(elements, TypeTree(defn.AnyType))
      val body = ref(UtilsClass.requiredMethod(toStringMethodName))
        .withSpan(ctx.owner.span.focus)
        .appliedTo(list)

      debug("ToString for class", DefDef(ts.asTerm, body))
      DefDef(ts.asTerm, body)
    }
