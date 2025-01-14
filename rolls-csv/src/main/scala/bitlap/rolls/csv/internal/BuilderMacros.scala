package bitlap.rolls.csv.internal

import scala.compiletime.*
import scala.deriving.Mirror
import scala.quoted.*

import bitlap.rolls.csv.*
import bitlap.rolls.csv.builder.*

/** @author
 *    梦境迷离
 *  @version 1.0,2023/4/5
 */
private[csv] object BuilderMacros {

  private type IsString[S <: String] = S

  transparent inline def dropCompletionField[
      SpecificBuilder[_, _ <: Tuple, _ <: Tuple],
      From,
      FromSubs <: Tuple,
      DerivedFromSubs <: Tuple,
      FieldType
  ](
      inline builder: SpecificBuilder[From, FromSubs, DerivedFromSubs],
      inline selector: From => FieldType
  ) = ${ dropCompletionFieldMacro('builder, 'selector) }

  inline def selectedField[From, Field](inline selector: From => Field): String =
    ${ selectedFieldMacro[From, Field]('selector) }

  private def selectedFieldMacro[From: Type, Field: Type](selector: Expr[From => Field])(using Quotes) =
    Expr(selectedFieldName(selector))

  private def dropCompletionFieldMacro[
      SpecificBuilder[_, _ <: Tuple, _ <: Tuple]: Type,
      From: Type,
      FromSubs <: Tuple: Type,
      DerivedFromSubs <: Tuple: Type,
      Field: Type
  ](
      builder: Expr[SpecificBuilder[From, FromSubs, DerivedFromSubs]],
      selector: Expr[From => Field]
  )(using Quotes) =
    import quotes.reflect.*
    val selectedField = selectedFieldName(selector).asConstantType
    selectedField match {
      case '[IsString[selectedField]] =>
        '{
          $builder.asInstanceOf[
            SpecificBuilder[
              From,
              FromSubs,
              Field.DropByLabel[selectedField, DerivedFromSubs]
            ]
          ]
        }
    }

  private def selectedFieldName[From: Type, FieldType](lambda: Expr[From => FieldType])(using Quotes): String = {
    import quotes.reflect.*
    val validFields = TypeRepr.of[From].typeSymbol.caseFields.map(_.name)
    lambda.asTerm match {
      case FieldSelector(fieldName) if validFields.contains(fieldName) => fieldName
      case _                                                           => report.errorAndAbort("Not a field selector!")
    }
  }

  extension (value: String) {

    def asConstantType(using Quotes): Type[? <: AnyKind] = {
      import quotes.reflect.*
      ConstantType(StringConstant(value)).asType
    }
  }
}
