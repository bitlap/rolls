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

  final case class TestCaseClass2 @prettyToString() (
    id: String,
    tenantId: Map[String, String],
    private val resourceActions: List[String],
    deleted: Int = 99,
    updateTime: Instant = Instant.now(),
    subPermissions: List[String]
  )

  final class TestClass1 @prettyToString() (
    id: String,
    val tenantId: Map[String, String],
    private val resourceActions: List[String],
    deleted: Int = 99,
    updateTime: Instant = Instant.now(),
    subPermissions: List[String]
  )

  @prettyToString
  final class TestClass2(
    id: String,
    val tenantId: Map[String, String],
    private val resourceActions: List[String],
    deleted: Int = 99,
    updateTime: Instant = Instant.now(),
    subPermissions: List[String]
  )

  // {"id":"iddd","tenantId":{},"deleted":98,"updateTime":1680060447.245149000,"subPermissions":[]}
  val testCaseClass1 =
    TestCaseClass1("iddd", Map.empty, List.empty, 98, Instant.now(), List.empty)
  println(testCaseClass1)

  // {"id":"iddd","tenantId":{},"deleted":98,"updateTime":1680068415.311346000,"subPermissions":[]}
  val testCaseClass2 =
    TestCaseClass2("iddd", Map.empty, List.empty, 98, Instant.now(), List.empty)
  println(testCaseClass2.toString)

  // {"tenantId":{}}
  val testClass1 =
    new TestClass1("iddd", Map.empty, List.empty, 98, Instant.now(), List.empty)
  println(testClass1.toString)

  // {"tenantId":{}}
  val testClass2 =
    new TestClass2("iddd", Map.empty, List.empty, 98, Instant.now(), List.empty)
  println(testClass2.toString)

}
