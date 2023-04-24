package bitlap.rolls.csv.test

import bitlap.rolls.csv.*
import bitlap.rolls.csv.test.model.*
import munit.FunSuite

/** @author
 *    梦境迷离
 *  @version 1.0,2022/4/30
 */
class StringUtilsSuite extends FunSuite {

  test("StringUtilsTest#splitColumns ok") {
    val line = """abc,"{""a"":""b"",""c"":""d""}",d,12,2,false,0.1,0.23333"""
    val csv  = StringUtils.splitColumns(line)(using DefaultCSVFormat)
    println(csv)
    assert(csv.size == 8)
  }

  test("StringUtilsTest#extractJsonValues ok") {
    // only extract json `"{""a"":""b"",""c"":""d""}"`
    val line = """abc,"{""a"":""b"",""c"":""d""}",d,12,2,false,0.1,0.23333"""
    val csv  = StringUtils.asClasses[Dimension](line)((k, v) => Dimension(k, v))
    println(csv)
    assert(csv.toString() == "List(Dimension(\"a\",\"b\"), Dimension(\"c\",\"d\"))")
  }
}
