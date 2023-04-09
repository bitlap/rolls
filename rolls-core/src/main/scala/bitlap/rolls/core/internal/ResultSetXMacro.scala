package bitlap.rolls.core.internal

import bitlap.rolls.core.jdbc.{ ResultSetX, TypeRow }

import java.sql.ResultSet
import scala.quoted.*
import scala.compiletime.*
import scala.deriving.Mirror

/** @author
 *    梦境迷离
 *  @version 1.0,2023/4/8
 */
object ResultSetXMacro {

  def resultSetXImpl[T <: TypeRow: Type](resultSet: Expr[ResultSet])(using quotes: Quotes): Expr[ResultSetX[T]] =
    import quotes.reflect.*
    def error = report.errorAndAbort(
      s"Cannot derive ResultSetX for ${TypeRepr.of[T].show}. Only case classes are supported."
    )
    Expr.summon[Mirror.ProductOf[T]].getOrElse(error) match
      case '{ $m: Mirror.ProductOf[T] { type MirroredElemTypes = types } } =>
        '{
          new ResultSetX[T]:
            override def fetch(typeMapping: (ResultSet, Int) => TypeRow): Seq[T] =
              val columnSize = $resultSet.getMetaData.getColumnCount
              val result     = _root_.scala.collection.mutable.ListBuffer[TypeRow]()
              while ($resultSet.next()) {
                val values = typeMapping($resultSet, columnSize)
                result += values
              }
              result.result().asInstanceOf[Seq[T]]
        }
}
