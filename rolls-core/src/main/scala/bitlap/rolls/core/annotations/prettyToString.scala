package bitlap.rolls.core.annotations

import scala.annotation.ConstantAnnotation

/** @author
 *    梦境迷离
 *  @param standard
 *    default is false, a standard toString also has fields label example:
 *    {{{
 *      Test(field1=value1,field2=value2)
 *    }}}
 *  @version 1.0,2023/3/28
 */
final case class prettyToString(standard: Boolean = false) extends ConstantAnnotation
