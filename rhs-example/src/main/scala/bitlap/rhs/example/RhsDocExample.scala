package bitlap.rhs.example

import bitlap.rhs.annotations.*

import java.time.Instant
import scala.concurrent.Future

@Doc
class Test {

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
