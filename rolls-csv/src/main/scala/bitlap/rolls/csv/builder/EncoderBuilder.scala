package bitlap.rolls.csv.builder

import scala.compiletime.*
import scala.deriving.*

import bitlap.rolls.csv.*
import bitlap.rolls.csv.internal.*

/** @author
 *    梦境迷离
 *  @version 1.0,2023/4/5
 */
trait EncoderBuilder[
    SpecificBuilder[_, _ <: Tuple, _ <: Tuple],
    From,
    FromSubs <: Tuple,
    DerivedFromSubs <: Tuple
]:
  private[csv] val computes: Map[FieldName, Any => String]

  final transparent inline def withFieldComputed[Field](
      inline selector: From => Field,
      f: Field => String
  ) = {
    val selectedField   = BuilderMacros.selectedField(selector)
    val computedField   = FieldName(selectedField) -> f.asInstanceOf[Any => String]
    val modifiedBuilder = this.construct[DerivedFromSubs](computes + computedField)
    BuilderMacros.dropCompletionField(modifiedBuilder, selector)
  }

  final inline def build(using csvFormat: CSVFormat): Encoder[From] =
    summonFrom {
      case fromMirror: Mirror.ProductOf[From] => CodecMacros.encode[From, DerivedFromSubs](computes)
      case _                                  => throw new Exception("Encoder Only support case classes!")
    }

  def construct[DerivedFromSubs <: Tuple](
      computes: Map[FieldName, Any => String] = this.computes
  ): SpecificBuilder[From, FromSubs, DerivedFromSubs]

end EncoderBuilder
