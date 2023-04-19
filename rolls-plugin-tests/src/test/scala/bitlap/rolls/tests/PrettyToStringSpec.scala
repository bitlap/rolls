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

  val listSize = 5

  val genTestCaseClassJsonNamedArg = for
    id              <- Gen.uuid.map(_.toString)
    tenantId        <- Gen.mapOf(Gen.uuid.map(_.toString).map(f => f -> f)).map(_.toList.take(listSize).toMap)
    resourceActions <- Gen.listOf(Gen.uuid.map(_.toString)).map(_.take(listSize))
    deleted         <- Gen.long
    subPermissions  <- Gen.listOf(Gen.uuid.map(_.toString)).map(_.take(listSize))
  yield TestCaseClassJsonNamedArg(
    id,
    tenantId,
    resourceActions,
    deleted,
    subPermissions
  )

  val genTestCaseClassJson = for
    id              <- Gen.uuid.map(_.toString)
    tenantId        <- Gen.mapOf(Gen.uuid.map(_.toString).map(f => f -> f)).map(_.toList.take(listSize).toMap)
    resourceActions <- Gen.listOf(Gen.uuid.map(_.toString)).map(_.take(listSize))
    deleted         <- Gen.long
    subPermissions  <- Gen.listOf(Gen.uuid.map(_.toString)).map(_.take(listSize))
  yield TestCaseClassJson(
    id,
    tenantId,
    resourceActions,
    deleted,
    subPermissions
  )

  val genTestCaseClassStandard = for
    id              <- Gen.uuid.map(_.toString)
    tenantId        <- Gen.mapOf(Gen.uuid.map(_.toString).map(f => f -> f)).map(_.toList.take(listSize).toMap)
    resourceActions <- Gen.listOf(Gen.uuid.map(_.toString)).map(_.take(listSize))
    deleted         <- Gen.long
    subPermissions  <- Gen.listOf(Gen.uuid.map(_.toString)).map(_.take(listSize))
  yield TestCaseClassStandard(
    id,
    tenantId,
    resourceActions,
    deleted,
    subPermissions
  )

  val genTestCaseClassStandardNamedArg = for
    id              <- Gen.uuid.map(_.toString)
    tenantId        <- Gen.mapOf(Gen.uuid.map(_.toString).map(f => f -> f)).map(_.toList.take(listSize).toMap)
    resourceActions <- Gen.listOf(Gen.uuid.map(_.toString)).map(_.take(listSize))
    deleted         <- Gen.long
    subPermissions  <- Gen.listOf(Gen.uuid.map(_.toString)).map(_.take(listSize))
  yield TestCaseClassStandardNamedArg(
    id,
    tenantId,
    resourceActions,
    deleted,
    subPermissions
  )

  val genTestClassStandardNamedArg = for
    id              <- Gen.uuid.map(_.toString)
    tenantId        <- Gen.mapOf(Gen.uuid.map(_.toString).map(f => f -> f)).map(_.toList.take(listSize).toMap)
    resourceActions <- Gen.listOf(Gen.uuid.map(_.toString)).map(_.take(listSize))
    deleted         <- Gen.long
    subPermissions  <- Gen.listOf(Gen.uuid.map(_.toString)).map(_.take(listSize))
  yield TestClassStandardNamedArg(
    id,
    tenantId,
    resourceActions,
    deleted,
    subPermissions
  )

  val genTestClassJsonNamedArg = for
    id              <- Gen.uuid.map(_.toString)
    tenantId        <- Gen.mapOf(Gen.uuid.map(_.toString).map(f => f -> f)).map(_.toList.take(listSize).toMap)
    resourceActions <- Gen.listOf(Gen.uuid.map(_.toString)).map(_.take(listSize))
    deleted         <- Gen.long
    subPermissions  <- Gen.listOf(Gen.uuid.map(_.toString)).map(_.take(listSize))
  yield TestClassJsonNamedArg(
    id,
    tenantId,
    resourceActions,
    deleted,
    subPermissions
  )

  val genTestClassJson = for
    id              <- Gen.uuid.map(_.toString)
    tenantId        <- Gen.mapOf(Gen.uuid.map(_.toString).map(f => f -> f)).map(_.toList.take(listSize).toMap)
    resourceActions <- Gen.listOf(Gen.uuid.map(_.toString)).map(_.take(listSize))
    deleted         <- Gen.long
    subPermissions  <- Gen.listOf(Gen.uuid.map(_.toString)).map(_.take(listSize))
  yield TestClassJson(
    id,
    tenantId,
    resourceActions,
    deleted,
    subPermissions
  )

  "prettyToString with stringMask" should "genTestCaseClassJson" in {
    println(genTestCaseClassJson.sample.toString)
    genTestCaseClassJson.sample.map(_.toString.contains(""""tenantId":"***"""")) shouldEqual Option(true)
    genTestCaseClassJson.sample.map(_.toString.contains(""""deleted":"***"""")) shouldEqual Option(true)
    genTestCaseClassJson.sample.map(_.toString.contains(""""subPermissions":"***"""")) shouldEqual Option(true)
  }

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
    genTestClassJson.sample.map(_.toString.contains(""""tenantId":"***"""")) shouldEqual Option(true)
  }

  "prettyToString type args" should "starGraphQLResult" in {
    val starGraphQLResult = StarGraphQLResult(Option("hello"))
    println(starGraphQLResult.toString)
    starGraphQLResult.toString.contains("""{"data":"hello","statusCode":200,"msg":"OK"}""") shouldEqual true
  }
}
