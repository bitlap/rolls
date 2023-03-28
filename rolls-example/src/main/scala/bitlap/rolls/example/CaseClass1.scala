package bitlap.rolls.example

import bitlap.rolls.annotations.prettyToString

import java.time.Instant

final case class CaseClass1 @prettyToString() (
  id: String,
  tenantId: Map[String, String],
  resourceActions: List[String],
  deleted: Int = 99,
  updateTime: Instant = Instant.now(),
  subPermissions: List[String]
)
