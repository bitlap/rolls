package bitlap.rolls.compiler.plugin

import bitlap.rolls.compiler.plugin.*
import dotty.tools.dotc.ast.tpd
import dotty.tools.dotc.ast.tpd.*
import dotty.tools.dotc.core.Contexts.Context
import dotty.tools.dotc.core.Flags.*
import dotty.tools.dotc.core.Symbols.*
import dotty.tools.dotc.core.Types
import dotty.tools.dotc.plugins.PluginPhase
import dotty.tools.dotc.quoted.reflect.FromSymbol
import dotty.tools.dotc.report
import dotty.tools.dotc.transform.{ PickleQuotes, Staging }

/** @author
 *    梦境迷离
 *  @version 1.0,2023/3/21
 */
final class ClassSchemaPhase extends PluginPhase with TypeDefPluginPhaseFilter:

  override val phaseName               = "ClassSchemaPhase"
  override val runsAfter: Set[String]  = Set(Staging.name)
  override val runsBefore: Set[String] = Set(PickleQuotes.name)

  override def transformTypeDef(tree: tpd.TypeDef)(using ctx: Context): tpd.Tree =
    if (existsAnnot(tree)) handle(tree) else tree
  end transformTypeDef

  private lazy val Unknown = TypeSchema(typeName = "Unknown", fields = List.empty)

  override val annotationFullNames: List[String] = List("bitlap.rolls.annotations.classSchema")

  override def handle(tree: TypeDef)(using ctx: Context): tpd.TypeDef = {
    if tree.isClassDef then
      val template     = tree.rhs.asInstanceOf[Template]
      val methodSchema = template.body.map(mapDefDef).collect { case Some(value) => value }
      report.debugwarn(s"Find name:${tree.name}, methodSchema:$methodSchema")
      val classSchema = ClassSchema(tree.name.show, methodSchema)
      report.debugwarn(s"Find classSchema: $classSchema")
      Utils.sendClassSchema(classSchema)
    tree
  }

  def mapDefDef(tree: tpd.Tree)(using ctx: Context): Option[MethodSchema] =
    tree match
      case dd: DefDef =>
        if !dd.mods.isOneOf(Local | Protected | Private | Abstract | Synthetic | ParamAccessor | Implicit) &&
          !defn.syntheticCoreMethods.map(_.name).contains(dd.name) &&
          !defn.caseClassSynthesized.map(_.name).contains(dd.name)
        then
          Some(
            MethodSchema(
              methodName = dd.name.show,
              params = dd.termParamss.flatten.map(mapType),
              resultType = mapType(dd.tpt)
            )
          )
        else {
          report.debugwarn(s"DefDef: ${dd.name.show}, mods:${dd.mods}")
          None
        }
      case _ => None

  private def mapSeqLiteral(tree: tpd.SeqLiteral)(using ctx: Context): TypeSchema =
    report.debugwarn(s"SeqLiteral: $tree")
    TypeSchema(typeName = tree.show, fields = tree.elems.map(mapType))

  private def mapTypeDef(tree: tpd.TypeDef)(using ctx: Context): TypeSchema =
    tree match
      case tdef: TypeDef if tdef.isClassDef =>
        report.debugwarn(s"Iterable: $tree")
        lazy val IterableType: Types.TypeRef = requiredClassRef("scala.collection.Iterable")
        if tdef.tpe <:< IterableType
        then
          val typeTree = tdef.tpe.typeParams
            .map(_.paramInfo)
            .map(_.typeSymbol)
            .map(s => FromSymbol.definitionFromSym(s))
            .map(tr => mapType(tr))
          TypeSchema(typeName = tdef.name.show, genericType = Option(typeTree))
        else mapTemplate(tdef)
      case tdef: TypeDef =>
        TypeSchema(typeName = tdef.name.show)
      case null =>
        Unknown

  private def mapTemplate(tree: tpd.TypeDef)(using ctx: Context): TypeSchema = {
    val ps = tree.rhs.asInstanceOf[Template].body.collect { case vd: ValDef => mapType(vd) }
    TypeSchema(
      typeName = tree.name.show,
      fields = if tree.tpe.typeSymbol.is(Abstract) then List.empty else ps
    )
  }

  private def mapTypeTree(tree: tpd.TypeTree)(using ctx: Context): TypeSchema =
    val actualGeneric = tree.tpe.argTypes.map(_.typeSymbol).map(FromSymbol.definitionFromSym).map(mapType)
    val typeTree      = FromSymbol.definitionFromSym(tree.tpe.typeSymbol)
    mapType(typeTree).copy(genericType = Option(actualGeneric))

  private def mapValDef(name: String, tree: tpd.ValDef)(using ctx: Context): TypeSchema =
    report.debugwarn(s"ValDef: $tree")
    mapType(tree.tpt).copy(fieldName = Some(name))

  private def mapAppliedTypeTree(tree: tpd.AppliedTypeTree)(using ctx: Context): TypeSchema =
    report.debugwarn(s"AppliedType: $tree")
    TypeSchema(
      typeName = ctx.printer.nameString(tree.tpt.symbol),
      genericType = Option(tree.args.map(a => mapType(a)))
    )

  private def mapRefTree(tree: tpd.RefTree)(using ctx: Context): TypeSchema =
    report.debugwarn(s"RefTree: $tree")
    tree match
      case t: Ident => mapIdent(t)
      case _        => Unknown

  private def mapIdent(tree: tpd.Ident)(using ctx: Context): TypeSchema =
    tree match
      case it: Ident
          if it.tpe.typeSymbol.isPrimitiveValueClass || it.tpe <:< defn.StringType || it.tpe <:< defn.SingletonType =>
        TypeSchema(typeName = it.name.show)
      case it: Ident =>
        val fields = tree.tpe.fields.map { field =>
          val itTree        = FromSymbol.definitionFromSym(field.info.typeSymbol)
          val actualGeneric = field.info.argTypes.map(_.typeSymbol).map(FromSymbol.definitionFromSym).map(mapType)
          mapType(itTree).copy(fieldName = Some(field.name.show)).copy(genericType = Option(actualGeneric))
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
