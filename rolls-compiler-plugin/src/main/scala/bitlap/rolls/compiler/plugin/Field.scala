package bitlap.rolls.compiler.plugin

import dotty.tools.dotc.ast.tpd.*
import dotty.tools.dotc.core.Annotations.Annotation
import dotty.tools.dotc.core.Denotations.SingleDenotation
import dotty.tools.dotc.core.Names.*
import dotty.tools.dotc.core.Types.*
import dotty.tools.dotc.core.Contexts.Context
import dotty.tools.dotc.core.Symbols.ClassSymbol
import dotty.tools.dotc.core.Symbols.Symbol

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
  def containsAnnotation(annotation: Name, annots: List[Tree] = List.empty): Boolean =
    (annotations ++ annots).collectFirst {
      case Apply(Select(New(Ident(an)), _), List()) if an.asSimpleName == annotation => true
    }.getOrElse(false)

end Field

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
