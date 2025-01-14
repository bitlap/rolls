package bitlap.rolls.compiler.plugin

import scala.annotation.threadUnsafe

import bitlap.rolls.compiler.plugin.*

import dotty.tools.dotc.ast.tpd.*
import dotty.tools.dotc.core.*
import dotty.tools.dotc.core.Constants.Constant
import dotty.tools.dotc.core.Contexts.Context
import dotty.tools.dotc.core.Flags.*
import dotty.tools.dotc.core.Names.*
import dotty.tools.dotc.core.StdNames.nme
import dotty.tools.dotc.core.Symbols.*
import dotty.tools.dotc.core.Types.*
import dotty.tools.dotc.plugins.PluginPhase
import dotty.tools.dotc.transform.*

/** @author
 *    梦境迷离
 *  @version 1.0,2023/3/28
 */
final class PrettyToStringPhase(setting: RollsSetting) extends PluginPhase with TypeDefPluginPhaseFilter:

  override val phaseName: String                 = RollsPluginPhase.PrettyToString.name
  override val description: String               = RollsPluginPhase.PrettyToString.description
  override val runsAfter: Set[String]            = Set(Staging.name)
  override val runsBefore: Set[String]           = Set(PickleQuotes.name)
  override val annotationFullNames: List[String] = setting.config.prettyToString.toList

  @threadUnsafe private lazy val methodName: Context ?=> Name             = StdNames.nme.toString_.asSimpleName
  @threadUnsafe private lazy val StringMaskClass: Context ?=> ClassSymbol = requiredClass(setting.config.stringMask)

  @threadUnsafe private lazy val RollsRuntimeClass: Context ?=> TermSymbol = requiredModule(
    setting.config.rollsRuntimeClass
  )
  @threadUnsafe private lazy val Tuple2Class: Context ?=> TermSymbol = requiredModule("scala.Tuple2")

  override def transformTypeDef(tree: TypeDef)(using Context): Tree =
    if (filterClassDefAnnotations(tree)) mapTree(tree) else tree
  end transformTypeDef

  private def filterDefName(tree: Tree)(using ctx: Context): Boolean =
    tree match
      case dd: DefDef => dd.name == methodName
      case _          => false

  override def mapTree(tree: TypeDef): Context ?=> TypeDef =
    val classTree                   = tree.toClassTree
    implicit val clazz: ClassSymbol = classTree.classSymbol
    val template                    = classTree.template
    val allAnnotations              = classTree.annotations ++ classTree.contrAnnotations
    val pluginSettingAnnotations    = getPluginSettingAnnotations
    val standard = allAnnotations.collectFirst {
      case Apply(Select(New(Ident(an)), _), Nil)
          if an.asSimpleName == pluginSettingAnnotations.head.name.asSimpleName =>
        false
      case Apply(Select(New(Ident(an)), _), List(Literal(Constant(standard: Boolean))))
          if an.asSimpleName == pluginSettingAnnotations.head.name.asSimpleName =>
        standard
      case Apply(Select(New(Ident(an)), _), List(NamedArg(_, Literal(Constant(standard: Boolean)))))
          if an.asSimpleName == pluginSettingAnnotations.head.name.asSimpleName =>
        standard
      case Apply(Select(New(Ident(an)), _), List(Select(Ident(_), _)))
          if an.asSimpleName == pluginSettingAnnotations.head.name.asSimpleName =>
        false
    }.getOrElse(false)

    if template.body.exists(filterDefName) then
      val newBody = template.body.map {
        case d: DefDef if d.name == methodName =>
          mapDefDef(standard, classTree, d.symbol.asTerm)
        case o => o
      }
      val ret = ClassDefWithParents(clazz, template.constr, template.parents, newBody)
      debug(s"Modify ${classTree.name} toString", ret)
      ret
    else
      val newMethodSymbol: Symbol = newSymbol(
        clazz,
        methodName,
        Synthetic | Method | Override,
        MethodType(
          Nil,
          defn.StringType
        ),
        coord = clazz.coord
      )

      ClassDefWithParents(
        clazz,
        template.constr,
        template.parents,
        template.body ::: List(
          mapDefDef(standard, classTree, newMethodSymbol)
        )
      )

  private def mapDefDef(standard: Boolean, classTree: ClassTree, ts: Symbol)(using
      ctx: Context,
      clazz: ClassSymbol
  ): DefDef =
    val paramSyms = classTree.primaryConstructor.paramSymss.flatten.filter(!_.isType).map(_.toFieldTree)
    val elements = paramSyms
      .filter(!_.isPrivate)
      .map { f =>
        ref(Tuple2Class)
          .select(nme.apply)
          .appliedToTypes(List(defn.StringType, defn.AnyType))
          .appliedToArgs(
            List(
              const(f.name.show),
              if f.containsAnnotation(StringMaskClass.name.asSimpleName)
              then Literal(Constant("***"))
              else f.thisDot
            )
          )
      }

    val list = mkList(elements, TypeTree(defn.AnyType, false))
    val body = ref(RollsRuntimeClass.requiredMethod(setting.config.rollsRuntimeToStringMethod))
      .withSpan(ctx.owner.span.focus)
      .appliedToArgs(const(standard) :: const(classTree.name) :: list :: Nil)

    debug(s"${classTree.name} generate toString for class", DefDef(ts.asTerm, body))
    DefDef(ts.asTerm, body)
