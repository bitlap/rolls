package bitlap.rolls.csv.internal

import scala.compiletime.*
import scala.deriving.Mirror
import scala.quoted.*

/** @author
 *    梦境迷离
 *  @version 1.0,2023/4/5
 */
inline def showType[T] = ${ showTypeImpl[T] }
def showTypeImpl[T: Type](using Quotes): Expr[String] = {
  import quotes.reflect.*
  Expr(Type.show[T])
}

opaque type FieldName <: String = String

object FieldName:

  def apply(value: String): FieldName                    = value
  def wrapAll[A](map: Map[String, A]): Map[FieldName, A] = map

end FieldName

trait Field[Label <: String, Type]

object Field:

  type FromLabelsAndTypes[Labels <: Tuple, Types <: Tuple] <: Tuple =
    (Labels, Types) match {
      case (EmptyTuple, EmptyTuple) => EmptyTuple
      case (labelHead *: labelTail, typeHead *: typeTail) =>
        Field[labelHead, typeHead] *: FromLabelsAndTypes[labelTail, typeTail]
    }

  type DropByLabel[Label, Fields <: Tuple] <: Tuple =
    Fields match {
      case EmptyTuple                => EmptyTuple
      case Field[Label, tpe] *: tail => tail
      case head *: tail              => head *: DropByLabel[Label, tail]
    }

  type TypeForLabel[Label, Fields <: Tuple] =
    Fields match {
      case EmptyTuple                => Nothing
      case Field[Label, tpe] *: tail => tpe
      case head *: tail              => TypeForLabel[Label, tail]
    }

end Field
