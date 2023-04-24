package bitlap.rolls.compiler.plugin

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
import dotty.tools.dotc.report
import dotty.tools.dotc.transform.*

import scala.annotation.threadUnsafe

/** @author
 *    梦境迷离
 *  @version 1.0,2023/3/28
 */
final class ValidateIdentPrefixPhase(setting: RollsSetting) extends PluginPhase with TypeDefPluginPhaseFilter:

  override val phaseName: String       = RollsPhase.ValidateIdentPrefix.name
  override val description: String     = RollsPhase.ValidateIdentPrefix.description
  override val runsAfter: Set[String]  = Set(Staging.name)
  override val runsBefore: Set[String] = Set(PickleQuotes.name)

  override val annotationFullNames: List[String] = setting.config.validateIdentPrefix

  @threadUnsafe private lazy val ValidateAnnotationsClasses: List[Context ?=> ClassSymbol] =
    setting.config.validateIdentPrefix.map(v => requiredClass(v))

  private lazy val startsWith = setting.config.validateShouldStartsWith

  override def transformTypeDef(tree: TypeDef)(using Context): Tree =
    if (filterClassDef(tree)) mapTree(tree) else tree
  end transformTypeDef

  override def mapTree(tree: TypeDef): Context ?=> TypeDef =
    // if annotation on type or primaryConstructor, check self name
    if (existsAnnotations(tree) && !tree.name.show.startsWith(startsWith.capitalize)) {
      report.error(
        s"""
           |case class name does not startsWith ${startsWith.capitalize} in ${tree.name}
           |Expected: ${startsWith.capitalize}${tree.name.show}
           |Actual: ${tree.name.show}
           |""".stripMargin,
        tree.srcPos
      )
    }

    val typeTypeTree                = tree.toClassTree
    implicit val clazz: ClassSymbol = typeTypeTree.classSymbol
    val paramSyms = typeTypeTree.primaryConstructor.paramSymss.flatten.filter(!_.isType).map(_.toFieldTree)
    val existsAnnots = ValidateAnnotationsClasses
      .map(_.name.asSimpleName)
      .exists(declare => paramSyms.exists(_.containsAnnotation(declare)))
    if !existsAnnots then tree
    else
      // if paramss contains annotation, check self name
      if (!tree.name.asSimpleName.startsWith(startsWith.capitalize)) {
        report.error(
          s"""
             |case class name does not startsWith ${startsWith.capitalize} in ${tree.name}
             |Expected: ${startsWith.capitalize}${tree.name.show}
             |Actual: ${tree.name.show}
             |""".stripMargin,
          tree.srcPos
        )
      }

      // if paramss contains annotation, check for case classes and functions
      val needCheckParams = paramSyms
        .filter(p =>
          ValidateAnnotationsClasses.map(_.name.asSimpleName).exists(declare => p.containsAnnotation(declare))
        )
        .filter { f =>
          val caseClass =
            f.tpe <:< defn.ProductClass.typeRef && f.tpe.member(nme.copy).filterWithFlags(Synthetic, EmptyFlags).exists
          val function = defn.isFunctionType(f.tpe)
          caseClass || function
        }

      val assertFalseName: List[FieldTree] =
        needCheckParams.filter(n => !n.name.startsWith(startsWith))

      // report error for fields
      if (assertFalseName.nonEmpty) {
        report.error(
          s"""
             |parameter names of the primary constructor don't startsWith $startsWith in ${tree.name}
             |Expected: ${assertFalseName
              .map(_.name.show.capitalize)
              .map(f => s"$startsWith$f")
              .mkString(",")}
             |Actual: ${assertFalseName.map(_.name).mkString(",")}
             |""".stripMargin,
          tree.srcPos
        )
      }

      tree
  end mapTree

end ValidateIdentPrefixPhase
