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
final class AppliedDecoderBuilder[
    To,
    ToSubs <: Tuple,
    DerivedToSubs <: Tuple
](
    private val appliedTo: String,
    private[csv] override val computes: Map[FieldName, String => Any]
) extends DecoderBuilder[AppliedDecoderBuilder, To, ToSubs, DerivedToSubs]:

  override def construct[DerivedToSubs <: Tuple](
      computes: Map[FieldName, String => Any]
  ): AppliedDecoderBuilder[To, ToSubs, DerivedToSubs] =
    new AppliedDecoderBuilder[To, ToSubs, DerivedToSubs](this.appliedTo, computes)

  inline def decode(using csvFormat: CSVFormat = DefaultCSVFormat): To = build.decode(appliedTo)

end AppliedDecoderBuilder

object AppliedDecoderBuilder:

  transparent inline def create[To](appliedTo: String) =
    summonFrom {
      case to: Mirror.ProductOf[To] =>
        new AppliedDecoderBuilder[
          To,
          Field.FromLabelsAndTypes[to.MirroredElemLabels, to.MirroredElemTypes],
          Field.FromLabelsAndTypes[to.MirroredElemLabels, to.MirroredElemTypes]
        ](appliedTo, Map.empty)
      case _ => throw new Exception("Only support case classes!")

    }
end AppliedDecoderBuilder
