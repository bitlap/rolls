package bitlap.rolls.server

import bitlap.rolls.compiler.plugin.{ ClassSchema, MethodSchema, TypeSchema, Utils }
import com.sun.net.httpserver.*
import io.circe.{ Encoder, Json }
import io.circe.generic.semiauto.deriveEncoder

/** @author
 *    梦境迷离
 *  @version 1.0,2023/3/20
 */
object PostSchemaHandler {

  given ClassSchemaEncoder: Encoder[ClassSchema]   = deriveEncoder[ClassSchema]
  given TypeSchemaEncoder: Encoder[TypeSchema]     = deriveEncoder[TypeSchema]
  given MethodSchemaEncoder: Encoder[MethodSchema] = deriveEncoder[MethodSchema]
  given methodSchemaEncoder[T](using io.circe.Encoder[T]): Encoder[List[T]] = (items: List[T]) =>
    if (items.nonEmpty) Encoder.encodeList.apply(items) else Json.Null
}
final class PostSchemaHandler extends HttpHandler {

  def handle(exchange: HttpExchange): Unit =
    try {
      val schema: ClassSchema = Utils.readObject(exchange.getRequestBody)
      if (schema != null) {
        val json = schemaAsJson(schema)
        println(s"ClassSchema:\n$json")
      } else {
        println(s"No ClassSchema")
      }

      exchange.sendResponseHeaders(Utils.OK, 0)
      val os = exchange.getResponseBody
      os.write(Utils.Empty.getBytes())
      os.flush()
      os.close()
    } catch {
      case e: Exception =>
        e.printStackTrace()
        exchange.sendResponseHeaders(Utils.OK, 0)
        val os = exchange.getResponseBody
        os.write(Utils.Empty.getBytes())
        os.flush()
        os.close()
    }
}
