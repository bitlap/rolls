package bitlap.rolls.csv.builder

import scala.compiletime.*
import scala.compiletime.ops.int.*
import scala.deriving.Mirror

import bitlap.rolls.csv.*
import bitlap.rolls.csv.builder.*
import bitlap.rolls.csv.internal.*

/** @author
 *    梦境迷离
 *  @version 1.0,2023/4/5
 */
final class AppliedEncoderBuilder[
    From,
    FromSubs <: Tuple,
    DerivedFromSubs <: Tuple
](
    private val appliedTo: From,
    private[csv] val computes: Map[FieldName, Any => String]
) extends EncoderBuilder[AppliedEncoderBuilder, From, FromSubs, DerivedFromSubs]:

  override def construct[DerivedFromSubs <: Tuple](
      computes: Map[FieldName, Any => String]
  ): AppliedEncoderBuilder[From, FromSubs, DerivedFromSubs] =
    new AppliedEncoderBuilder[From, FromSubs, DerivedFromSubs](this.appliedTo, computes)

  inline def encode(using csvFormat: CSVFormat = DefaultCSVFormat): String = build.encode(appliedTo)

end AppliedEncoderBuilder

object AppliedEncoderBuilder:

  transparent inline def create[From](appliedTo: From) =
    summonFrom {
      case from: Mirror.ProductOf[From] =>
        new AppliedEncoderBuilder[
          From,
          Field.FromLabelsAndTypes[from.MirroredElemLabels, from.MirroredElemTypes],
          Field.FromLabelsAndTypes[from.MirroredElemLabels, from.MirroredElemTypes]
        ](appliedTo, Map.empty)
      case _ => throw new Exception("Only support case classes!")

    }
end AppliedEncoderBuilder
