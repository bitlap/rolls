package bitlap.rolls.compiler.plugin

/** @author
 *    梦境迷离
 *  @version 1.0,2023/3/21
 */
sealed trait Schema

final case class ClassSchema(
  className: String,
  methods: List[MethodSchema]
) extends Schema

final case class MethodSchema(
  methodName: String,
  params: List[TypeSchema],
  resultType: TypeSchema
) extends Schema

final case class TypeSchema(
  typeName: String,
  fields: List[TypeSchema] = List.empty,
  fieldName: Option[String] = None,
  genericType: Option[List[TypeSchema]] = None
) extends Schema
