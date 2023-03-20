package bitlap.rhs.plugin.server

import com.typesafe.config.ConfigFactory

import java.sql.{ Connection, DriverManager }

/** @author
 *    梦境迷离
 *  @version 1.0,2023/3/20
 */
object ConfigUtils {

  private lazy val config = ConfigFactory.load()

  lazy val tableName = config.getString("rhs-mapping.tableName")

  lazy val nameColumn = config.getString("rhs-mapping.nameColumn")

  lazy val idColumn = config.getString("rhs-mapping.idColumn")

  lazy val port = config.getInt("rhs-mapping.port")

  lazy val url = config.getString("rhs-mapping.url")

  lazy val conn: Connection = DriverManager.getConnection(url)
}
