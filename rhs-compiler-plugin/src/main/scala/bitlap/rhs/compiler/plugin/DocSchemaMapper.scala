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
import dotty.tools.dotc.typer.ForceDegree
import dotty.tools.dotc.typer.Inferencing.isFullyDefined

/** @author
 *    梦境迷离
 *  @version 1.0,2023/3/21
 */
trait DocSchemaMapper:

  private lazy val Unknown = TypeSchema(typeName = "Unknown", fields = List.empty)

  def mapDefDef(tree: tpd.Tree)(using ctx: Context): Option[MethodSchema] =
    report.debugwarn(s"DefDef: $tree")
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

  private def mapSeqLiteral(tree: tpd.SeqLiteral)(using ctx: Context): TypeSchema =
    report.debugwarn(s"SeqLiteral: $tree")
    TypeSchema(typeName = tree.show, fields = tree.elems.map(mapType))

  private def mapTypeDef(tree: tpd.TypeDef)(using ctx: Context): TypeSchema =
    tree match
      case tdef: TypeDef
          if tdef.isClassDef && (
            tdef.tpe.typeSymbol == ctx.definitions.ListClass ||
              tdef.tpe.typeSymbol == ctx.definitions.SeqModule
          ) =>
        val infos = tdef.tpe.typeParams.map(_.paramInfo)
        val typeTree = infos
          .map(_.typeSymbol)
          .map(s => FromSymbol.definitionFromSym(s))
          .map(tr => mapType(tr))
        TypeSchema(typeName = tdef.name.show, generic = Option(typeTree))
      case tdef: TypeDef if tdef.isClassDef =>
        mapTemplate(tdef)
      case tdef: TypeDef if !tdef.isClassDef =>
        TypeSchema(typeName = tdef.name.show)
      case null =>
        Unknown

  private def mapTemplate(tree: tpd.TypeDef)(using ctx: Context): TypeSchema = {
    report.debugwarn(s"Template: $tree")
    val ps = tree.rhs.asInstanceOf[Template].body.collect { case vd: ValDef => mapType(vd) }
    TypeSchema(typeName = tree.name.show, fields = ps)
  }

  private def mapTypeTree(tree: tpd.TypeTree)(using ctx: Context): TypeSchema =
    val actualGeneric = tree.tpe.argTypes.map(_.typeSymbol).map(FromSymbol.definitionFromSym).map(mapType)
    val typeTree      = FromSymbol.definitionFromSym(tree.tpe.typeSymbol)
    mapType(typeTree).copy(generic = Option(actualGeneric))

  private def mapValDef(name: String, tree: tpd.ValDef)(using ctx: Context): TypeSchema =
    report.debugwarn(s"ValDef: $tree")
    mapType(tree.tpt).copy(name = Some(name))

  private def mapAppliedTypeTree(tree: tpd.AppliedTypeTree)(using ctx: Context): TypeSchema =
    report.debugwarn(s"AppliedType: $tree")
    TypeSchema(
      typeName = ctx.printer.nameString(tree.tpt.symbol),
      generic = Option(tree.args.map(a => mapType(a)))
    )

  private def mapRefTree(tree: tpd.RefTree)(using ctx: Context): TypeSchema =
    report.debugwarn(s"RefTree: $tree")
    tree match
      case t: Ident => mapIdent(t)
      case _        => Unknown

  private def mapIdent(tree: tpd.Ident)(using ctx: Context): TypeSchema =
    tree match
      case it: Ident
          if it.tpe.typeSymbol.isPrimitiveValueClass ||
            it.tpe <:< ctx.definitions.SingletonType || it.tpe <:< ctx.definitions.StringType =>
        TypeSchema(typeName = it.name.show)
      case it: Ident =>
        val fields = tree.tpe.fields.map { field =>
          val itTree        = FromSymbol.definitionFromSym(field.info.typeSymbol)
          val actualGeneric = field.info.argTypes.map(_.typeSymbol).map(FromSymbol.definitionFromSym).map(mapType)
          mapType(itTree).copy(name = Some(field.name.show)).copy(generic = Option(actualGeneric))
        }.toList
        TypeSchema(typeName = it.name.show, fields)
      case null =>
        Unknown

  private def mapType(tree: tpd.Tree)(using ctx: Context): TypeSchema =
    tree match
      case s: SeqLiteral      => mapSeqLiteral(s)
      case a: AppliedTypeTree => mapAppliedTypeTree(a)
      case t: TypeDef         => mapTypeDef(t)
      case v: ValDef          => mapValDef(v.name.show, v)
      case t: Ident           => mapIdent(t)
      case r: RefTree         => mapRefTree(r)
      case t: TypeTree        => mapTypeTree(t)
      case _                  => Unknown
  end mapType
