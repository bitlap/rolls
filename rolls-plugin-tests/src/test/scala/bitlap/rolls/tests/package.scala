package bitlap.rolls.tests

import java.time.Instant

import bitlap.rolls.core.*
import bitlap.rolls.core.annotations.{ classSchema, prettyToString, stringMask }

import caliban.schema.Annotations.GQLDescription

/** @author
 *    梦境迷离
 *  @version 1.0,2023/3/30
 */
@prettyToString
final case class TestCaseClassJson(
    id: String,
    tenantId: Map[String, String] @stringMask,
    private val resourceActions: List[String],
    @stringMask deleted: Long,
    @stringMask subPermissions: List[String]
)

@prettyToString(standard = false)
final case class TestCaseClassJsonNamedArg(
    id: String,
    tenantId: Map[String, String],
    private val resourceActions: List[String],
    deleted: Long,
    subPermissions: List[String]
)

@prettyToString(true)
final case class TestCaseClassStandard(
    id: String,
    tenantId: Map[String, String],
    private val resourceActions: List[String],
    deleted: Long,
    subPermissions: List[String]
)

@prettyToString(standard = true)
final case class TestCaseClassStandardNamedArg(
    id: String,
    tenantId: Map[String, String],
    private val resourceActions: List[String],
    deleted: Long,
    subPermissions: List[String]
)

@prettyToString(standard = true)
final class TestClassStandardNamedArg(
    id: String,
    val tenantId: Map[String, String],
    private val resourceActions: List[String],
    deleted: Long,
    subPermissions: List[String]
)

@prettyToString(standard = false)
final class TestClassJsonNamedArg(
    id: String,
    val tenantId: Map[String, String],
    private val resourceActions: List[String],
    deleted: Long,
    subPermissions: List[String]
)

@prettyToString
final class TestClassJson(
    id: String,
    @stringMask val tenantId: Map[String, String],
    private val resourceActions: List[String],
    deleted: Long,
    subPermissions: List[String]
)

/** Start HttpServer.scala to query class schema
 */
@classSchema
final case class SimpleClassTest() {

  def testMethod(
      listField: List[SubSubSubAuthPermissionPO],
      stringField: String,
      optField: Option[SubSubSubAuthPermissionPO],
      NestedObjectField: SubSubSubAuthPermissionPO,
      eitherField: Either[String, SubSubSubAuthPermissionPO]
  ): SubSubSubAuthPermissionPO = ???

}

@classSchema
final case class CaseClassTest() {

  def testMethod1(
      listField: List[AuthPermissionPO],
      stringField: String,
      longOptField: Option[Long],
      NestedObjectField: SubAuthPermissionPO,
      eitherField: Either[String, AuthPermissionPO],
      simpleEitherField: Either[Throwable, String]
  ): Seq[AuthPermissionPO] = ???

  def testMethod2(): Either[String, AuthPermissionPO] = ???
  def testMethod3(): Either[Throwable, String]        = ???
  def testMethod4(): AuthPermissionPO                 = ???
  def testMethod5(): Map[String, AuthPermissionPO]    = ???
  def testMethod6(): Array[AuthPermissionPO]          = ???
  def testMethod7(): Array[String]                    = ???

}

@classSchema
class ClassTest {

  def testMethod1(
      listField: List[AuthPermissionPO],
      stringField: String,
      longField: Long,
      nestedObjectField: SubAuthPermissionPO,
      eitherField: Either[String, AuthPermissionPO],
      simpleEitherField: Either[Throwable, String]
  ): Seq[AuthPermissionPO] = ???

  def testMethod2(): Either[String, AuthPermissionPO] = ???
  def testMethod3(): Either[Throwable, String]        = ???
  def testMethod4(): AuthPermissionPO                 = ???
  def testMethod5(): Map[String, AuthPermissionPO]    = ???

}

final case class AuthPermissionPO(
    id: String,
    tenantId: Map[String, SubSubSubAuthPermissionPO],
    resourceActions: List[String],
    deleted: Int = 99,
    updateTime: Instant = Instant.now(),
    subPermissions: List[SubAuthPermissionPO],
    subPermission: SubAuthPermissionPO
)

final case class SubAuthPermissionPO(
    subsub: SubSubAuthPermissionPO,
    subsubMap: Map[String, SubSubSubAuthPermissionPO],
    subsubList: List[SubSubSubAuthPermissionPO]
)

final case class SubSubAuthPermissionPO(
    id: String,
    subsubsub: SubSubSubAuthPermissionPO
)

final case class SubSubSubAuthPermissionPO(
    list: List[String]
)

@prettyToString
final case class StarGraphQLResult[T](
    data: Option[T],
    statusCode: Int = 200,
    msg: String = "OK"
)
