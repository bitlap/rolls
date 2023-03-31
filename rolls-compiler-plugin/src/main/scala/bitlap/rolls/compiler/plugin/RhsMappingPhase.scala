package bitlap.rolls.compiler.plugin

import bitlap.rolls.compiler.plugin.Utils
import dotty.tools.dotc.ast.*
import dotty.tools.dotc.ast.tpd.*
import dotty.tools.dotc.core.Constants.Constant
import dotty.tools.dotc.core.Contexts.Context
import dotty.tools.dotc.core.Symbols.*
import dotty.tools.dotc.plugins.PluginPhase
import dotty.tools.dotc.report
import dotty.tools.dotc.transform.{ PickleQuotes, Staging }

/** @author
 *    梦境迷离
 *  @version 1.0,2023/3/27
 */
final class RhsMappingPhase(setting: Setting) extends PluginPhase with PluginPhaseFilter[ValDef] {

  override val phaseName               = "RhsMappingPhase"
  override val runsAfter: Set[String]  = Set(Staging.name)
  override val runsBefore: Set[String] = Set(PickleQuotes.name)

  override def transformValDef(tree: ValDef)(using Context): Tree =
    if (existsAnnot(tree)) handle(tree) else tree
  end transformValDef

  override val annotationFullNames: List[String] = List(setting.config.rhsMapping, setting.config.customRhsMapping)

  override def existsAnnot(tree: ValDef): Context ?=> Boolean = {
    val annotCls = annotationFullNames.map(requiredClass(_))
    tree.mods.annotations.collectFirst {
      case Apply(Select(New(Ident(an)), _), Nil) if an.asSimpleName == annotCls(0).name.asSimpleName =>
        true
      case Apply(Select(New(Ident(an)), _), _) if an.asSimpleName == annotCls(1).name.asSimpleName =>
        true
    }.getOrElse(false)
  }

  override def handle(tree: ValDef): Context ?=> ValDef = {
    val annotCls = getDeclarationAnnots
    val nameArgs = tree.mods.annotations.collectFirst {
      case Apply(Select(New(Ident(an)), _), Nil) if an.asSimpleName == annotCls(0).name.asSimpleName =>
        Map()
      case Apply(Select(New(Ident(an)), _), listNameArgs) if an.asSimpleName == annotCls(1).name.asSimpleName =>
        listNameArgs.collect { case NamedArg(n, Literal(Constant(v: String))) =>
          n.asSimpleName.toString -> v
        }.toMap
    }.getOrElse(Map.empty)

    val _idColumn    = nameArgs.getOrElse("idColumn", "")
    val _nameColumns = nameArgs.getOrElse("nameColumns", "")
    val _tableName   = nameArgs.getOrElse("tableName", "")

    tree.rhs match
      case Literal(Constant(original: String)) =>
        if (nameArgs.nonEmpty && (!_nameColumns.contains(".") || _nameColumns.split('.').length > 2)) {
          report.error(s"Rhs mapping nameColumns format was invalid:${_nameColumns}", tree.sourcePos)
        }

        if (!original.contains(".") || original.split('.').length > 2) {
          report.error(s"Rhs mapping rhs format was invalid:${_nameColumns}", tree.sourcePos)
        }

        val httpUrl =
          s"${setting.config.rhsMappingUri}?value=$original&idColumn=${_idColumn}&nameColumns=${_nameColumns}&tableName=${_tableName}"
        val response = Utils.sendRhsMapping(httpUrl)
        debug(s"Rhs mapping transform with $httpUrl, response:$response", tree)
        if response == null || response.isEmpty then {
          ValDef(tree.symbol.asTerm, tree.rhs)
        } else {
          val valdef = ValDef(tree.symbol.asTerm, const(response))
          debug("Rhs mapping generate new ValDef", tree)
          valdef
        }
      case t => tree
  }
}
