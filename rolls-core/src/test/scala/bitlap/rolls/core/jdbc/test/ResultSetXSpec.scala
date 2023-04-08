package bitlap.rolls.core.jdbc.test

import bitlap.rolls.core.annotations.prettyToString
import bitlap.rolls.core.jdbc.*
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import java.sql.*
import scala.Tuple.Union

/** @author
 *    梦境迷离
 *  @version 1.0,2023/4/8
 */
class ResultSetXSpec extends AnyFlatSpec with Matchers {

  Class.forName("org.h2.Driver")

  "ResultSetX" should "ok on class" in {
    val statement = DriverManager
      .getConnection(
        "jdbc:h2:mem:zim?caseSensitive=false;MODE=MYSQL;TRACE_LEVEL_FILE=2;INIT=RUNSCRIPT FROM 'classpath:test.sql'"
      )
      .createStatement()
    statement.execute(s"""select * from T_USER""".stripMargin)

    val rowSet: ResultSet = statement.getResultSet

    // default type mapping
    val rows = ResultSetX[TypeRow4[Int, String, String, String]](rowSet).fetch()
    assert(rows.size == 2)
    assert(rows.head.values.size == 4)
  }
}
