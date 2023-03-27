package bitlap.rolls.compiler.plugin.handler

import bitlap.rolls.compiler.plugin.{Utils, ValDefHandler}
import dotty.tools.dotc.ast.tpd.ValDef
import dotty.tools.dotc.core.Contexts.Context
import dotty.tools.dotc.ast.{Trees, tpd}
import dotty.tools.dotc.ast.tpd.*
import dotty.tools.dotc.core.Constants.Constant
import dotty.tools.dotc.core.Symbols.*
import dotty.tools.dotc.report

/** @author
 *    梦境迷离
 *  @version 1.0,2023/3/27
 */
final class RhsMappingValDefHandler extends ValDefHandler {

  override val annotationFullNames: List[String] = List("bitlap.rolls.annotations.RhsMapping","bitlap.rolls.annotations.CustomRhsMapping")

  override def existsAnnot(tree: ValDef)(using ctx: Context): Boolean = {
    val annotCls = annotationFullNames.map(requiredClass(_))
    tree.mods.annotations.collectFirst {
      case Apply(Select(New(Ident(an)), _), Nil) if an.asSimpleName == annotCls(0).name.asSimpleName =>
        true
      case Apply(Select(New(Ident(an)), _), _) if an.asSimpleName == annotCls(1).name.asSimpleName =>
        true
      case _ => false
    }.getOrElse(false)
  }

  override def handle(tree: ValDef)(using ctx: Context): ValDef = {
    val annotCls = annotationFullNames.map(requiredClass(_))
    val nameArgs = tree.mods.annotations.collectFirst {
      case Apply(Select(New(Ident(an)), _), Nil) if an.asSimpleName == annotCls(0).name.asSimpleName =>
        Map()
      case Apply(Select(New(Ident(an)), _), listNameArgs) if an.asSimpleName == annotCls(1).name.asSimpleName =>
        listNameArgs.collect {
          case NamedArg(n, Literal(Constant(v: String))) => n.asSimpleName.toString -> v
        }.toMap
      case _ =>  Map.empty
    }.getOrElse(Map.empty)

    val _idColumn = nameArgs.getOrElse("idColumn", "")
    val _nameColumns = nameArgs.getOrElse("nameColumns", "")
    val _tableName = nameArgs.getOrElse("tableName", "")

    tree.rhs match
      case Literal(Constant(original: String)) =>
        if (nameArgs.nonEmpty && (!_nameColumns.contains(".") || _nameColumns.split('.').length > 2)) {
          report.error(s"Rhs mapping nameColumns format was invalid:${_nameColumns}", tree.sourcePos)
        }

        if (!original.contains(".") || original.split('.').length > 2) {
          report.error(s"Rhs mapping rhs format was invalid:${_nameColumns}", tree.sourcePos)
        }

        val httpUrl =
          s"${Utils.reqUrl}?value=$original&idColumn=${_idColumn}&nameColumns=${_nameColumns}&tableName=${_tableName}"
        val response = Utils.sendRhsMapping(httpUrl)
        report.debugwarn(s"Rhs mapping transform with $httpUrl, response:$response", tree.sourcePos)
        if response == null || response.isEmpty then {
          ValDef(tree.symbol.asTerm, tree.rhs)
        } else {
          val valdef = ValDef(tree.symbol.asTerm, Literal(Constant(response)))
          report.debugwarn(s"Rhs mapping generate new ValDef:$valdef", tree.sourcePos)
          valdef
        }
      case t => tree
  }
}