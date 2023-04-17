package bitlap.rolls.core

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.github.pjfanning.`enum`.EnumModule

/** @author
 *    梦境迷离
 *  @version 1.0,2023/4/8
 */
object RollsRuntime {

  final lazy val mapper = JsonMapper
    .builder()
    .addModule(DefaultScalaModule)
    .serializationInclusion(JsonInclude.Include.NON_EMPTY)
    .addModule(EnumModule)
    .addModule(new JavaTimeModule)
    .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
    .build()

  def toString_(standard: Boolean, label: String, p: Any): String =
    if (standard) {
      p match
        case _: List[?] =>
          val map = p.asInstanceOf[List[(String, Any)]].toMap
          map.map(f => s"${f._1}=${f._2}").mkString(s"$label(", ",", ")")
        case p: Product =>
          (p.productElementNames zip p.productIterator)
            .map(e => s"${e._1}=${e._2}")
            .mkString(p.productPrefix + "(", ",", ")")
    } else {
      p match
        case _: List[?] =>
          val map = p.asInstanceOf[List[(String, Any)]].toMap
          mapper.writeValueAsString(map)
        case _ => mapper.writeValueAsString(p)
    }
}
