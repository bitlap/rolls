package bitlap.rolls.csv.test

import bitlap.rolls.csv.*
import bitlap.rolls.csv.CSVUtils.{ CSVData, FileName }
import bitlap.rolls.csv.test.model.*
import munit.FunSuite

import java.io.File

/** @author
 *    梦境迷离
 *  @version 1.0,2023/4/6
 */
class CSVUtilsSuite extends FunSuite {

  given CsvFormat = DefaultCsvFormat

  test("CSVUtils#readCSVWithMetadata ok") {
    val file = this.getClass.getClassLoader.getResource("simple_data.csv").getFile
    val (metadata: CSVMetadata, metrics: LazyList[Metric]) = CSVUtils.readCSVWithMetadata[Metric](FileName(file)) {
      line =>
        line
          .into[Metric]
          .withFieldComputed(_.dimensions, dims => StringUtils.extractJsonValues(dims)((k, v) => Dimension(k, v)))
          .decode
    }
    assertEquals(metrics.toList, Metric.`simple_data_objs`)
    assertEquals(metadata.classFieldNames, List("time", "entity", "dimensions", "metricName", "metricValue"))
    assertEquals(metadata.rowsNum.apply(), 16L)
  }

  test("CSVUtils#readCSV ok") {
    val file = this.getClass.getClassLoader.getResource("simple_data.csv").getFile
    val metrics: LazyList[Metric] = CSVUtils.readCSV(FileName(file)) { line =>
      line
        .into[Metric]
        .withFieldComputed(_.dimensions, dims => StringUtils.extractJsonValues(dims)((k, v) => Dimension(k, v)))
        .decode
    }
    assertEquals(metrics.toList, Metric.`simple_data_objs`)
  }

  test("CSVUtils#writeCSV ok") {
    val storeFile = new File("./simple_data.csv")
    if (storeFile.exists()) storeFile.delete() else storeFile.createNewFile()
    val status: Boolean = CSVUtils.writeCSV(storeFile, Metric.`simple_data_objs`) { m =>
      m.into
        .withFieldComputed(_.dimensions, dims => StringUtils.asJsonString(dims.map(f => f.key -> f.value).toList))
        .encode
    }
    storeFile.delete()
    assertEquals(status, true)
  }
}
