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
import dotty.tools.dotc.core.Types.AppliedType
import dotty.tools.dotc.plugins.{ PluginPhase, StandardPlugin }
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
      case _ => Unknown

  private def mapTypeDef(tree: tpd.Tree)(using ctx: Context): TypeSchema =
    tree match
      case tdef @ TypeDef(name, rhs) if tdef.isClassDef =>
        val ps = rhs.asInstanceOf[Template].body.collect { case vd: ValDef =>
          mapType(vd)
        }
        TypeSchema(typeName = name.show, fields = ps)
      case _ => Unknown

  private def mapValDef(tree: tpd.Tree)(using ctx: Context): TypeSchema =
    report.debugwarn(s"Find mapValDef: $tree")
    tree match
      case ValDef(_, tpt, _) =>
        tpt match
          case _: AppliedTypeTree                      => mapAppliedTypeTree(tpt)
          case _: SeqLiteral                           => mapSeqLiteral(tpt)
          case tdef @ TypeDef(_, _) if tdef.isClassDef => mapTypeDef(tpt)
          case _: Ident                                => mapIdent(tpt)
          case _                                       => Unknown
      case _ => Unknown

  private def mapAppliedTypeTree(tree: tpd.Tree)(using ctx: Context): TypeSchema =
    tree match
      case AppliedTypeTree(tpt, args) =>
        TypeSchema(
          typeName = ctx.printer.nameString(tpt.symbol),
          generic = Option(args.map(a => mapType(a)))
        )
      case _ => Unknown

  private def mapIdent(tree: tpd.Tree)(using ctx: Context): TypeSchema =
    tree match
      case it: Ident if !it.tpe.typeSymbol.isPrimitiveValueClass =>
        val fields = tree.tpe.fields.map { field =>
          report.debugwarn(s"Find type tree: ${field.info.classSymbol}", tree.sourcePos)
          TypeSchema(
            name = Some(field.name.show),
            typeName = ctx.printer.nameString(field.info.typeSymbol)
          )
        }.toList
        TypeSchema(typeName = it.name.show, fields)
      case it: Ident =>
        TypeSchema(typeName = it.name.show)
      case _ => Unknown

  private def mapType(tree: tpd.Tree)(using ctx: Context): TypeSchema =
    report.debugwarn(s"Find type tree: $tree", tree.sourcePos)
    tree match
      case _: SeqLiteral                           => mapSeqLiteral(tree)
      case tdef @ TypeDef(_, _) if tdef.isClassDef => mapTypeDef(tree)
      case v: ValDef                               => mapValDef(tree).copy(name = Some(v.name.show))
      case _: AppliedTypeTree                      => mapAppliedTypeTree(tree)
      case _: Ident                                => mapIdent(tree)
      case _                                       => Unknown
  end mapType
