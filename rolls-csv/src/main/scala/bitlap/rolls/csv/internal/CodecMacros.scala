package bitlap.rolls.csv.internal

import scala.deriving.Mirror
import scala.quoted.*

import bitlap.rolls.csv.*

/** @author
 *    梦境迷离
 *  @version 1.0,2023/4/9
 */
private[csv] object CodecMacros {

  inline def encode[From, DerivedFromSubs <: Tuple](
      computes: Map[FieldName, Any => String]
  )(using CSVFormat): Encoder[From] = { (from: From) =>
    val encoders = Derivation.encodersForAllFields[DerivedFromSubs]
    Construct.constructCSV(from.asInstanceOf[Product]) { (labelsToValuesOfFrom, label) =>
      val fieldValue                                  = labelsToValuesOfFrom(label)
      lazy val maybeValueFromDerived: Option[String]  = encoders.get(label).map(_.encode(fieldValue))
      lazy val maybeValueFromComputed: Option[String] = computes.get(label).map(f => f(fieldValue))
      maybeValueFromDerived.orElse(maybeValueFromComputed).getOrElse(fieldValue.toString)
    }
  }

  inline def decode[To, DerivedToSubs <: Tuple](
      toMirror: Mirror.ProductOf[To],
      computes: Map[FieldName, String => Any]
  )(using CSVFormat): Decoder[To] = { (to: String) =>
    val strings  = StringUtils.splitColumns(to)
    val decoders = Derivation.decodersForAllFields[DerivedToSubs]
    val valueArrayOfTo =
      Construct.constructInstance[toMirror.MirroredElemLabels](strings, 0, decoders, computes)
    toMirror.fromProduct(Tuple.fromArray(valueArrayOfTo.toArray))
  }
}
