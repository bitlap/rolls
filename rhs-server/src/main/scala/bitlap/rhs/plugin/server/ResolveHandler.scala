package bitlap.rhs.plugin.server

import bitlap.rhs.compiler.plugin.Utils
import com.sun.net.httpserver.*

/** @author
 *    梦境迷离
 *  @version 1.0,2023/3/20
 */
final class ResolveHandler extends HttpHandler {

  def handle(exchange: HttpExchange): Unit =
    try {
      val kvs   = getParamsFromQuery(exchange)
      val value = kvs.getOrElse("value", "")
      val table = kvs.get("tableName").fold(Configs.tableName)(t => if (t.isEmpty) Configs.tableName else t)
      val columns =
        kvs.get("nameColumns").fold(Configs.nameColumns)(t => if (t.isEmpty) Configs.nameColumns else t)
      val id = kvs.get("idColumn").fold(Configs.idColumn)(t => if (t.isEmpty) Configs.idColumn else t)

      val condAnd = columns
        .split('.')
        .zip(value.split('.'))
        .toMap
        .map(kv => s"${kv._1}='${kv._2}'")
        .mkString(" and ")

      println(s"condAnd:$condAnd")

      val rs = Configs.conn.createStatement().executeQuery(s"select $id from $table where $condAnd")

      val response = (if (rs.next()) {
                        rs.getString(id)
                      } else Utils.Empty).getBytes()

      exchange.sendResponseHeaders(Utils.OK, response.length)
      val os = exchange.getResponseBody
      os.write(response)
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
