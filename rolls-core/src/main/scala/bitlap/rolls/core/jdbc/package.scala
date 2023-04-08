package bitlap.rolls.core.jdbc

import scala.Tuple.Union
import scala.compiletime.*

/** @author
 *    梦境迷离
 *  @version 1.0,2023/4/8
 */
type TypeRow                                            = Tuple
type TypeRow1[T1]                                       = Tuple1[T1] *: EmptyTuple
type TypeRow2[T1, T2]                                   = TypeRow1[T1] *: Tuple1[T2]
type TypeRow3[T1, T2, T3]                               = TypeRow2[T1, T2] *: Tuple1[T3]
type TypeRow4[T1, T2, T3, T4]                           = TypeRow3[T1, T2, T3] *: Tuple1[T4]
type TypeRow5[T1, T2, T3, T4, T5]                       = TypeRow4[T1, T2, T3, T4] *: Tuple1[T5]
type TypeRow6[T1, T2, T3, T4, T5, T6]                   = TypeRow5[T1, T2, T3, T4, T5] *: Tuple1[T6]
type TypeRow7[T1, T2, T3, T4, T5, T6, T7]               = TypeRow6[T1, T2, T3, T4, T5, T6] *: Tuple1[T7]
type TypeRow8[T1, T2, T3, T4, T5, T6, T7, T8]           = TypeRow7[T1, T2, T3, T4, T5, T6, T7] *: Tuple1[T8]
type TypeRow9[T1, T2, T3, T4, T5, T6, T7, T8, T9]       = TypeRow8[T1, T2, T3, T4, T5, T6, T7, T8] *: Tuple1[T9]
type TypeRow10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = TypeRow9[T1, T2, T3, T4, T5, T6, T7, T8, T9] *: Tuple1[T10]
type TypeRow11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] = TypeRow10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] *:
  Tuple1[T11]

type TypeRow12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12] =
  TypeRow11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] *: Tuple1[T12]

type TypeRow13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13] =
  TypeRow12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12] *: Tuple1[T13]

type TypeRow14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14] =
  TypeRow13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13] *: Tuple1[T14]

type TypeRow15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15] =
  TypeRow14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14] *: Tuple1[T15]

type TypeRow16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16] =
  TypeRow15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15] *: Tuple1[T16]

type TypeRow17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17] =
  TypeRow16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16] *: Tuple1[T17]

type TypeRow18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18] =
  TypeRow17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17] *: Tuple1[T18]

type TypeRow19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19] =
  TypeRow18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18] *: Tuple1[T19]

type TypeRow20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20] =
  TypeRow19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19] *: Tuple1[T20]

type TypeRow21[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21] =
  TypeRow20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20] *: Tuple1[T21]

type TypeRow22[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22] =
  TypeRow21[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21] *:
    Tuple1[T22]

extension (typeRow: TypeRow)
  def values: List[Any] =
    typeRow match
      case t: TypeRow2[?, ?] =>
        val tuple2 = t.productIterator.toList
        tuple2.head :: tuple2(1).asInstanceOf[TypeRow].values
      case t: TypeRow1[?] => List(t.productIterator.toList.head)
      case EmptyTuple     => Nil
