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
case class SimpleClass(field1: Int, field2: String, field3: Double, field4Opt: Option[String])

class BuilderConfigurationSuite extends FunSuite {

  test("encode simple class to csv string") {
    val simpleClass = SimpleClass(field1 = 1, field2 = "2", field3 = 0.4, None)
    val csv: String = simpleClass.into
      .withFieldComputed(_.field1, _ => "hello world")
      .encode

    val obj = csv
      .into[SimpleClass]
      .withFieldComputed(_.field1, _ => 1)
      .decode

    assertEquals(csv, "hello world,2,0.4,")
    assertEquals(obj, simpleClass)

  }
}
