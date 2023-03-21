package bitlap.rhs.plugin.server

import com.sun.net.httpserver.*

/** @author
 *    梦境迷离
 *  @version 1.0,2023/3/20
 */
final class RhsResolveHandler extends HttpHandler {

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

      val value = kvs.getOrElse("value", "")
      val table = kvs.get("tableName").fold(ConfigUtils.tableName)(t => if (t.isEmpty) ConfigUtils.tableName else t)
      val columns =
        kvs.get("nameColumns").fold(ConfigUtils.nameColumns)(t => if (t.isEmpty) ConfigUtils.nameColumns else t)
      val id = kvs.get("idColumn").fold(ConfigUtils.idColumn)(t => if (t.isEmpty) ConfigUtils.idColumn else t)

      val condAnd = columns
        .split('.')
        .zip(value.split('.'))
        .toMap
        .map(kv => s"${kv._1}='${kv._2}'")
        .mkString(" and ")

      println(s"items:${items.mkString(",")}, condAnd:$condAnd")

      val rs = ConfigUtils.conn.createStatement().executeQuery(s"select $id from $table where $condAnd")

      val response = (if (rs.next()) {
                        rs.getString(id)
                      } else Utils.Empty).getBytes()

      exchange.sendResponseHeaders(Utils.OK, response.length)
      val os = exchange.getResponseBody
      os.write(response)
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
