package bitlap.rolls.core.jdbc

import java.sql.{ Connection, ResultSet, Statement }

/** @author
 *    梦境迷离
 *  @version 1.0,2023/4/8
 */
opaque type TypeMappingArgs = (ResultSet, Int)
def TypeMappingArgs(resultSet: ResultSet, index: Int): TypeMappingArgs = resultSet -> index

extension (typeMappingArgs: TypeMappingArgs)
  def underlyingMappingResultSet: ResultSet = typeMappingArgs._1
  def underlyingMappingIndex: Int           = typeMappingArgs._2
end extension

type FetchInput = (Statement, ResultSet)

type TypeRow                                      = Tuple
type TypeRow1[T1]                                 = Tuple1[T1] *: EmptyTuple
type TypeRow2[T1, T2]                             = (T1, T2)
type TypeRow3[T1, T2, T3]                         = (T1, T2, T3)
type TypeRow4[T1, T2, T3, T4]                     = (T1, T2, T3, T4)
type TypeRow5[T1, T2, T3, T4, T5]                 = (T1, T2, T3, T4, T5)
type TypeRow6[T1, T2, T3, T4, T5, T6]             = (T1, T2, T3, T4, T5, T6)
type TypeRow7[T1, T2, T3, T4, T5, T6, T7]         = (T1, T2, T3, T4, T5, T6, T7)
type TypeRow8[T1, T2, T3, T4, T5, T6, T7, T8]     = (T1, T2, T3, T4, T5, T6, T7, T8)
type TypeRow9[T1, T2, T3, T4, T5, T6, T7, T8, T9] = (T1, T2, T3, T4, T5, T6, T7, T8, T9)

extension (typeRow: TypeRow)

  def flatProduct(t: Product): Iterator[Any] = t.productIterator.flatMap {
    case p: Product => flatProduct(p)
    case x          => Iterator(x)
  }

  def values: List[Any] = flatProduct(typeRow).toList
end extension

extension (sqlStatement: StringContext)

  def sqlQ(args: Any*)(using Connection): FetchInput = {
    val stmt = summon[Connection].createStatement()
    stmt -> stmt.executeQuery(sqlStatement.s(args: _*))
  }

  def sql(args: Any*)(using Connection): FetchInput = {
    val stmt = summon[Connection].createStatement()
    stmt.execute(sqlStatement.s(args: _*))
    stmt -> stmt.getResultSet

  }
end extension
