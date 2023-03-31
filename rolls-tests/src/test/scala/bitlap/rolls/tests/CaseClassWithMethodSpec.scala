package bitlap.rolls.tests

import bitlap.rolls.core.annotations.prettyToString
import org.scalacheck.Gen
import org.scalacheck.Gen.*
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import java.io.*
import java.nio.file.*
import java.time.Instant

/** @author
 *    梦境迷离
 *  @version 1.0,2023/3/30
 */
class CaseClassWithMethodSpec extends AnyFlatSpec with Matchers {

  final case class CaseClassWithMethod(a: List[String], b: String, c: Boolean)

  "CaseClassWithMethodPhase" should "ok" in {
    val obj = CaseClassWithMethod(List.empty, "b", false)
  }
}
