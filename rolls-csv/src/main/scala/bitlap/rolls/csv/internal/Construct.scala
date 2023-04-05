package bitlap.rolls.csv.internal

import bitlap.rolls.csv.*

import scala.collection.mutable.ListBuffer
import scala.deriving.Mirror
import scala.compiletime.ops.int.*
import scala.compiletime.*

/** @author
 *    梦境迷离
 *  @version 1.0,2023/4/5
 */
private[csv] object Construct:

  inline def constructInstance[L](
    values: List[String],
    i: Int,
    decoders: Map[FieldName, Decoder[Any]],
    computes: Map[FieldName, String => Any]
  ): List[Any] =
    inline erasedValue[L] match
      case _: EmptyTuple => Nil
      case _: (l *: tl) =>
        val label                       = FieldName(constValue[l].asInstanceOf[String])
        val value                       = values(i)
        lazy val maybeValueFromDerived  = decoders.get(label).map(_.decode(value))
        lazy val maybeValueFromComputed = computes.get(label).map(f => f(value))
        val vl                          = maybeValueFromDerived orElse maybeValueFromComputed getOrElse value
        List(vl) ::: constructInstance[tl](values, i + 1, decoders, computes)

  end constructInstance

  inline def constructCSV(
    from: Product
  )(unsafeMapper: (Map[FieldName, Any], FieldName) => String)(using csvFormat: CsvFormat): String = {
    val labelsToValuesOfFrom = FieldName.wrapAll(from.productElementNames.zip(from.productIterator).toMap)
    val result = from.productElementNames.map { label =>
      unsafeMapper(labelsToValuesOfFrom, FieldName(label))
    }
    StringUtils.combineColumns(result.toList)
  }

  inline def labelIndices[
    Labels <: Tuple,
    Acc <: Int
  ]: Map[FieldName, Int] =
    inline erasedValue[Labels] match {
      case _: EmptyTuple => Map.empty
      case _: (h *: t) =>
        val labelToIndex = FieldName(constValue[h].asInstanceOf[String]) -> constValue[Acc]
        labelIndices[t, S[Acc]] + labelToIndex
    }

end Construct
