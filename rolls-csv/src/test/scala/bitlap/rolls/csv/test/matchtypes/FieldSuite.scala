package bitlap.rolls.csv.test.matchtypes

import scala.deriving.Mirror

import bitlap.rolls.csv.*
import bitlap.rolls.csv.internal.Field
import bitlap.rolls.csv.test.model.*

import munit.FunSuite

/** @author
 *    梦境迷离
 *  @version 1.0,2023/4/5
 */
class FieldSuite extends FunSuite {

  private val mirror = summon[Mirror.Of[FieldSuiteProduct]]

  test("Field.FromLabelsAndTypes has proper type") {
    type Expected =
      Field["field1", Int] *: Field["field2", String] *: Field["field3", Double] *: EmptyTuple

    type Actual = Field.FromLabelsAndTypes[mirror.MirroredElemLabels, mirror.MirroredElemTypes]

    summon[Actual =:= Expected]
  }

  test("Field.DropByLabel drops an element from a tuple based on its label") {
    type Expected =
      Field["field1", Int] *: Field["field3", Double] *: EmptyTuple

    type Fields = Field.FromLabelsAndTypes[mirror.MirroredElemLabels, mirror.MirroredElemTypes]
    type Actual = Field.DropByLabel["field2", Fields]

    summon[Actual =:= Expected]
  }

  test("Field.TypeForLabel fetches a type based on its label") {
    type Expected = Double

    type Fields = Field.FromLabelsAndTypes[mirror.MirroredElemLabels, mirror.MirroredElemTypes]
    type Actual = Field.TypeForLabel["field3", Fields]

    summon[Actual =:= Expected]
  }

  test("Field.TypeForLabel returns Nothing if a type with a given label is not found") {
    type Expected = Nothing

    type Fields = Field.FromLabelsAndTypes[mirror.MirroredElemLabels, mirror.MirroredElemTypes]
    type Actual = Field.TypeForLabel["not existing field", Fields]

    summon[Actual =:= Expected]
  }
}
