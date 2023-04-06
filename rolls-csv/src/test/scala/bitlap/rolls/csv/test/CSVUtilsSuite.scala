package bitlap.rolls.csv.test

import bitlap.rolls.csv.{ into, CSVUtils, CsvFormat, DefaultCsvFormat, StringUtils }
import bitlap.rolls.csv.test.model.{ Dimension, Metric }
import munit.FunSuite

import java.io.File

/** @author
 *    梦境迷离
 *  @version 1.0,2023/4/6
 */
class CSVUtilsSuite extends FunSuite {

  given CsvFormat = DefaultCsvFormat

  test("CSVUtils#readCSV ok") {
    val file = this.getClass.getClassLoader.getResource("simple_data.csv").getFile
    val metrics: List[Metric] = CSVUtils.readCSV(file) { line =>
      line
        .into[Metric]
        .withFieldComputed(_.dimensions, dims => StringUtils.extractJsonValues(dims)((k, v) => Dimension(k, v)))
        .decode
    }
    assertEquals(metrics, Metric.`simple_data_objs`)
  }

  test("CSVUtils#writeCSV ok") {
    val file = new File("./simple_data.csv")
    if (file.exists()) file.delete() else file.createNewFile()
    val status: Boolean = CSVUtils.writeCSV(
      file,
      Metric.`simple_data_objs`.map { m =>
        m.into
          .withFieldComputed(_.dimensions, dims => StringUtils.asJsonString(dims.map(f => f.key -> f.value).toList))
          .encode
      }
    )
    file.delete()
    assertEquals(status, true)
  }
}
