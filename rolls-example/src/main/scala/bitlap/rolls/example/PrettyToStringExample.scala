package bitlap.rolls.example

import bitlap.rolls.annotations.prettyToString

import java.time.Instant

/** @author
 *    梦境迷离
 *  @version 1.0,2023/3/28
 */
object PrettyToStringExample extends App {

  @prettyToString
  final case class TestCaseClass1(
    id: String,
    tenantId: Map[String, String],
    private val resourceActions: List[String],
    deleted: Int = 99,
    updateTime: Instant = Instant.now(),
    subPermissions: List[String]
  )

  @prettyToString(true)
  final class TestClass1(
    id: String,
    val tenantId: Map[String, String],
    private val resourceActions: List[String],
    deleted: Int = 99,
    updateTime: Instant = Instant.now(),
    subPermissions: List[String]
  )

  val testCaseClass1 =
    TestCaseClass1("iddd", Map.empty, List.empty, 98, Instant.now(), List.empty)
  println(testCaseClass1)

  val testClass1 =
    new TestClass1("iddd", Map.empty, List.empty, 98, Instant.now(), List.empty)
  println(testClass1.toString)


}
