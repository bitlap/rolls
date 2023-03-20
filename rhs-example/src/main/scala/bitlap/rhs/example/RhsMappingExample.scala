package bitlap.rhs.example

import bitlap.rhs.annotations.{ CustomRhsMapping, RhsMapping }

object RhsMappingExample extends App :

  @RhsMapping val re1 = "permission"

  @CustomRhsMapping(
    idColumn = "id",
    nameColumn = "resource",
    tableName = "schema.table"
  ) val re2 = "permission"

  println(re1)
  println(re2)

