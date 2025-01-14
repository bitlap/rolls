package bitlap.rolls.compiler.plugin

import scala.collection.immutable.List

import dotty.tools.dotc.ast.*
import dotty.tools.dotc.ast.tpd.*
import dotty.tools.dotc.core.Annotations.*
import dotty.tools.dotc.core.Contexts.Context
import dotty.tools.dotc.core.Denotations.SingleDenotation
import dotty.tools.dotc.core.Flags.*
import dotty.tools.dotc.core.Names.*
import dotty.tools.dotc.core.StdNames.nme
import dotty.tools.dotc.core.Symbols
import dotty.tools.dotc.core.Symbols.*
import dotty.tools.dotc.core.Types.*
import dotty.tools.dotc.quoted.reflect.FromSymbol

/** @author
 *    梦境迷离
 *  @version 1.0,2023/3/31
 */
final case class FieldTree(
    name: Name,
    thisDot: Select,
    tpe: Type,
    isPrivate: Boolean,
    annotations: List[Tree] // on term
):

  def containsAnnotation(annotation: Name): Context ?=> Boolean =
    (annotations ++ getAnnotatedTypeAnnotation.toList).collectFirst {
      case Apply(Select(New(Ident(an)), _), _) if an.asSimpleName == annotation => true
    }.getOrElse(false)
  end containsAnnotation

  def getAnnotatedTypeAnnotation: Context ?=> Option[Tree] =
    tpe match
      case a @ AnnotatedType(_, ConcreteAnnotation(Apply(Select(New(Ident(_)), _), _))) => Option(a.annot.tree)
      case _                                                                            => None
  end getAnnotatedTypeAnnotation

end FieldTree

final case class SimpleFieldTree(
    name: String,
    tpe: Type,
    typeTree: Tree,
    argTypes: List[Tree]
)

final case class TpeTree(
    typeSymbol: Tree,
    argTypes: List[Tree]
)

final case class ClassTree(
    name: String,
    typeParams: List[Tree],
    template: Template,
    typeSymbol: Symbol,
    annotations: List[untpd.Tree],
    classSymbol: ClassSymbol,
    contrAnnotations: List[Tree],
    primaryConstructor: Symbol,
    isCaseClass: Boolean
)

extension (s: SingleDenotation)

  def toSimpleFieldTree: Context ?=> SimpleFieldTree = SimpleFieldTree(
    s.name.show,
    s.info,
    FromSymbol.definitionFromSym(s.info.typeSymbol),
    s.info.argTypes.map(_.typeSymbol).map(FromSymbol.definitionFromSym)
  )
end extension

extension (s: Symbol)

  // from primaryConstructor symbol
  def toFieldTree(using clazz: ClassSymbol): Context ?=> FieldTree = FieldTree(
    s.name,
    This(clazz)
      .select(s.name, _.info.isParameterless),
    s.info,
    s.isPrivate,
    s.annotations.map(_.tree)
  )

end extension

extension (ts: TypeTree)

  def toTypeTree: Context ?=> TpeTree = TpeTree(
    FromSymbol.definitionFromSym(ts.tpe.typeSymbol),
    ts.tpe.argTypes.map(_.typeSymbol).map(FromSymbol.definitionFromSym)
  )

end extension

extension (td: TypeDef)

  def isCaseClass: Context ?=> Boolean =
    td.symbol.asClass.parentSyms.contains(defn.ProductClass) &&
    td.tpe.member(nme.copy).filterWithFlags(Synthetic, EmptyFlags).exists

  def getPrimaryConstructor: Context ?=> Symbols.Symbol = td.tpe.typeSymbol.primaryConstructor

  def showName: Context ?=> String =
    td.name.show

  def getAnnotations: Context ?=> List[untpd.Tree] =
    td.mods.annotations

  def getContrAnnotations: Context ?=> List[tpd.Tree] =
    if (td.isClassDef && isCaseClass)
      val typeContrAnnots = td.tpe.typeConstructor.typeSymbol.annotations
      val contrAnnots     = getPrimaryConstructor.annotations
      contrAnnots.map(f => FromSymbol.definitionFromSym(f.symbol)) ++ typeContrAnnots.map(_.tree)
    else getPrimaryConstructor.annotations.map(f => FromSymbol.definitionFromSym(f.symbol))

  def toClassTree: Context ?=> ClassTree = ClassTree(
    td.name.show,
    td.tpe.typeParams
      .map(_.paramInfo)
      .map(_.typeSymbol)
      .map(FromSymbol.definitionFromSym),
    if (td.isClassDef) td.rhs.asInstanceOf[Template] else throw new Exception(s"Cannot get template from type:$td"),
    td.tpe.typeSymbol,
    getAnnotations,
    td.symbol.asClass,
    getContrAnnotations,
    getPrimaryConstructor,
    isCaseClass
  )
end extension
