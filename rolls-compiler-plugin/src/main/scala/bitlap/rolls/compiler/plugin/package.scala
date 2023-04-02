package bitlap.rolls.compiler.plugin

import dotty.tools.dotc.ast.tpd.*
import dotty.tools.dotc.ast.{ tpd, untpd }
import dotty.tools.dotc.core.Annotations.{ Annotation, ConcreteAnnotation }
import dotty.tools.dotc.core.Denotations.SingleDenotation
import dotty.tools.dotc.core.Names.*
import dotty.tools.dotc.core.Types.*
import dotty.tools.dotc.core.Contexts.Context
import dotty.tools.dotc.core.Symbols
import dotty.tools.dotc.core.Symbols.{ defn, requiredClass, ClassSymbol, Symbol }
import dotty.tools.dotc.quoted.reflect.FromSymbol

import scala.collection.immutable.List

/** @author
 *    梦境迷离
 *  @version 1.0,2023/3/31
 */
final case class Field(
  name: Name,
  thisDot: Select,
  tpe: Type,
  isPrivate: Boolean,
  annotations: List[Tree] // on term
):
  def containsAnnotation(annotation: Name): Context ?=> Boolean =
    (annotations ++ getAnnotatedTypeAnnotation.toList).collectFirst {
      case Apply(Select(New(Ident(an)), _), List()) if an.asSimpleName == annotation => true
    }.getOrElse(false)
  end containsAnnotation

  def getAnnotatedTypeAnnotation: Context ?=> Option[Tree] =
    tpe match
      case a @ AnnotatedType(_, ConcreteAnnotation(Apply(Select(New(Ident(_)), _), _))) => Option(a.annot.tree)
      case _                                                                            => None
  end getAnnotatedTypeAnnotation

end Field

final case class TypeFieldTree(
  name: String,
  tpe: Type,
  typeTree: Tree,
  argTypes: List[Tree]
)

final case class TypeTypeTree(
  typeSymbol: Tree,
  argTypes: List[Tree]
)

final case class TypeClassDef(
  name: String,
  typeParams: List[Tree],
  template: Template,
  typeSymbol: Symbol,
  annotations: List[untpd.Tree],
  classSymbol: ClassSymbol,
  contrAnnotations: List[Tree],
  primaryConstructor: Symbol,
  isProduct: Boolean
)
extension (s: SingleDenotation)
  // from type
  def toField(using clazz: ClassSymbol): Context ?=> Field = Field(
    s.name,
    This(clazz)
      .select(s.name, f => f.info.isParameterless),
    s.info,
    s.symbol.isPrivate,
    s.symbol.annotations.map(_.tree)
  )

  def toFieldTree: Context ?=> TypeFieldTree = TypeFieldTree(
    s.name.show,
    s.info,
    FromSymbol.definitionFromSym(s.info.typeSymbol),
    s.info.argTypes.map(_.typeSymbol).map(FromSymbol.definitionFromSym)
  )
end extension

extension (s: Symbol)
  // from primaryConstructor symbol
  def toField(using clazz: ClassSymbol): Context ?=> Field = Field(
    s.name,
    This(clazz)
      .select(s.name, f => f.info.isParameterless),
    s.info,
    s.isPrivate,
    s.annotations.map(_.tree)
  )

end extension

extension (ts: TypeTree)
  def toTypeTree: Context ?=> TypeTypeTree = TypeTypeTree(
    FromSymbol.definitionFromSym(ts.tpe.typeSymbol),
    ts.tpe.argTypes.map(_.typeSymbol).map(FromSymbol.definitionFromSym)
  )

end extension

extension (td: TypeDef)

  def isProduct: Context ?=> Boolean = td.symbol.asClass.parentSyms.contains(defn.ProductClass)

  def getPrimaryConstructor: Context ?=> Symbols.Symbol = td.tpe.typeSymbol.primaryConstructor

  def getName: Context ?=> String =
    td.name.show

  def getAnnotations: Context ?=> List[untpd.Tree] =
    td.mods.annotations

  def getContrAnnotations: Context ?=> List[tpd.Tree] =
    if (td.isClassDef && isProduct)
      val typeContrAnnots = td.tpe.typeConstructor.typeSymbol.annotations
      val contrAnnots     = getPrimaryConstructor.annotations
      contrAnnots.map(f => FromSymbol.definitionFromSym(f.symbol)) ++ typeContrAnnots.map(_.tree)
    else getPrimaryConstructor.annotations.map(f => FromSymbol.definitionFromSym(f.symbol))

  def toClassDef: Context ?=> TypeClassDef = TypeClassDef(
    td.name.show,
    td.tpe.typeParams
      .map(_.paramInfo)
      .map(_.typeSymbol)
      .map(s => FromSymbol.definitionFromSym(s)),
    if (td.isClassDef) td.rhs.asInstanceOf[Template] else throw new Exception(s"Cannot get template from type:$td"),
    td.tpe.typeSymbol,
    getAnnotations,
    td.symbol.asClass,
    getContrAnnotations,
    getPrimaryConstructor,
    isProduct
  )
end extension
