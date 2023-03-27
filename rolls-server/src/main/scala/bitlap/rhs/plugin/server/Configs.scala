package bitlap.rhs.plugin.server

import com.typesafe.config.ConfigFactory

import java.sql.{ Connection, DriverManager }

/** @author
 *    梦境迷离
 *  @version 1.0,2023/3/20
 */
object Configs {

  private lazy val config = ConfigFactory.load()

  lazy val tableName = config.getString("rolls-mapping.tableName")

  lazy val nameColumns = config.getString("rolls-mapping.nameColumns")

  lazy val idColumn = config.getString("rolls-mapping.idColumn")

  lazy val port = config.getInt("rolls-mapping.port")

  lazy val url = config.getString("rolls-mapping.url")

  lazy val conn: Connection = DriverManager.getConnection(url)
}
