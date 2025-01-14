package bitlap.rolls.tests

import caliban.schema.Annotations.GQLDescription

/** @author
 *    梦境迷离
 *  @version 1.0,2023/4/12
 */
object ValidatePrefixExample extends App {

  final case class StarDictInput(
      @GQLDescription("dictName")
      dictName: Option[String],
      @GQLDescription("dictCode")
      dictCode: Option[String],
      @GQLDescription("starDictPayload")
      starDictPayload: StarDictPayload,
      @GQLDescription("starDictFunction")
      starDictFunction: String => StarDictPayload
  )

  final case class StarDictPayload(
      @GQLDescription("id")
      id: Option[String],
      @GQLDescription("name")
      name: Option[String]
  )
}
