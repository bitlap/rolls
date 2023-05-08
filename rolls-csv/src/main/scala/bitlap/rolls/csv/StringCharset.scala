package bitlap.rolls.csv

import java.nio.charset.Charset as JCharset

import scala.util.Try

/** @author
 *    梦境迷离
 *  @version 1.0,2023/4/21
 */
enum StringCharset(val charsetName: String):
  case `ISO-8859-1`                             extends StringCharset("ISO-8859-1")
  case `UTF-8`                                  extends StringCharset("UTF-8")
  case GBK                                      extends StringCharset("GBK")
  case LookUP(override val charsetName: String) extends StringCharset(charsetName)

  def charset: JCharset =
    Try(JCharset.forName(charsetName)).getOrElse(JCharset.defaultCharset)
