package bitlap.rolls.example

import bitlap.rolls.annotations.*

import java.time.Instant
import scala.concurrent.Future

/** Start HttpServer.scala to query class schema
 */
final case class SimpleClassTest @classSchema()() {

  def testMethod(
    listField: List[SubSubSubAuthPermissionPO],
    stringField: String,
    optField: Option[SubSubSubAuthPermissionPO],
    NestedObjectField: SubSubSubAuthPermissionPO,
    eitherField: Either[String, SubSubSubAuthPermissionPO]
  ): SubSubSubAuthPermissionPO = ???

}

@classSchema()
final case class CaseClassTest(){

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
  def testMethod6(): Array[AuthPermissionPO]    = ???
  def testMethod7(): Array[String]    = ???

}

@classSchema
class ClassTest {

  def testMethod1(
    listField: List[AuthPermissionPO],
    stringField: String,
    longField: Long,
    NestedObjectField: SubAuthPermissionPO,
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
