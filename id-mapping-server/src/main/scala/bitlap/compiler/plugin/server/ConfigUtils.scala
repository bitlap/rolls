package bitlap.compiler.plugin.server

import com.typesafe.config.ConfigFactory

import java.sql.{ Connection, DriverManager }

/** @author
 *    梦境迷离
 *  @version 1.0,2023/3/20
 */
object ConfigUtils {

  lazy val config = ConfigFactory.load()

  lazy val tableName = config.getString("id-mapping.tableName")

  lazy val nameColumn = config.getString("id-mapping.nameColumn")

  lazy val idColumn = config.getString("id-mapping.idColumn")

  lazy val port = config.getInt("id-mapping.port")

  lazy val url = config.getString("id-mapping.url")

  lazy val conn: Connection =
    DriverManager.getConnection(config.getString("id-mapping.url"))
}
