package bitlap.compiler.plugin.server

import com.sun.net.httpserver.*

/** @author
 *    梦境迷离
 *  @version 1.0,2023/3/20
 */
final class IdMappingHandler extends HttpHandler {

  def handle(exchange: HttpExchange): Unit =
    try {
      val items = exchange.getRequestURI.getQuery.split("&")
      val kvs = items.map { kv =>
        kv.split("=")
      }.map(a => if (a.length == 2) Option(a(0) -> a(1)) else None)
        .collect { case Some(value) =>
          value
        }
        .toMap

      val value  = kvs.getOrElse("value", "")
      val table  = kvs.get("tableName").fold(ConfigUtils.tableName)(t => if (t.isEmpty) ConfigUtils.tableName else t)
      val column = kvs.get("nameColumn").fold(ConfigUtils.nameColumn)(t => if (t.isEmpty) ConfigUtils.nameColumn else t)
      val id     = kvs.get("idColumn").fold(ConfigUtils.idColumn)(t => if (t.isEmpty) ConfigUtils.idColumn else t)

      val rs = ConfigUtils.conn
        .createStatement()
        .executeQuery(
          s"select $id from $table where $column=$value"
        )
      println(s"Query id by sql:$rs")
      val response = rs.getString("id")
      exchange.sendResponseHeaders(200, 0)
      val os = exchange.getResponseBody
      os.write(response.getBytes())
      os.close()
    } catch {
      case e: Exception =>
        exchange.sendResponseHeaders(200, 0)
        val os = exchange.getResponseBody
        os.write("233".getBytes())
        os.close()
    }
}
