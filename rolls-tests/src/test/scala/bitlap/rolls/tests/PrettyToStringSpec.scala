package bitlap.rolls.tests

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalacheck.Gen
import Gen.*
import bitlap.rolls.core.annotations.prettyToString

import java.time.Instant

/** @author
 *    梦境迷离
 *  @version 1.0,2023/3/30
 */
class PrettyToStringSpec extends AnyFlatSpec with Matchers {

  val genTestCaseClassJsonNamedArg = for
    id              <- Gen.uuid.map(_.toString)
    tenantId        <- Gen.mapOf(Gen.stringOf(Gen.alphaChar).map(f => f -> f))
    resourceActions <- Gen.listOf(Gen.alphaUpperStr)
    deleted         <- Gen.long
    subPermissions  <- Gen.listOf(Gen.alphaUpperStr)
  yield TestCaseClassJsonNamedArg(
    id,
    tenantId,
    resourceActions,
    deleted,
    subPermissions
  )

  val genTestCaseClassJson = for
    id              <- Gen.uuid.map(_.toString)
    tenantId        <- Gen.mapOf(Gen.stringOf(Gen.alphaChar).map(f => f -> f))
    resourceActions <- Gen.listOf(Gen.alphaUpperStr)
    deleted         <- Gen.long
    subPermissions  <- Gen.listOf(Gen.alphaUpperStr)
  yield TestCaseClassJson(
    id,
    tenantId,
    resourceActions,
    deleted,
    subPermissions
  )

  val genTestCaseClassStandard = for
    id              <- Gen.uuid.map(_.toString)
    tenantId        <- Gen.mapOf(Gen.stringOf(Gen.alphaChar).map(f => f -> f))
    resourceActions <- Gen.listOf(Gen.alphaUpperStr)
    deleted         <- Gen.long
    subPermissions  <- Gen.listOf(Gen.alphaUpperStr)
  yield TestCaseClassStandard(
    id,
    tenantId,
    resourceActions,
    deleted,
    subPermissions
  )

  val genTestCaseClassStandardNamedArg = for
    id              <- Gen.uuid.map(_.toString)
    tenantId        <- Gen.mapOf(Gen.stringOf(Gen.alphaChar).map(f => f -> f))
    resourceActions <- Gen.listOf(Gen.alphaUpperStr)
    deleted         <- Gen.long
    subPermissions  <- Gen.listOf(Gen.alphaUpperStr)
  yield TestCaseClassStandardNamedArg(
    id,
    tenantId,
    resourceActions,
    deleted,
    subPermissions
  )

  val genTestClassStandardNamedArg = for
    id              <- Gen.uuid.map(_.toString)
    tenantId        <- Gen.mapOf(Gen.stringOf(Gen.alphaChar).map(f => f -> f))
    resourceActions <- Gen.listOf(Gen.alphaUpperStr)
    deleted         <- Gen.long
    subPermissions  <- Gen.listOf(Gen.alphaUpperStr)
  yield TestClassStandardNamedArg(
    id,
    tenantId,
    resourceActions,
    deleted,
    subPermissions
  )

  val genTestClassJsonNamedArg = for
    id              <- Gen.uuid.map(_.toString)
    tenantId        <- Gen.mapOf(Gen.stringOf(Gen.alphaChar).map(f => f -> f))
    resourceActions <- Gen.listOf(Gen.alphaUpperStr)
    deleted         <- Gen.long
    subPermissions  <- Gen.listOf(Gen.alphaUpperStr)
  yield TestClassJsonNamedArg(
    id,
    tenantId,
    resourceActions,
    deleted,
    subPermissions
  )

  val genTestClassJson = for
    id              <- Gen.uuid.map(_.toString)
    tenantId        <- Gen.mapOf(Gen.stringOf(Gen.alphaChar).map(f => f -> f))
    resourceActions <- Gen.listOf(Gen.alphaUpperStr)
    deleted         <- Gen.long
    subPermissions  <- Gen.listOf(Gen.alphaUpperStr)
  yield TestClassJson(
    id,
    tenantId,
    resourceActions,
    deleted,
    subPermissions
  )

  "prettyToString" should "genTestCaseClassJson" in {
    println(genTestCaseClassJson.sample.toString)
    genTestCaseClassJson.sample.map(_.toString.startsWith("{")) shouldEqual Option(true)
  }

  "prettyToString" should "genTestCaseClassJsonNamedArg" in {
    println(genTestCaseClassJsonNamedArg.sample.toString)
    genTestCaseClassJsonNamedArg.sample.map(_.toString.startsWith("{")) shouldEqual Option(true)
  }

  "prettyToString" should "genTestCaseClassStandardNamedArg" in {
    // error1
    println(genTestCaseClassStandardNamedArg.sample.toString)
    genTestCaseClassStandardNamedArg.sample.map(_.toString.contains("=")) shouldEqual Option(true)
  }

  "prettyToString" should "genTestCaseClassStandard" in {
    // error1
    println(genTestCaseClassStandard.sample.toString)
    genTestCaseClassStandard.sample.map(_.toString.contains("=")) shouldEqual Option(true)
  }

  "prettyToString" should "genTestClassStandardNamedArg" in {
    println(genTestClassStandardNamedArg.sample.toString)
    genTestClassStandardNamedArg.sample.map(_.toString.contains("=")) shouldEqual Option(true)
  }

  "prettyToString" should "genTestClassJsonNamedArg" in {
    println(genTestClassJsonNamedArg.sample.toString)
    genTestClassJsonNamedArg.sample.map(_.toString.startsWith("{")) shouldEqual Option(true)
  }

  "prettyToString" should "genTestClassJson" in {
    println(genTestClassJson.sample.toString)
    genTestClassJson.sample.map(_.toString.startsWith("{")) shouldEqual Option(true)
  }

}
