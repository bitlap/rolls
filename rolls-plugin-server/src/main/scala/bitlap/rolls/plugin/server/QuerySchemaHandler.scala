package bitlap.rolls.plugin.server

import bitlap.rolls.compiler.plugin.*

import com.sun.net.httpserver.*

/** @author
 *    梦境迷离
 *  @version 1.0,2023/3/22
 */
class QuerySchemaHandler extends HttpHandler {

  def handle(exchange: HttpExchange): Unit =
    try {
      val className           = getParamsFromQuery(exchange).getOrElse("className", "")
      val schema: ClassSchema = Utils.readObject(className)
      val res = (if (schema != null) {
                   schemaAsJson(schema)
                 } else {
                   "null"
                 }).getBytes()

      exchange.getResponseHeaders.add(Utils.ContentType, Utils.`application/json`)
      exchange.sendResponseHeaders(Utils.OK, res.length)
      val os = exchange.getResponseBody
      os.write(res)
      os.flush()
      os.close()
    } catch {
      case e: Exception =>
        e.printStackTrace()
        exchange.sendResponseHeaders(Utils.OK, 0)
        val os = exchange.getResponseBody
        os.write(Utils.Empty.getBytes())
        os.close()
    }
}
