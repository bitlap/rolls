package bitlap.compiler.plugin.example

import bitlap.compiler.plugin.annotations.{ CustomIdNameMapping, IdNameMapping }

object IdNameMappingExample extends App {

  @IdNameMapping(name = "permission") val re = "permission"

  @CustomIdNameMapping(
    name = "permission",
    idColumn = "id",
    nameColumn = "resource",
    tableName = "schema.table"
  ) val re2 =
    "permission"

  println(re)
  println(re2)
}
