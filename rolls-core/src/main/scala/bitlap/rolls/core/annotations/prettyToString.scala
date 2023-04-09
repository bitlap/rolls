package bitlap.rolls.core.annotations

import scala.annotation.ConstantAnnotation

/** @author
 *    梦境迷离
 *  @param standard
 *    default is false.
 *
 *  If `standard` is `true`, call `toString` yields `Test(field1=value1,field2=value2)`.
 *
 *  Otherwise, call `toString` yields `{"field1":"value1","field2":"value2"}`.
 *  @version 1.0,2023/3/28
 */
final case class prettyToString(standard: Boolean = false) extends ConstantAnnotation
