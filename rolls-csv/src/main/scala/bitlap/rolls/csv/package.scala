package bitlap.rolls.csv

import bitlap.rolls.csv.builder.*

/** @author
 *    梦境迷离
 *  @version 1.0,2023/4/5
 */
extension [From <: Product](value: From)

  transparent inline def into: AppliedEncoderBuilder[From, _ <: Tuple, _ <: Tuple] =
    AppliedEncoderBuilder.create[From](value)

  def to(using Encoder[From]): String = Encoder[From].encode(value)

end extension

extension [A <: String](value: A)

  transparent inline def into[To]: AppliedDecoderBuilder[To, _ <: Tuple, _ <: Tuple] =
    AppliedDecoderBuilder.create[To](String.valueOf(value))

  def to[To](using Decoder[To]): To = Decoder[To].decode(value)

end extension
