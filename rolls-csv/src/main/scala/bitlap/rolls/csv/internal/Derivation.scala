package bitlap.rolls.csv.internal

import scala.compiletime.*
import scala.compiletime.ops.int.*
import scala.deriving.Mirror

import bitlap.rolls.csv.*

/** @author
 *    梦境迷离
 *  @version 1.0,2023/4/5
 */
private[csv] object Derivation:

  inline def encodersForAllFields[
      FromFields <: Tuple
  ]: Map[FieldName, Encoder[Any]] =
    inline erasedValue[FromFields] match {
      case _: EmptyTuple =>
        Map.empty
      case _: (Field[label, tpe] *: tail) =>
        encodersForAllFields[tail] + encoderForField[label, FromFields]
    }

  inline def decodersForAllFields[
      FromFields <: Tuple
  ]: Map[FieldName, Decoder[Any]] =
    inline erasedValue[FromFields] match {
      case _: EmptyTuple =>
        Map.empty
      case _: (Field[label, tpe] *: tail) =>
        decodersForAllFields[tail] + decoderForField[label, FromFields]
    }

  private inline def decoderForField[
      FieldLabel <: String,
      FromFields <: Tuple
  ]: (FieldName, Decoder[Any]) =
    inline erasedValue[FromFields] match {
      case _: EmptyTuple =>
        error("Decoder not found for field '" + constValue[FieldLabel] + "' with type " + showType[FromFields])
      case _: (Field[FieldLabel, tpe] *: _) =>
        FieldName(constValue[FieldLabel]) -> summonInline[Decoder[tpe]].asInstanceOf[Decoder[Any]]
      case _: (_ *: tail) =>
        decoderForField[FieldLabel, tail]
    }

  private inline def encoderForField[
      FieldLabel <: String,
      FromFields <: Tuple
  ]: (FieldName, Encoder[Any]) =
    inline erasedValue[FromFields] match {
      case _: EmptyTuple =>
        error("Encoder not found for field '" + constValue[FieldLabel] + "' with type " + showType[FromFields])
      case _: (Field[FieldLabel, tpe] *: _) =>
        FieldName(constValue[FieldLabel]) -> summonInline[Encoder[tpe]].asInstanceOf[Encoder[Any]]
      case _: (_ *: tail) =>
        encoderForField[FieldLabel, tail]
    }

end Derivation
