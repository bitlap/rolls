package bitlap.rhs.example

import bitlap.rhs.annotations.*

import java.time.Instant
import scala.concurrent.Future

@Doc
class Test {

  def testMethod(
    listField: List[AuthPermissionPO],
    stringField: String,
    NestedField: SubAuthPermissionPO,
    eitherField: Either[String, AuthPermissionPO]
  ): Seq[AuthPermissionPO] = ???

}

final case class AuthPermissionPO(
  id: String,
  tenantId: Long,
  resourceActions: List[String],
  deleted: Int = 99,
  updateTime: Instant = Instant.now(),
  subPermissions: List[SubAuthPermissionPO],
  subPermission: SubAuthPermissionPO
)

final case class SubAuthPermissionPO(
  tenantId: String,
  subsub: SubSubAuthPermissionPO
)

final case class SubSubAuthPermissionPO(
  id: String,
  tenantId: Short
)
