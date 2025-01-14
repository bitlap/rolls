package bitlap.rolls.zio

import scala.reflect.*

import zio.*

/** @author
 *    梦境迷离
 *  @version 1.0,2023/6/28
 */

trait autoLive:
  self =>

  val len: Int

  type R

  final def checkArgs[O: ClassTag](args: Any*): Option[String] = {
    val argTypeList =
      classTag[O].runtimeClass.getConstructors.filter(_.getParameterCount == len).head.getParameterTypes.toList
    val msg = () => {
      Some(s"""
           |Constructor argument type mismatch
           |Expect: ${argTypeList.map(_.getTypeName).mkString("[", ",", "]")}
           |Actual: ${args.map(_.getClass).toList.map(_.getTypeName).mkString("[", ",", "]")}
           |""".stripMargin)
    }
    if (args.size == argTypeList.size) {
      val eq = argTypeList.zip(args).forall { (c1, c2) =>
        c1.isInstance(c2)
      }
      if (eq) None else msg()
    } else
      msg()
  }

  final def buildInstance[O: ClassTag](args: Any*): O = {
    val check = checkArgs[O](args*)
    check.fold(()) { msg =>
      java.lang.System.err.println(msg)
    }
    classTag[O].runtimeClass.getConstructors.filter(_.getParameterCount == len).head.newInstance(args*).asInstanceOf[O]
  }

  lazy val live: ZLayer[R, Nothing, Any]

end autoLive

object autoLive:

  open class deriveLive0[O: ClassTag: Tag] extends autoLive:
    self =>

    override type R = Any

    override val len: Int = 0

    override lazy val live: ZLayer[R, Nothing, O] =
      ZLayer.fromFunction(() => buildInstance[O]())
  end deriveLive0

  open class deriveLive1[O: ClassTag: Tag, R1: ClassTag: Tag] extends autoLive:

    override type R = R1

    override val len: Int = 1

    override lazy val live: ZLayer[R, Nothing, O] =
      ZLayer.fromFunction((i1: R1) => buildInstance[O](i1))
  end deriveLive1

  open class deriveLive2[O: ClassTag: Tag, R1: ClassTag: Tag, R2: ClassTag: Tag] extends autoLive:

    override type R = R1 & R2
    override val len: Int = 2

    override lazy val live: ZLayer[R, Nothing, O] =
      ZLayer.fromFunction((i1: R1, i2: R2) => buildInstance[O](i1, i2))
  end deriveLive2

  open class deriveLive3[O: ClassTag: Tag, R1: ClassTag: Tag, R2: ClassTag: Tag, R3: ClassTag: Tag] extends autoLive:

    override type R = R1 & R2 & R3
    override val len: Int = 3

    override lazy val live: ZLayer[R, Nothing, O] =
      ZLayer.fromFunction((i1: R1, i2: R2, i3: R3) => buildInstance[O](i1, i2, i3))
  end deriveLive3

  open class deriveLive4[
      O: ClassTag: Tag,
      R1: ClassTag: Tag,
      R2: ClassTag: Tag,
      R3: ClassTag: Tag,
      R4: ClassTag: Tag
  ] extends autoLive:

    override type R = R1 & R2 & R3 & R4
    override val len: Int = 4

    override lazy val live: ZLayer[R, Nothing, O] =
      ZLayer.fromFunction((i1: R1, i2: R2, i3: R3, i4: R4) => buildInstance[O](i1, i2, i3, i4))
  end deriveLive4

  open class deriveLive5[
      O: ClassTag: Tag,
      R1: ClassTag: Tag,
      R2: ClassTag: Tag,
      R3: ClassTag: Tag,
      R4: ClassTag: Tag,
      R5: ClassTag: Tag
  ] extends autoLive:

    override type R = R1 & R2 & R3 & R4 & R5
    override val len: Int = 5

    override lazy val live: ZLayer[R, Nothing, O] =
      ZLayer.fromFunction((i1: R1, i2: R2, i3: R3, i4: R4, i5: R5) => buildInstance[O](i1, i2, i3, i4, i5))
  end deriveLive5

  open class deriveLive6[
      O: ClassTag: Tag,
      R1: ClassTag: Tag,
      R2: ClassTag: Tag,
      R3: ClassTag: Tag,
      R4: ClassTag: Tag,
      R5: ClassTag: Tag,
      R6: ClassTag: Tag
  ] extends autoLive:

    override type R = R1 & R2 & R3 & R4 & R5 & R6
    override val len: Int = 6

    override lazy val live: ZLayer[R, Nothing, O] =
      ZLayer.fromFunction((i1: R1, i2: R2, i3: R3, i4: R4, i5: R5, i6: R6) => buildInstance[O](i1, i2, i3, i4, i5, i6))
  end deriveLive6

  open class deriveLive7[
      O: ClassTag: Tag,
      R1: ClassTag: Tag,
      R2: ClassTag: Tag,
      R3: ClassTag: Tag,
      R4: ClassTag: Tag,
      R5: ClassTag: Tag,
      R6: ClassTag: Tag,
      R7: ClassTag: Tag
  ] extends autoLive:

    override type R = R1 & R2 & R3 & R4 & R5 & R6 & R7
    override val len: Int = 7

    override lazy val live: ZLayer[R, Nothing, O] =
      ZLayer.fromFunction((i1: R1, i2: R2, i3: R3, i4: R4, i5: R5, i6: R6, i7: R7) =>
        buildInstance[O](i1, i2, i3, i4, i5, i6, i7)
      )
  end deriveLive7

  open class deriveLive8[
      O: ClassTag: Tag,
      R1: ClassTag: Tag,
      R2: ClassTag: Tag,
      R3: ClassTag: Tag,
      R4: ClassTag: Tag,
      R5: ClassTag: Tag,
      R6: ClassTag: Tag,
      R7: ClassTag: Tag,
      R8: ClassTag: Tag
  ] extends autoLive:

    override type R = R1 & R2 & R3 & R4 & R5 & R6 & R7 & R8
    override val len: Int = 8

    override lazy val live: ZLayer[R, Nothing, O] =
      ZLayer.fromFunction((i1: R1, i2: R2, i3: R3, i4: R4, i5: R5, i6: R6, i7: R7, i8: R8) =>
        buildInstance[O](i1, i2, i3, i4, i5, i6, i7, i8)
      )
  end deriveLive8

  open class deriveLive9[
      O: ClassTag: Tag,
      R1: ClassTag: Tag,
      R2: ClassTag: Tag,
      R3: ClassTag: Tag,
      R4: ClassTag: Tag,
      R5: ClassTag: Tag,
      R6: ClassTag: Tag,
      R7: ClassTag: Tag,
      R8: ClassTag: Tag,
      R9: ClassTag: Tag
  ] extends autoLive:

    override type R = R1 & R2 & R3 & R4 & R5 & R6 & R7 & R8 & R9
    override val len: Int = 9

    override lazy val live: ZLayer[R, Nothing, O] =
      ZLayer.fromFunction((i1: R1, i2: R2, i3: R3, i4: R4, i5: R5, i6: R6, i7: R7, i8: R8, i9: R9) =>
        buildInstance[O](i1, i2, i3, i4, i5, i6, i7, i8, i9)
      )
  end deriveLive9

  open class deriveLive10[
      O: ClassTag: Tag,
      R1: ClassTag: Tag,
      R2: ClassTag: Tag,
      R3: ClassTag: Tag,
      R4: ClassTag: Tag,
      R5: ClassTag: Tag,
      R6: ClassTag: Tag,
      R7: ClassTag: Tag,
      R8: ClassTag: Tag,
      R9: ClassTag: Tag,
      R10: ClassTag: Tag
  ] extends autoLive:

    override type R = R1 & R2 & R3 & R4 & R5 & R6 & R7 & R8 & R9 & R10
    override val len: Int = 10

    override lazy val live: ZLayer[R, Nothing, O] =
      ZLayer.fromFunction((i1: R1, i2: R2, i3: R3, i4: R4, i5: R5, i6: R6, i7: R7, i8: R8, i9: R9, i10: R10) =>
        buildInstance[O](i1, i2, i3, i4, i5, i6, i7, i8, i9, i10)
      )
  end deriveLive10

  open class deriveLive11[
      O: ClassTag: Tag,
      R1: ClassTag: Tag,
      R2: ClassTag: Tag,
      R3: ClassTag: Tag,
      R4: ClassTag: Tag,
      R5: ClassTag: Tag,
      R6: ClassTag: Tag,
      R7: ClassTag: Tag,
      R8: ClassTag: Tag,
      R9: ClassTag: Tag,
      R10: ClassTag: Tag,
      R11: ClassTag: Tag
  ] extends autoLive:

    override type R = R1 & R2 & R3 & R4 & R5 & R6 & R7 & R8 & R9 & R10 & R11
    override val len: Int = 11

    override lazy val live: ZLayer[R, Nothing, O] =
      ZLayer.fromFunction(
        (i1: R1, i2: R2, i3: R3, i4: R4, i5: R5, i6: R6, i7: R7, i8: R8, i9: R9, i10: R10, i11: R11) =>
          buildInstance[O](i1, i2, i3, i4, i5, i6, i7, i8, i9, i10, i11)
      )
  end deriveLive11

  open class deriveLive12[
      O: ClassTag: Tag,
      R1: ClassTag: Tag,
      R2: ClassTag: Tag,
      R3: ClassTag: Tag,
      R4: ClassTag: Tag,
      R5: ClassTag: Tag,
      R6: ClassTag: Tag,
      R7: ClassTag: Tag,
      R8: ClassTag: Tag,
      R9: ClassTag: Tag,
      R10: ClassTag: Tag,
      R11: ClassTag: Tag,
      R12: ClassTag: Tag
  ] extends autoLive:

    override type R = R1 & R2 & R3 & R4 & R5 & R6 & R7 & R8 & R9 & R10 & R11 & R12
    override val len: Int = 12

    override lazy val live: ZLayer[R, Nothing, O] =
      ZLayer.fromFunction(
        (i1: R1, i2: R2, i3: R3, i4: R4, i5: R5, i6: R6, i7: R7, i8: R8, i9: R9, i10: R10, i11: R11, i12: R12) =>
          buildInstance[O](i1, i2, i3, i4, i5, i6, i7, i8, i9, i10, i11, i12)
      )
  end deriveLive12

  open class deriveLive13[
      O: ClassTag: Tag,
      R1: ClassTag: Tag,
      R2: ClassTag: Tag,
      R3: ClassTag: Tag,
      R4: ClassTag: Tag,
      R5: ClassTag: Tag,
      R6: ClassTag: Tag,
      R7: ClassTag: Tag,
      R8: ClassTag: Tag,
      R9: ClassTag: Tag,
      R10: ClassTag: Tag,
      R11: ClassTag: Tag,
      R12: ClassTag: Tag,
      R13: ClassTag: Tag
  ] extends autoLive:

    override type R = R1 & R2 & R3 & R4 & R5 & R6 & R7 & R8 & R9 & R10 & R11 & R12 & R13
    override val len: Int = 13

    override lazy val live: ZLayer[R, Nothing, O] =
      ZLayer.fromFunction(
        (
            i1: R1,
            i2: R2,
            i3: R3,
            i4: R4,
            i5: R5,
            i6: R6,
            i7: R7,
            i8: R8,
            i9: R9,
            i10: R10,
            i11: R11,
            i12: R12,
            i13: R13
        ) => buildInstance[O](i1, i2, i3, i4, i5, i6, i7, i8, i9, i10, i11, i12, i13)
      )
  end deriveLive13

  open class deriveLive14[
      O: ClassTag: Tag,
      R1: ClassTag: Tag,
      R2: ClassTag: Tag,
      R3: ClassTag: Tag,
      R4: ClassTag: Tag,
      R5: ClassTag: Tag,
      R6: ClassTag: Tag,
      R7: ClassTag: Tag,
      R8: ClassTag: Tag,
      R9: ClassTag: Tag,
      R10: ClassTag: Tag,
      R11: ClassTag: Tag,
      R12: ClassTag: Tag,
      R13: ClassTag: Tag,
      R14: ClassTag: Tag
  ] extends autoLive:

    override type R = R1 & R2 & R3 & R4 & R5 & R6 & R7 & R8 & R9 & R10 & R11 & R12 & R13 & R14
    override val len: Int = 14

    override lazy val live: ZLayer[R, Nothing, O] =
      ZLayer.fromFunction(
        (
            i1: R1,
            i2: R2,
            i3: R3,
            i4: R4,
            i5: R5,
            i6: R6,
            i7: R7,
            i8: R8,
            i9: R9,
            i10: R10,
            i11: R11,
            i12: R12,
            i13: R13,
            i14: R14
        ) => buildInstance[O](i1, i2, i3, i4, i5, i6, i7, i8, i9, i10, i11, i12, i13, i14)
      )
  end deriveLive14

  open class deriveLive15[
      O: ClassTag: Tag,
      R1: ClassTag: Tag,
      R2: ClassTag: Tag,
      R3: ClassTag: Tag,
      R4: ClassTag: Tag,
      R5: ClassTag: Tag,
      R6: ClassTag: Tag,
      R7: ClassTag: Tag,
      R8: ClassTag: Tag,
      R9: ClassTag: Tag,
      R10: ClassTag: Tag,
      R11: ClassTag: Tag,
      R12: ClassTag: Tag,
      R13: ClassTag: Tag,
      R14: ClassTag: Tag,
      R15: ClassTag: Tag
  ] extends autoLive:

    override type R = R1 & R2 & R3 & R4 & R5 & R6 & R7 & R8 & R9 & R10 & R11 & R12 & R13 & R14 & R15
    override val len: Int = 15

    override lazy val live: ZLayer[R, Nothing, O] =
      ZLayer.fromFunction(
        (
            i1: R1,
            i2: R2,
            i3: R3,
            i4: R4,
            i5: R5,
            i6: R6,
            i7: R7,
            i8: R8,
            i9: R9,
            i10: R10,
            i11: R11,
            i12: R12,
            i13: R13,
            i14: R14,
            i15: R15
        ) => buildInstance[O](i1, i2, i3, i4, i5, i6, i7, i8, i9, i10, i11, i12, i13, i14, i15)
      )
  end deriveLive15

  open class deriveLive16[
      O: ClassTag: Tag,
      R1: ClassTag: Tag,
      R2: ClassTag: Tag,
      R3: ClassTag: Tag,
      R4: ClassTag: Tag,
      R5: ClassTag: Tag,
      R6: ClassTag: Tag,
      R7: ClassTag: Tag,
      R8: ClassTag: Tag,
      R9: ClassTag: Tag,
      R10: ClassTag: Tag,
      R11: ClassTag: Tag,
      R12: ClassTag: Tag,
      R13: ClassTag: Tag,
      R14: ClassTag: Tag,
      R15: ClassTag: Tag,
      R16: ClassTag: Tag
  ] extends autoLive:

    override type R = R1 & R2 & R3 & R4 & R5 & R6 & R7 & R8 & R9 & R10 & R11 & R12 & R13 & R14 & R15 & R16
    override val len: Int = 16

    override lazy val live: ZLayer[R, Nothing, O] =
      ZLayer.fromFunction(
        (
            i1: R1,
            i2: R2,
            i3: R3,
            i4: R4,
            i5: R5,
            i6: R6,
            i7: R7,
            i8: R8,
            i9: R9,
            i10: R10,
            i11: R11,
            i12: R12,
            i13: R13,
            i14: R14,
            i15: R15,
            i16: R16
        ) => buildInstance[O](i1, i2, i3, i4, i5, i6, i7, i8, i9, i10, i11, i12, i13, i14, i15, i16)
      )
  end deriveLive16

  open class deriveLive17[
      O: ClassTag: Tag,
      R1: ClassTag: Tag,
      R2: ClassTag: Tag,
      R3: ClassTag: Tag,
      R4: ClassTag: Tag,
      R5: ClassTag: Tag,
      R6: ClassTag: Tag,
      R7: ClassTag: Tag,
      R8: ClassTag: Tag,
      R9: ClassTag: Tag,
      R10: ClassTag: Tag,
      R11: ClassTag: Tag,
      R12: ClassTag: Tag,
      R13: ClassTag: Tag,
      R14: ClassTag: Tag,
      R15: ClassTag: Tag,
      R16: ClassTag: Tag,
      R17: ClassTag: Tag
  ] extends autoLive:

    override type R = R1 & R2 & R3 & R4 & R5 & R6 & R7 & R8 & R9 & R10 & R11 & R12 & R13 & R14 & R15 & R16 & R17
    override val len: Int = 17

    override lazy val live: ZLayer[R, Nothing, O] =
      ZLayer.fromFunction(
        (
            i1: R1,
            i2: R2,
            i3: R3,
            i4: R4,
            i5: R5,
            i6: R6,
            i7: R7,
            i8: R8,
            i9: R9,
            i10: R10,
            i11: R11,
            i12: R12,
            i13: R13,
            i14: R14,
            i15: R15,
            i16: R16,
            i17: R17
        ) => buildInstance[O](i1, i2, i3, i4, i5, i6, i7, i8, i9, i10, i11, i12, i13, i14, i15, i16, i17)
      )
  end deriveLive17

  open class deriveLive18[
      O: ClassTag: Tag,
      R1: ClassTag: Tag,
      R2: ClassTag: Tag,
      R3: ClassTag: Tag,
      R4: ClassTag: Tag,
      R5: ClassTag: Tag,
      R6: ClassTag: Tag,
      R7: ClassTag: Tag,
      R8: ClassTag: Tag,
      R9: ClassTag: Tag,
      R10: ClassTag: Tag,
      R11: ClassTag: Tag,
      R12: ClassTag: Tag,
      R13: ClassTag: Tag,
      R14: ClassTag: Tag,
      R15: ClassTag: Tag,
      R16: ClassTag: Tag,
      R17: ClassTag: Tag,
      R18: ClassTag: Tag
  ] extends autoLive:

    override type R = R1 & R2 & R3 & R4 & R5 & R6 & R7 & R8 & R9 & R10 & R11 & R12 & R13 & R14 & R15 & R16 & R17 & R18
    override val len: Int = 18

    override lazy val live: ZLayer[R, Nothing, O] =
      ZLayer.fromFunction(
        (
            i1: R1,
            i2: R2,
            i3: R3,
            i4: R4,
            i5: R5,
            i6: R6,
            i7: R7,
            i8: R8,
            i9: R9,
            i10: R10,
            i11: R11,
            i12: R12,
            i13: R13,
            i14: R14,
            i15: R15,
            i16: R16,
            i17: R17,
            i18: R18
        ) => buildInstance[O](i1, i2, i3, i4, i5, i6, i7, i8, i9, i10, i11, i12, i13, i14, i15, i16, i17, i18)
      )
  end deriveLive18

  open class deriveLive19[
      O: ClassTag: Tag,
      R1: ClassTag: Tag,
      R2: ClassTag: Tag,
      R3: ClassTag: Tag,
      R4: ClassTag: Tag,
      R5: ClassTag: Tag,
      R6: ClassTag: Tag,
      R7: ClassTag: Tag,
      R8: ClassTag: Tag,
      R9: ClassTag: Tag,
      R10: ClassTag: Tag,
      R11: ClassTag: Tag,
      R12: ClassTag: Tag,
      R13: ClassTag: Tag,
      R14: ClassTag: Tag,
      R15: ClassTag: Tag,
      R16: ClassTag: Tag,
      R17: ClassTag: Tag,
      R18: ClassTag: Tag,
      R19: ClassTag: Tag
  ] extends autoLive:

    override type R = R1 & R2 & R3 & R4 & R5 & R6 & R7 & R8 & R9 & R10 & R11 & R12 & R13 & R14 & R15 & R16 & R17 & R18 &
      R19
    override val len: Int = 19

    override lazy val live: ZLayer[R, Nothing, O] =
      ZLayer.fromFunction(
        (
            i1: R1,
            i2: R2,
            i3: R3,
            i4: R4,
            i5: R5,
            i6: R6,
            i7: R7,
            i8: R8,
            i9: R9,
            i10: R10,
            i11: R11,
            i12: R12,
            i13: R13,
            i14: R14,
            i15: R15,
            i16: R16,
            i17: R17,
            i18: R18,
            i19: R19
        ) => buildInstance[O](i1, i2, i3, i4, i5, i6, i7, i8, i9, i10, i11, i12, i13, i14, i15, i16, i17, i18, i19)
      )
  end deriveLive19

  open class deriveLive20[
      O: ClassTag: Tag,
      R1: ClassTag: Tag,
      R2: ClassTag: Tag,
      R3: ClassTag: Tag,
      R4: ClassTag: Tag,
      R5: ClassTag: Tag,
      R6: ClassTag: Tag,
      R7: ClassTag: Tag,
      R8: ClassTag: Tag,
      R9: ClassTag: Tag,
      R10: ClassTag: Tag,
      R11: ClassTag: Tag,
      R12: ClassTag: Tag,
      R13: ClassTag: Tag,
      R14: ClassTag: Tag,
      R15: ClassTag: Tag,
      R16: ClassTag: Tag,
      R17: ClassTag: Tag,
      R18: ClassTag: Tag,
      R19: ClassTag: Tag,
      R20: ClassTag: Tag
  ] extends autoLive:

    override type R = R1 & R2 & R3 & R4 & R5 & R6 & R7 & R8 & R9 & R10 & R11 & R12 & R13 & R14 & R15 & R16 & R17 & R18 &
      R19 & R20
    override val len: Int = 20

    override lazy val live: ZLayer[R, Nothing, O] =
      ZLayer.fromFunction(
        (
            i1: R1,
            i2: R2,
            i3: R3,
            i4: R4,
            i5: R5,
            i6: R6,
            i7: R7,
            i8: R8,
            i9: R9,
            i10: R10,
            i11: R11,
            i12: R12,
            i13: R13,
            i14: R14,
            i15: R15,
            i16: R16,
            i17: R17,
            i18: R18,
            i19: R19,
            i20: R20
        ) => buildInstance[O](i1, i2, i3, i4, i5, i6, i7, i8, i9, i10, i11, i12, i13, i14, i15, i16, i17, i18, i19, i20)
      )
  end deriveLive20

  open class deriveLive21[
      O: ClassTag: Tag,
      R1: ClassTag: Tag,
      R2: ClassTag: Tag,
      R3: ClassTag: Tag,
      R4: ClassTag: Tag,
      R5: ClassTag: Tag,
      R6: ClassTag: Tag,
      R7: ClassTag: Tag,
      R8: ClassTag: Tag,
      R9: ClassTag: Tag,
      R10: ClassTag: Tag,
      R11: ClassTag: Tag,
      R12: ClassTag: Tag,
      R13: ClassTag: Tag,
      R14: ClassTag: Tag,
      R15: ClassTag: Tag,
      R16: ClassTag: Tag,
      R17: ClassTag: Tag,
      R18: ClassTag: Tag,
      R19: ClassTag: Tag,
      R20: ClassTag: Tag,
      R21: ClassTag: Tag
  ] extends autoLive:

    override type R = R1 & R2 & R3 & R4 & R5 & R6 & R7 & R8 & R9 & R10 & R11 & R12 & R13 & R14 & R15 & R16 & R17 & R18 &
      R19 & R20 & R21
    override val len: Int = 21

    override lazy val live: ZLayer[R, Nothing, O] =
      ZLayer.fromFunction(
        (
            i1: R1,
            i2: R2,
            i3: R3,
            i4: R4,
            i5: R5,
            i6: R6,
            i7: R7,
            i8: R8,
            i9: R9,
            i10: R10,
            i11: R11,
            i12: R12,
            i13: R13,
            i14: R14,
            i15: R15,
            i16: R16,
            i17: R17,
            i18: R18,
            i19: R19,
            i20: R20,
            i21: R21
        ) =>
          buildInstance[O](
            i1,
            i2,
            i3,
            i4,
            i5,
            i6,
            i7,
            i8,
            i9,
            i10,
            i11,
            i12,
            i13,
            i14,
            i15,
            i16,
            i17,
            i18,
            i19,
            i20,
            i21
          )
      )
  end deriveLive21

  open class deriveLive22[
      O: ClassTag: Tag,
      R1: ClassTag: Tag,
      R2: ClassTag: Tag,
      R3: ClassTag: Tag,
      R4: ClassTag: Tag,
      R5: ClassTag: Tag,
      R6: ClassTag: Tag,
      R7: ClassTag: Tag,
      R8: ClassTag: Tag,
      R9: ClassTag: Tag,
      R10: ClassTag: Tag,
      R11: ClassTag: Tag,
      R12: ClassTag: Tag,
      R13: ClassTag: Tag,
      R14: ClassTag: Tag,
      R15: ClassTag: Tag,
      R16: ClassTag: Tag,
      R17: ClassTag: Tag,
      R18: ClassTag: Tag,
      R19: ClassTag: Tag,
      R20: ClassTag: Tag,
      R21: ClassTag: Tag,
      R22: ClassTag: Tag
  ] extends autoLive:

    override type R = R1 & R2 & R3 & R4 & R5 & R6 & R7 & R8 & R9 & R10 & R11 & R12 & R13 & R14 & R15 & R16 & R17 & R18 &
      R19 & R20 & R21 & R22
    override val len: Int = 22

    override lazy val live: ZLayer[R, Nothing, O] =
      ZLayer.fromFunction(
        (
            i1: R1,
            i2: R2,
            i3: R3,
            i4: R4,
            i5: R5,
            i6: R6,
            i7: R7,
            i8: R8,
            i9: R9,
            i10: R10,
            i11: R11,
            i12: R12,
            i13: R13,
            i14: R14,
            i15: R15,
            i16: R16,
            i17: R17,
            i18: R18,
            i19: R19,
            i20: R20,
            i21: R21,
            i22: R22
        ) =>
          buildInstance[O](
            i1,
            i2,
            i3,
            i4,
            i5,
            i6,
            i7,
            i8,
            i9,
            i10,
            i11,
            i12,
            i13,
            i14,
            i15,
            i16,
            i17,
            i18,
            i19,
            i20,
            i21,
            i22
          )
      )
  end deriveLive22

end autoLive
