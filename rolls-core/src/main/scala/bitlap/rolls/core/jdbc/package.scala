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

type TypeRow = Tuple

type TypeRow1[T1] = Tuple1[T1]

type TypeRow2[T1, T2] = (T1, T2)

type TypeRow3[T1, T2, T3] = (T1, T2, T3)

type TypeRow4[T1, T2, T3, T4] = (T1, T2, T3, T4)

type TypeRow5[T1, T2, T3, T4, T5] = (T1, T2, T3, T4, T5)

type TypeRow6[T1, T2, T3, T4, T5, T6] = (T1, T2, T3, T4, T5, T6)

type TypeRow7[T1, T2, T3, T4, T5, T6, T7] = (T1, T2, T3, T4, T5, T6, T7)

type TypeRow8[T1, T2, T3, T4, T5, T6, T7, T8] = (T1, T2, T3, T4, T5, T6, T7, T8)

type TypeRow9[T1, T2, T3, T4, T5, T6, T7, T8, T9] = (T1, T2, T3, T4, T5, T6, T7, T8, T9)

type TypeRow10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10)

type TypeRow11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] = (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11)

type TypeRow12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12] = (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12)

type TypeRow13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13] =
  (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13)

type TypeRow14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14] =
  (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14)

type TypeRow15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15] =
  (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15)

type TypeRow16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16] =
  (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16)

type TypeRow17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17] =
  (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17)

type TypeRow18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18] =
  (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18)

type TypeRow19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19] =
  (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19)

type TypeRow20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20] =
  (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20)

type TypeRow21[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21] =
  (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21)

type TypeRow22[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22] =
  (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22)

extension (typeRow: TypeRow)

  def flatProduct(t: Product): Iterator[Any] = t.productIterator.flatMap {
    case p: Product => flatProduct(p)
    case x          => Iterator(x)
  }

  def lazyColumns: LazyList[Any] = LazyList.from(flatProduct(typeRow))

  def columns[T]: T = {
    // NOTE: typeRow will always be a Tuple2
    val list = flatProduct(typeRow).toList
    // convert Tuple2 to real TupleX
    val realTuple = Tuple.fromArray(list.toArray[Any])
    realTuple.asInstanceOf[T]
  }

end extension

extension (sqlStatement: StringContext)

  def sqlQ(args: Any*)(using Connection): FetchInput = {
    val conn = summon[Connection]
    if (conn.isClosed) throw new Exception("Cannot use `sqlQ` after Connection has been closed")
    val stmt = conn.createStatement()
    stmt -> stmt.executeQuery(sqlStatement.s(args: _*))
  }

  def sql(args: Any*)(using Connection): FetchInput = {
    val conn = summon[Connection]
    if (conn.isClosed) throw new Exception("Cannot use `sql` after Connection has been closed")
    val stmt = conn.createStatement()
    stmt.execute(sqlStatement.s(args: _*))
    stmt -> stmt.getResultSet
  }
end extension
