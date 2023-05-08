package bitlap.rolls.tests

import java.io.*
import java.nio.file.*
import java.time.Instant

import bitlap.rolls.core.annotations.prettyToString

import org.scalacheck.Gen
import org.scalacheck.Gen.*
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

/** @author
 *    梦境迷离
 *  @version 1.0,2023/3/30
 */
class ClassSchemaSpec extends AnyFlatSpec with Matchers {

  // copy from rolls-server (cannot depends on rolls-server)
  // so cannot cats binary data to ClassSchema class
  private final val folder = "/tmp/.compiler"
  private val fileName     = s"$folder/classSchema_%s.txt"

  def readBinary(className: String): Array[Byte] =
    Files.readAllBytes(Paths.get(fileName.format(className)))

  "classSchema" should "ok on simple class" in {
    val res = readBinary("SimpleClassTest")
    println(res.length)
    res.length shouldEqual 2167
  }

  "classSchema" should "ok on class" in {
    val res = readBinary("ClassTest")
    println(res.length)
    res.length shouldEqual 17546
  }

  "classSchema" should "ok on case class" in {
    val res = readBinary("CaseClassTest")
    println(res.length)
    res.length shouldEqual 20272
  }
}
