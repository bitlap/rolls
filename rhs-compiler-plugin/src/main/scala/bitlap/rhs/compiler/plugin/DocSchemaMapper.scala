package bitlap.rhs.compiler.plugin

import dotty.tools.dotc.ast.tpd
import dotty.tools.dotc.ast.tpd.*
import dotty.tools.dotc.core.Contexts.Context
import dotty.tools.dotc.report
import dotty.tools.dotc.ast.tpd
import dotty.tools.dotc.core.Constants.Constant
import dotty.tools.dotc.core.Contexts.Context
import dotty.tools.dotc.core.Decorators.*
import dotty.tools.dotc.core.Denotations.*
import dotty.tools.dotc.core.StdNames.*
import dotty.tools.dotc.core.Symbols.*
import dotty.tools.dotc.core.Types.ThisType
import dotty.tools.dotc.plugins.{ PluginPhase, StandardPlugin }
import dotty.tools.dotc.quoted.reflect.FromSymbol
import dotty.tools.dotc.report
import dotty.tools.dotc.semanticdb.AnnotatedType
import dotty.tools.dotc.transform.{ PickleQuotes, Staging }

/** @author
 *    梦境迷离
 *  @version 1.0,2023/3/21
 */
trait DocSchemaMapper:

  private lazy val Unknown = TypeSchema(typeName = "Unknown", fields = List.empty)

  def mapDefDef(tree: tpd.Tree)(using ctx: Context): Option[MethodSchema] =
    tree match
      case dd @ DefDef(name, _, tpt, _) =>
        Some(
          MethodSchema(
            name = name.show,
            params = dd.termParamss.flatten.map(mapType),
            resultType = mapType(tpt)
          )
        )
      case _ => None

  private def mapSeqLiteral(tree: tpd.Tree)(using ctx: Context): TypeSchema =
    tree match
      case r: SeqLiteral =>
        TypeSchema(typeName = r.show, fields = r.elems.map(mapType))
      case _ =>
        Unknown

  private def mapTypeDef(tree: tpd.Tree)(using ctx: Context): TypeSchema =
    tree match
      case tdef: TypeDef
          if tdef.isClassDef && (
            tdef.tpe.typeSymbol == ctx.definitions.ListClass ||
              tdef.tpe.typeSymbol == ctx.definitions.SeqModule
          ) =>
        val infos = tdef.tpe.typeParams.map(_.paramInfo)
        report.warning(s"mapTypeDef infos tree: $tdef")
        val typeTree = infos
          .map(_.typeSymbol)
          .map(s => FromSymbol.definitionFromSym(s))
          .map(tr => mapType(tr))
        TypeSchema(typeName = tdef.name.show, generic = Option(typeTree))
      case tdef: TypeDef if tdef.isClassDef => mapTemplate(tdef)
      case tdef @ TypeDef(name, _) if !tdef.isClassDef =>
        TypeSchema(typeName = name.show)
      case _ =>
        Unknown

  private def mapTemplate(tree: tpd.TypeDef)(using ctx: Context): TypeSchema = {
    val ps = tree.rhs.asInstanceOf[Template].body.collect { case vd: ValDef => mapType(vd) }
    TypeSchema(typeName = tree.name.show, fields = ps)
  }

  private def mapTypeTree(tree: tpd.Tree)(using ctx: Context): TypeSchema =
    tree match
      case tt: TypeTree =>
        val typeTree = FromSymbol.definitionFromSym(tt.tpe.typeSymbol)
        mapTypeDef(typeTree)
      case _ =>
        Unknown

  private def mapValDef(tree: tpd.Tree)(using ctx: Context): TypeSchema =
    report.debugwarn(s"Find ValDef: $tree")
    tree match
      case ValDef(_, tpt, _) =>
        tpt match
          case _: AppliedTypeTree => mapAppliedTypeTree(tpt)
          case _: SeqLiteral      => mapSeqLiteral(tpt)
          case _: TypeDef         => mapTypeDef(tpt)
          case _: Ident           => mapIdent(tpt)
          case _: TypeTree        => mapTypeTree(tpt)
          case _ => Unknown
      case _ => Unknown

  private def mapAppliedTypeTree(tree: tpd.Tree)(using ctx: Context): TypeSchema =
    tree match
      case AppliedTypeTree(tpt, args) =>
        TypeSchema(
          typeName = ctx.printer.nameString(tpt.symbol),
          generic = Option(args.map(a => mapType(a)))
        )
      case _ =>
        Unknown

  private def mapIdent(tree: tpd.Tree)(using ctx: Context): TypeSchema =
    tree match
      case it: Ident
          if it.tpe.typeSymbol.isPrimitiveValueClass ||
            it.tpe <:< ctx.definitions.SingletonType || it.tpe <:< ctx.definitions.StringType =>
        TypeSchema(typeName = it.name.show)
      case it: Ident =>
        val fields = tree.tpe.fields.map { field =>
          val itTree = FromSymbol.definitionFromSym(field.info.typeSymbol)
          mapType(itTree).copy(name = Some(field.name.show))
        }.toList
        TypeSchema(typeName = it.name.show, fields)
      case _ =>
        Unknown

  private def mapType(tree: tpd.Tree)(using ctx: Context): TypeSchema =
    report.debugwarn(s"Find tree: $tree", tree.sourcePos)
    tree match
      case _: SeqLiteral      => mapSeqLiteral(tree)
      case _: AppliedTypeTree => mapAppliedTypeTree(tree)
      case _: TypeDef         => mapTypeDef(tree)
      case v: ValDef          => mapValDef(tree).copy(name = Some(v.name.show))
      case _: Ident           => mapIdent(tree)
      case _ =>
        Unknown
  end mapType
