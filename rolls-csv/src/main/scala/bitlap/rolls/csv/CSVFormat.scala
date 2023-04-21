/*
 * Copyright (c) 2022 bitlap
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

/** @author
 *    梦境迷离
 *  @version 1.0,2023/4/5
 */
trait CSVFormat extends Serializable {

  def delimiter: Char

  def escapeChar: Char

  /** Mode for writing string into files.
   */
  def append: Boolean = false

  /** String Charset.
   */
  def stringCharset: StringCharset = StringCharset.`UTF-8`

  /** Does the CSV file contain header?
   */
  def hasHeaders: Boolean = false

  /** Does the CSV file contain row index?
   */
  def hasColIndex: Boolean = false

}

object DefaultCSVFormat extends CSVFormat {
  val delimiter: Char  = ','
  val escapeChar: Char = '"'
}

object TSVFormat extends CSVFormat {
  val delimiter: Char  = '\t'
  val escapeChar: Char = '\\'
}
