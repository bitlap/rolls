package bitlap.rhs.example

import bitlap.rhs.annotations.{ CustomRhsMapping, RhsMapping }

import scala.concurrent.Future

/** Must start HttpServer.scala
 */
object MappingExample extends App:

  // If not found, continue using `rhs`, otherwise use mapping by sql.
  @RhsMapping val re1 = "menu.operate" // ast: mods val name: tpt = rhs

  @CustomRhsMapping(idColumn = "id", nameColumns = "resource.action", tableName = "schema.table") val re2 =
    "menu.operate"

  println(re1) // `select id from schema.table where resource = 'menu' and action = 'operate'`
  println(re2) // `select id from schema.table where resource = 'menu' and action = 'operate'`

end MappingExample
