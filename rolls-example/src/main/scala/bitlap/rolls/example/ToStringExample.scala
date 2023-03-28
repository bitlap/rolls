package bitlap.rolls.example

import bitlap.rolls.annotations.toString

import java.time.Instant

/** @author
 *    梦境迷离
 *  @version 1.0,2023/3/28
 */
object ToStringExample extends App {

  final case class CaseClass @toString() (
    id: String,
    tenantId: Map[String, String],
    resourceActions: List[String],
    deleted: Int = 99,
    updateTime: Instant = Instant.now(),
    subPermissions: List[String]
  )

  val test1 =
    CaseClass("iddd", Map.empty, List.empty, 98, Instant.now(), List.empty)
  println(test1)

}
