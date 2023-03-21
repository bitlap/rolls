package bitlap.rhs.example

import bitlap.rhs.annotations.*

import java.time.Instant
import scala.concurrent.Future

@Doc
class Test {

  def testMethod(
    permissions: List[AuthPermissionPO],
    permission2: String,
    permission: AuthPermissionPO,
    either: Either[String, AuthPermissionPO]
  ): Seq[AuthPermissionPO] = ???

}

final case class AuthPermissionPO(
  id: String,
  userId: Long,
  tenantId: Long,
  resourceId: String,
  resource: String,
  resourceActions: List[String],
  deleted: Int = 99,
  createBy: Long,
  createTime: Instant = Instant.now(),
  updateBy: Long,
  updateTime: Instant = Instant.now(),
  subPermissions: List[AuthPermissionPO],
  subPermission: AuthPermissionPO
)
