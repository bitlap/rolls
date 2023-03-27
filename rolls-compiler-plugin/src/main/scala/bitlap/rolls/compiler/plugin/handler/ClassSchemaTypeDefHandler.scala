package bitlap.rolls.compiler.plugin.handler

import bitlap.rolls.compiler.plugin.*
import dotty.tools.dotc.ast.tpd
import dotty.tools.dotc.ast.tpd.*
import dotty.tools.dotc.core.Contexts.Context
import dotty.tools.dotc.core.Flags.*
import dotty.tools.dotc.core.StdNames.*
import dotty.tools.dotc.core.Symbols.*
import dotty.tools.dotc.quoted.reflect.FromSymbol
import dotty.tools.dotc.report

/** @author
 *    梦境迷离
 *  @version 1.0,2023/3/21
 */
final class ClassSchemaTypeDefHandler extends TypeDefHandler:

  private final val productMethods = Seq(
    "productPrefix",
    "productElement",
    "productElementName",
    "productArity",
    "equals",
    "canEqual",
    "toString",
    "hashCode",
    "copy"
  )

  override val annotationFullNames: List[String] = List("bitlap.rolls.annotations.ClassSchema")

  private lazy val Unknown = TypeSchema(typeName = "Unknown", fields = List.empty)

  override def existsAnnot(tree: TypeDef)(using ctx: Context): Boolean = {
    lazy val annotCls = annotationFullNames.map(requiredClass(_))
    lazy val existsAnnotOnClassContr =
      tree.tpe.typeSymbol.primaryConstructor.annotations.exists(p => annotCls.contains(p.symbol))
    lazy val exists = tree.mods.annotations.collectFirst {
      case Apply(Select(New(Ident(an)), _), _) if annotCls.exists(_.name.asSimpleName == an.asSimpleName) =>
        true
      case _ => false
    }.getOrElse(false)

    report.debugwarn(s"ExistsAnnot exists:$exists, existsAnnotOnClassContr:$existsAnnotOnClassContr")
    exists || existsAnnotOnClassContr
  }

  def handle(tree: TypeDef)(using ctx: Context): tpd.TypeDef = {
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
        report.debugwarn(s"DefDef: ${dd.name.show}, mods:${dd.mods},${productMethods.contains(dd.name.show)}")
        if !dd.mods.isOneOf(Local | Protected | Private | Abstract | Synthetic | ParamAccessor | Implicit) &&
          !productMethods.contains(dd.name.show)
        then
          Some(
            MethodSchema(
              methodName = dd.name.show,
              params = dd.termParamss.flatten.map(mapType),
              resultType = mapType(dd.tpt)
            )
          )
        else None
      case _ => None

  private def mapSeqLiteral(tree: tpd.SeqLiteral)(using ctx: Context): TypeSchema =
    report.debugwarn(s"SeqLiteral: $tree")
    TypeSchema(typeName = tree.show, fields = tree.elems.map(mapType))

  private def mapTypeDef(tree: tpd.TypeDef)(using ctx: Context): TypeSchema =
    tree match
      case tdef: TypeDef if tdef.isClassDef =>
        if tdef.tpe.typeSymbol == ctx.definitions.ListClass || tdef.tpe.typeSymbol == ctx.definitions.SeqModule
        then
          val infos    = tdef.tpe.typeParams.map(_.paramInfo)
          val typeTree = infos.map(_.typeSymbol).map(s => FromSymbol.definitionFromSym(s)).map(tr => mapType(tr))
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
          if it.tpe.typeSymbol.isPrimitiveValueClass ||
            it.tpe <:< ctx.definitions.SingletonType || it.tpe <:< ctx.definitions.StringType =>
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
