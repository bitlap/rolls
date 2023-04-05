/*
 * Copyright (c) 2023 bitlap
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package bitlap.rolls.csv

import scala.compiletime.*
import scala.deriving.Mirror
import scala.util.Try

/** @author
 *    梦境迷离
 *  @version 1.0,2023/2/22
 */
trait Decoder[T]:
  def decode(str: String): T
end Decoder

object Decoder:

  def apply[A](using decoder: Decoder[A]): Decoder[A] = decoder

  given optionCodec[T: Decoder]: Decoder[Option[T]] = (s: String) =>
    if (s.isEmpty) None else Try(summon[Decoder[T]].decode(s)).toOption

  given Decoder[String]  = s => s
  given Decoder[Int]     = s => s.toInt
  given Decoder[Short]   = s => s.toShort
  given Decoder[Float]   = s => s.toFloat
  given Decoder[Double]  = s => s.toDouble
  given Decoder[Char]    = s => s.charAt(0)
  given Decoder[Byte]    = s => s.toByte
  given Decoder[Boolean] = s => s.toBoolean
  given Decoder[Long]    = s => s.toLong
