package bitlap.rolls.annotations

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.scala.DefaultScalaModule

object Utils {

  private final val mapper = JsonMapper
    .builder()
    .addModule(DefaultScalaModule)
    .addModule(new JavaTimeModule)
    .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
    .build()

  def toString_(p: Any): String =
    mapper.writeValueAsString(p)
//    (p.productElementNames zip p.productIterator)
//      .map(e => s"${e._1}=${e._2}")
//      .mkString(p.productPrefix + "(", ",", ")")

}
