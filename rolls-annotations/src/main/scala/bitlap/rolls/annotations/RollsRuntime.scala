package bitlap.rolls.annotations

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.github.pjfanning.`enum`.EnumModule

object RollsRuntime {

  private final val mapper = JsonMapper
    .builder()
    .addModule(DefaultScalaModule)
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
