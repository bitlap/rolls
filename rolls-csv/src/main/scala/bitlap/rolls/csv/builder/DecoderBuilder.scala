package bitlap.rolls.csv.builder

import scala.compiletime.*
import scala.deriving.*

import bitlap.rolls.csv.*
import bitlap.rolls.csv.internal.*

/** @author
 *    梦境迷离
 *  @version 1.0,2023/4/5
 */
trait DecoderBuilder[
    SpecificBuilder[_, _ <: Tuple, _ <: Tuple],
    To,
    ToSubs <: Tuple,
    DerivedToSubs <: Tuple
]:
  private[csv] val computes: Map[FieldName, String => Any]

  final transparent inline def withFieldComputed[Field](
      inline selector: To => Field,
      f: String => Field
  ) = {
    val selectedField   = BuilderMacros.selectedField(selector)
    val computedField   = FieldName(selectedField) -> f.asInstanceOf[String => Field]
    val modifiedBuilder = this.construct[DerivedToSubs](computes + computedField)
    BuilderMacros.dropCompletionField(modifiedBuilder, selector)
  }

  final inline def build(using csvFormat: CSVFormat): Decoder[To] =
    summonFrom {
      case toMirror: Mirror.ProductOf[To] => CodecMacros.decode[To, DerivedToSubs](toMirror, computes)
      case _                              => throw new Exception("Decoder Only support case classes!")
    }

  def construct[DerivedToSubs <: Tuple](
      computes: Map[FieldName, String => Any] = this.computes
  ): SpecificBuilder[To, ToSubs, DerivedToSubs]

end DecoderBuilder
