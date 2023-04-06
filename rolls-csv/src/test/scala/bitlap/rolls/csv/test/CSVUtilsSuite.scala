package bitlap.rolls.csv.test

import bitlap.rolls.csv.{ into, CSVUtils, StringUtils }
import bitlap.rolls.csv.test.model.{ Dimension, Metric }
import munit.FunSuite

/** @author
 *    梦境迷离
 *  @version 1.0,2023/4/6
 */
class CSVUtilsSuite extends FunSuite {

  test("CSVUtils#readCSV ok") {
    val file = ClassLoader.getSystemResource("simple_data.csv").getFile
    val metrics: List[Metric] = CSVUtils.readCSV(file) { line =>
      line
        .into[Metric]
        .withFieldComputed(_.dimensions, dims => StringUtils.extractJsonValues(dims)((k, v) => Dimension(k, v)))
        .decode
    }
    assertEquals(metrics, Metric.`simple_data_objs`)
  }
}
