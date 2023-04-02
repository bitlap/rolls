package bitlap.rolls.compiler.plugin

import bitlap.rolls.compiler.plugin.*
import dotty.tools.dotc.ast.tpd.*
import dotty.tools.dotc.core.Contexts.Context
import dotty.tools.dotc.core.Flags.*
import dotty.tools.dotc.core.Symbols.*
import dotty.tools.dotc.core.Types
import dotty.tools.dotc.plugins.PluginPhase
import dotty.tools.dotc.quoted.reflect.FromSymbol
import dotty.tools.dotc.report
import dotty.tools.dotc.transform.{ PickleQuotes, Staging }

import scala.annotation.threadUnsafe

/** @author
 *    梦境迷离
 *  @version 1.0,2023/3/21
 */
final class ClassSchemaPhase(setting: RollsSetting) extends PluginPhase with TypeDefPluginPhaseFilter:

  override val phaseName               = "ClassSchemaPhase"
  override val runsAfter: Set[String]  = Set(Staging.name)
  override val runsBefore: Set[String] = Set(PickleQuotes.name)

  override def transformTypeDef(tree: TypeDef)(using Context): Tree =
    if (existsAnnot(tree)) handle(tree) else tree
  end transformTypeDef

  @threadUnsafe private lazy val Unknown = TypeSchema(typeName = "Unknown", fields = List.empty)

  override val annotationFullNames: List[String] = List(setting.config.classSchema)

  override def handle(tree: TypeDef): Context ?=> TypeDef =
    if tree.isClassDef then
      val typeTypeTree = tree.toClassDef
      val methodSchema = typeTypeTree.template.body.map(mapDefDef).collect { case Some(value) => value }
      val classSchema  = ClassSchema(typeTypeTree.name, methodSchema)
      Utils.sendClassSchema(classSchema, setting.config)
    tree
  end handle

  def mapDefDef(tree: Tree): Context ?=> Option[MethodSchema] =
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
        else None
      case _ => None
  end mapDefDef

  private def mapSeqLiteral(tree: SeqLiteral): Context ?=> TypeSchema =
    TypeSchema(typeName = tree.show, fields = tree.elems.map(mapType))

  private def mapTypeDef(tree: TypeDef): Context ?=> TypeSchema =
    tree match
      case tpeDef: TypeDef if tpeDef.isClassDef =>
        lazy val IterableType: Types.TypeRef = requiredClassRef("scala.collection.Iterable")
        if tpeDef.tpe <:< IterableType
        then
          val typeTree = tpeDef.toClassDef
          TypeSchema(typeName = typeTree.name, genericType = Option(typeTree.typeParams.map(tr => mapType(tr))))
        else mapTemplate(tpeDef)
      case tpeDef: TypeDef =>
        TypeSchema(typeName = tpeDef.name.show)
  end mapTypeDef

  private def mapTemplate(tree: TypeDef): Context ?=> TypeSchema =
    val typeTree = tree.toClassDef
    val fields   = typeTree.template.body.collect { case vd: ValDef => mapType(vd) }
    TypeSchema(
      typeName = typeTree.name,
      fields = if typeTree.typeSymbol.is(Abstract) then List.empty else fields
    )
  end mapTemplate

  private def mapTypeTree(tree: TypeTree): Context ?=> TypeSchema =
    val typeTypeTree  = tree.toTypeTree
    val actualGeneric = typeTypeTree.argTypes.map(mapType)
    val typeTree      = typeTypeTree.typeSymbol
    mapType(typeTree).copy(genericType = Option(actualGeneric))

  private def mapValDef(name: String, tree: ValDef): Context ?=> TypeSchema =
    mapType(tree.tpt).copy(fieldName = Some(name))

  private def mapAppliedTypeTree(tree: AppliedTypeTree)(using ctx: Context): TypeSchema =
    TypeSchema(
      typeName = ctx.printer.nameString(tree.tpt.symbol),
      genericType = Option(tree.args.map(a => mapType(a)))
    )
  end mapAppliedTypeTree

  private def mapRefTree(tree: RefTree): Context ?=> TypeSchema =
    tree match
      case t: Ident => mapIdent(t)
      case _        => Unknown
  end mapRefTree

  private def mapIdent(tree: Ident): Context ?=> TypeSchema =
    tree match
      case it: Ident
          if it.tpe.typeSymbol.isPrimitiveValueClass || it.tpe <:< defn.StringType || it.tpe <:< defn.SingletonType =>
        TypeSchema(typeName = it.name.show)
      case it: Ident =>
        val fields = tree.tpe.fields.map { field =>
          val fieldType = field.toFieldTree
          mapType(fieldType.typeTree)
            .copy(fieldName = Some(fieldType.name))
            .copy(genericType = Option(fieldType.argTypes.map(mapType)))
        }.toList

        TypeSchema(typeName = it.name.show, fields)
  end mapIdent

  private def mapType(tree: Tree): Context ?=> TypeSchema =
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
