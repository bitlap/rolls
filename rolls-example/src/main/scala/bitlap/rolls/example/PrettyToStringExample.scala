package bitlap.rolls.example

import bitlap.rolls.annotations.prettyToString

import java.time.Instant

/** @author
 *    梦境迷离
 *  @version 1.0,2023/3/28
 */
object PrettyToStringExample extends App {

  val test1 =
    CaseClass1("iddd", Map.empty, List.empty, 98, Instant.now(), List.empty)
  println(test1)

}
