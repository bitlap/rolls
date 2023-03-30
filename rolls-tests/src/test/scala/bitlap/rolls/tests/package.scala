package bitlap.rolls.tests

import bitlap.rolls.annotations.prettyToString

/** @author
 *    梦境迷离
 *  @version 1.0,2023/3/30
 */
@prettyToString
final case class TestCaseClassJson(
  id: String,
  tenantId: Map[String, String],
  private val resourceActions: List[String],
  deleted: Long,
  subPermissions: List[String]
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
  val tenantId: Map[String, String],
  private val resourceActions: List[String],
  deleted: Long,
  subPermissions: List[String]
)
