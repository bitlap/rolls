package bitlap.rolls.compiler.plugin

import dotty.tools.dotc.ast.tpd.*
import dotty.tools.dotc.core.Denotations.SingleDenotation
import dotty.tools.dotc.core.Names.*
import dotty.tools.dotc.core.Types.*
import dotty.tools.dotc.core.Contexts.Context
import dotty.tools.dotc.core.Symbols.ClassSymbol

/** @author
 *    梦境迷离
 *  @version 1.0,2023/3/31
 */
final case class Field(
  name: Name,
  thisDot: Select,
  tpe: Type,
  isPrivate: Boolean
)

extension (s: SingleDenotation)
  def toField(using clazz: ClassSymbol): Context ?=> Field = Field(
    s.name,
    This(clazz)
      .select(s.name, f => f.info.isParameterless),
    s.info,
    s.symbol.isPrivate
  )
