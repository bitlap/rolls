package bitlap.rolls.csv.test.builder

import bitlap.rolls.csv.*
import bitlap.rolls.csv.builder.*
import bitlap.rolls.csv.test.model.*
import munit.FunSuite

import scala.compiletime.testing.*

/** @author
 *    梦境迷离
 *  @version 1.0,2023/4/5
 */

class AppliedDecoderBuilderSuite extends FunSuite {

  test("decode simple class to csv string") {
    val obj = "hello world,2,0.4,"
      .into[SimpleClass]
      .withFieldComputed(_.field1, _ => 1)
      .decode
    assertEquals(obj, SimpleClass(field1 = 1, field2 = "2", field3 = 0.4, None))

  }

  test("decode simple class from csv list") {
    val metrics: List[Metric] = Metric.`simple_data_string`
      .split("\n")
      .toList
      .map(csv =>
        csv
          .into[Metric]
          .withFieldComputed(_.dimensions, dims => StringUtils.asClasses(dims)((k, v) => Dimension(k, v)))
          .decode
      )

    println(metrics)

    assert(metrics.head.dimensions.head.key == "city")
    assert(metrics.head.dimensions.head.value == "北京")
  }
}
