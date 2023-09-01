package bitlap.rolls.core.jdbc.test

import scala.runtime.Tuples

import bitlap.rolls.core.extensions.*
import bitlap.rolls.core.jdbc.*

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

/** @author
 *    梦境迷离
 *  @version 1.0,2023/4/8
 */
class UtilsSpec extends AnyFlatSpec with Matchers {

  final case class Test(i: String)

  "utils toProduct method" should "ok on case class" in {
    val obj = Tuple1("hello").toProduct[Test]
    println(obj)
    assert(obj != null)

  }
}
