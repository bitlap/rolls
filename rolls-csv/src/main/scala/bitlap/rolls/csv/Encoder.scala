package bitlap.rolls.csv

import scala.deriving.Mirror
import scala.compiletime.*

/** @author
 *    梦境迷离
 *  @version 1.0,2023/4/5
 */
@FunctionalInterface
trait Encoder[From]:
  def encode(from: From): String
end Encoder

object Encoder:

  def apply[A](using encoder: Encoder[A]): Encoder[A] = encoder

  given Encoder[String]  = s => s
  given Encoder[Int]     = s => s.toString
  given Encoder[Boolean] = s => s.toString
  given Encoder[Char]    = s => s.toString
  given Encoder[Float]   = s => s.toString
  given Encoder[Double]  = s => s.toString
  given Encoder[Short]   = s => s.toString
  given Encoder[Long]    = s => s.toString
  given Encoder[Byte]    = s => s.toString
  given optionCodec[T: Encoder]: Encoder[Option[T]] = s =>
    s match
      case Some(s) => summon[Encoder[T]].encode(s)
      case None    => ""

end Encoder
