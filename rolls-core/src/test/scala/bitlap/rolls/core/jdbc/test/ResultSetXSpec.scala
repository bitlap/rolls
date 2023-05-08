package bitlap.rolls.core.jdbc.test

import java.sql.*

import scala.Tuple.Union

import bitlap.rolls.core.annotations.prettyToString
import bitlap.rolls.core.jdbc.*

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

/** @author
 *    梦境迷离
 *  @version 1.0,2023/4/8
 */
class ResultSetXSpec extends AnyFlatSpec with Matchers {

  Class.forName("org.h2.Driver")

  "ResultSetX" should "ok on class" in {
    given Connection = DriverManager
      .getConnection(
        "jdbc:h2:mem:zim?caseSensitive=false;MODE=MYSQL;TRACE_LEVEL_FILE=2;INIT=RUNSCRIPT FROM 'classpath:test.sql'"
      )
    // default type mapping
    val rows = ResultSetX[TypeRow4[Int, String, String, String]](sqlQ"select * from T_USER").fetch()
    assert(rows.size == 2)
    assert(rows.head.values.size == 4)
  }
}
