package bitlap.compiler.plugin.annotations

import scala.annotation.ConstantAnnotation

/** @author
 *    梦境迷离
 *  @version 1.0,2023/3/20
 */
final case class CustomIdNameMapping(
  name: String,
  idColumn: String,
  nameColumn: String,
  tableName: String
) extends ConstantAnnotation
