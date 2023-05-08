package bitlap.rolls.csv.test.builder

import scala.compiletime.testing.*

import bitlap.rolls.csv.*
import bitlap.rolls.csv.builder.*
import bitlap.rolls.csv.test.model.*

import munit.FunSuite

/** @author
 *    梦境迷离
 *  @version 1.0,2023/4/5
 */

class AppliedEncoderBuilderSuite extends FunSuite {

  test("encode simple class to csv string") {
    val simpleClass = SimpleClass(field1 = 1, field2 = "2", field3 = 0.4, None)
    val csv: String = simpleClass.into
      .withFieldComputed(_.field1, _ => "hello world")
      .encode
    assertEquals(csv, "hello world,2,0.4,")
  }

  test("encode simple class from csv list") {
    val metrics = Metric.`simple_data_objs`
    assert(metrics.head.dimensions.head.key == "city")
    assert(metrics.head.dimensions.head.value == "北京")

    val csv = metrics.map(metric =>
      metric.into.withFieldComputed(_.dimensions, ds => StringUtils.asString(ds.map(f => f.key -> f.value))).encode
    )

    println(csv)
    assert(
      csv.toString() == """List(100,1,"{""city"":""北京"",""os"":""Mac""}",vv,1, 100,1,"{""city"":""北京"",""os"":""Mac""}",pv,2, 100,1,"{""city"":""北京"",""os"":""Windows""}",vv,1, 100,1,"{""city"":""北京"",""os"":""Windows""}",pv,3, 100,2,"{""city"":""北京"",""os"":""Mac""}",vv,1, 100,2,"{""city"":""北京"",""os"":""Mac""}",pv,5, 100,3,"{""city"":""北京"",""os"":""Mac""}",vv,1, 100,3,"{""city"":""北京"",""os"":""Mac""}",pv,2, 200,1,"{""city"":""北京"",""os"":""Mac""}",vv,1, 200,1,"{""city"":""北京"",""os"":""Mac""}",pv,2, 200,1,"{""city"":""北京"",""os"":""Windows""}",vv,1, 200,1,"{""city"":""北京"",""os"":""Windows""}",pv,3, 200,2,"{""city"":""北京"",""os"":""Mac""}",vv,1, 200,2,"{""city"":""北京"",""os"":""Mac""}",pv,5, 200,3,"{""city"":""北京"",""os"":""Mac""}",vv,1, 200,3,"{""city"":""北京"",""os"":""Mac""}",pv,2)"""
    )
  }
}
