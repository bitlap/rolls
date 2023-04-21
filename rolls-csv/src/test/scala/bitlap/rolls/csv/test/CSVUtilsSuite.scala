package bitlap.rolls.csv.test

import bitlap.rolls.csv.*
import bitlap.rolls.csv.CSVUtils.*
import bitlap.rolls.csv.test.model.*
import munit.FunSuite

import java.io.File
import java.nio.file.Files

/** @author
 *    梦境迷离
 *  @version 1.0,2023/4/6
 */
class CSVUtilsSuite extends FunSuite {

  given CSVFormat = DefaultCSVFormat

  test("CSVUtils#readCSV ok") {
    val file = this.getClass.getClassLoader.getResource("simple_data.csv").getFile
    val (metadata, metrics) = CSVUtils.readCSV(FileName(file)) { line =>
      line
        .into[Metric]
        .withFieldComputed(_.dimensions, dims => StringUtils.extractJsonValues(dims)((k, v) => Dimension(k, v)))
        .decode
    }
    assertEquals(metrics.toList, Metric.`simple_data_objs`)
    assertEquals(metadata.classFieldNames, List("time", "entity", "dimensions", "metricName", "metricValue"))
    assertEquals(metadata.rowsNum.apply(), 16L)
    assertEquals(metadata.rawHeaders, List())
  }

  test("CSVUtils#writeCSV ok") {
    val fileName = FileName("./simple_data.csv")
    val status = CSVUtils.writeCSV(fileName, Metric.`simple_data_objs`) { m =>
      m.into
        .withFieldComputed(_.dimensions, dims => StringUtils.asJsonString(dims.map(f => f.key -> f.value).toList))
        .encode
    }
    Files.delete(new File("./simple_data.csv").toPath)
    assertEquals(status, true)
  }
}
