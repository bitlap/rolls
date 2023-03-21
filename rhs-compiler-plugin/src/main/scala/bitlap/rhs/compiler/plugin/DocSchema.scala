package bitlap.rhs.compiler.plugin

/** @author
 *    梦境迷离
 *  @version 1.0,2023/3/21
 */
sealed trait Schema

case class DocSchema(
  className: String,
  methods: List[MethodSchema]
) extends Schema

case class MethodSchema(
  name: String,
  params: List[TypeSchema],
  resultType: TypeSchema
) extends Schema

case class TypeSchema(
  typeName: String,
  fields: List[TypeSchema] = List.empty,
  name: Option[String] = None,
  generic: Option[List[TypeSchema]] = None
) extends Schema
