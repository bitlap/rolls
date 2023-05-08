/*
 * Copyright (c) 2022 bitlap
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package bitlap.rolls.core.jdbc

import java.sql.*

import scala.language.postfixOps

import bitlap.rolls.core.internal.ResultSetXMacro

/** @author
 *    梦境迷离
 *  @version 1.0,2023/4/8
 */
trait ResultSetX[T <: TypeRow] {

  protected def getResultSetTuple(typeMappingArgs: TypeMappingArgs): TypeRow =
    val resultSet = typeMappingArgs.underlyingMappingResultSet
    val size      = typeMappingArgs.underlyingMappingIndex
    getColumnValues(resultSet, 1, size)

  protected def getColumnValues(resultSet: ResultSet, idx: Int, size: Int): TypeRow = {
    if (idx == size + 1) return EmptyTuple
    val metadata = resultSet.getMetaData
    val tpe      = metadata.getColumnType(idx)
    val name     = metadata.getColumnName(idx)
    (tpe match {
      case Types.VARCHAR   => Tuple(resultSet.getString(name))
      case Types.BIGINT    => Tuple(resultSet.getLong(name))
      case Types.TINYINT   => Tuple(resultSet.getByte(name))
      case Types.SMALLINT  => Tuple(resultSet.getShort(name))
      case Types.BOOLEAN   => Tuple(resultSet.getBoolean(name))
      case Types.INTEGER   => Tuple(resultSet.getInt(name))
      case Types.DOUBLE    => Tuple(resultSet.getDouble(name))
      case Types.TIMESTAMP => Tuple(resultSet.getTimestamp(name))
      case Types.TIME      => Tuple(resultSet.getTime(name))
      case Types.FLOAT     => Tuple(resultSet.getFloat(name))
      case Types.DATE      => Tuple(resultSet.getDate(name))
      case _               => Tuple(resultSet.getObject(name))
    }) :* getColumnValues(resultSet, idx + 1, size)
  }

  def fetch(typeMappingFunc: TypeMappingArgs => TypeRow = getResultSetTuple): Seq[T]
}

object ResultSetX {

  inline def apply[T <: TypeRow](fetchInput: FetchInput): ResultSetX[T] = ${
    ResultSetXMacro.resultSetXImpl[T]('fetchInput)
  }
}
