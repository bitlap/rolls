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

import java.util.regex.Pattern

import scala.collection.mutable.ListBuffer
import scala.util.matching.Regex

/** split csv column value by delimiter.
 *
 *  @author
 *    梦境迷离
 *  @version 1.0,2022/4/30
 */
object StringUtils:

  private val regex: Regex     = "\\{(.*?)\\}".r
  private val kvr: Regex       = "(.*):(.*)".r
  private val pattern: Pattern = Pattern.compile(regex.toString())

  private def extractJsonPairs(jsonString: String): String =
    val matcher = pattern.matcher(jsonString)
    while (matcher.find) {
      val tail = matcher.group().tail.init
      if (tail != null && tail.nonEmpty) {
        return tail
      } else return null
    }
    null

  def lowerUnderscore(camelCaseString: String): String =
    if (camelCaseString == null || camelCaseString.isEmpty) return ""
    val charArray = camelCaseString.toCharArray
    val buffer    = new StringBuffer
    var i         = 0
    val l         = charArray.length
    while (i < l) {
      if (charArray(i) >= 65 && charArray(i) <= 90) buffer.append("_").append((charArray(i).toInt + 32).toChar)
      else buffer.append(charArray(i))
      i += 1
    }
    buffer.toString

  def asString[K, V](KeyValuePairs: Seq[(K, V)]): String =
    s"""\"{${KeyValuePairs.map(kv => s"""\"\"${kv._1}\"\":\"\"${kv._2}\"\"""").mkString(",")}}\""""

  def asClasses[T](jsonString: String)(jsonKeyValue: (String, String) => T): List[T] =
    val pairs = extractJsonPairs(jsonString)
    if (pairs == null) return Nil
    val jsonElements = pairs.split(",")
    val kvs = jsonElements.collect {
      case kvr(k, v) if k.length > 2 && v.length > 2 => k.init.tail -> v.init.tail
    }
    kvs.toList.map(f => jsonKeyValue(f._1, f._2))

  def combineColumns(values: List[String])(using format: CSVFormat): String =
    if (values.isEmpty) ""
    else values.mkString(format.delimiter.toString)

  def firstDelimiterIndex(line: => String)(using format: CSVFormat): Int =
    val chars = line.toCharArray
    var idx   = 0
    while (idx < chars.length)
      chars(idx) match {
        case c if c == format.delimiter => return idx + 1
        case _                          => idx += 1
      }
    0
  end firstDelimiterIndex

  def splitColumns(line: => String)(using format: CSVFormat): List[String] =
    val listBuffer   = ListBuffer[String]()
    val columnBuffer = ListBuffer[Char]()
    val chars        = line.toCharArray

    var idx = 0
    while (idx < chars.length)
      chars(idx) match {
        case c if c == format.delimiter =>
          listBuffer.append(columnBuffer.mkString)
          columnBuffer.clear()
          idx += 1
        case c if c == format.escapeChar =>
          idx += 1
          var isTail = false
          while (idx < chars.length && !isTail)
            if (chars(idx) == format.escapeChar && idx + 1 < chars.length && chars(idx + 1) == format.escapeChar) {
              columnBuffer.append(format.escapeChar)
              idx += 2
            } else if (chars(idx) == format.escapeChar) {
              isTail = true
              idx += 1
            } else {
              columnBuffer.append(chars(idx))
              idx += 1
            }
        case c =>
          columnBuffer.append(c)
          idx += 1
      }
    if (columnBuffer.nonEmpty) {
      listBuffer.append(columnBuffer.mkString)
      columnBuffer.clear()
    }
    // TODO
    if (line.last == format.delimiter) {
      listBuffer.append("")
    }
    listBuffer.result()
  end splitColumns
