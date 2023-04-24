package bitlap.rolls.csv.test

import bitlap.rolls.csv.*
import bitlap.rolls.csv.CSVUtils.*
import bitlap.rolls.csv.test.model.*
import munit.FunSuite

import java.io.File
import java.nio.file.Files
import scala.io.{ BufferedSource, Codec, Source }

/** @author
 *    梦境迷离
 *  @version 1.0,2023/4/6
 */
class CSVUtilsSuite extends FunSuite {

  test("CSVUtils#readCSV with headers row") {
    val file = this.getClass.getClassLoader.getResource("header_simple_data.csv").getFile
    given CSVFormat = new CSVFormat {
      override val hasHeaders: Boolean  = true
      override val hasColIndex: Boolean = true
    }
    val (metadata, metrics) = CSVUtils.readCSV(FileName(file)) { line =>
      line
        .into[Metric]
        .withFieldComputed(_.dimensions, dims => StringUtils.asClasses(dims)((k, v) => Dimension(k, v)))
        .decode
    }
    assertEquals(metrics.toList, Metric.`simple_data_objs`)
    assertEquals(metadata.classFieldNames, List("time", "entity", "dimensions", "metricName", "metricValue"))
    assertEquals(metadata.rowsNum.apply(), 16L)
    assertEquals(metadata.rawHeaders, List("time", "entity", "dimensions", "metric_name", "metric_value"))
  }

  test("CSVUtils#readCSV ok") {
    given CSVFormat = DefaultCSVFormat
    val file        = this.getClass.getClassLoader.getResource("simple_data.csv").getFile
    val (metadata, metrics) = CSVUtils.readCSV(FileName(file)) { line =>
      line
        .into[Metric]
        .withFieldComputed(_.dimensions, dims => StringUtils.asClasses(dims)((k, v) => Dimension(k, v)))
        .decode
    }
    assertEquals(metrics.toList, Metric.`simple_data_objs`)
    assertEquals(metadata.classFieldNames, List("time", "entity", "dimensions", "metricName", "metricValue"))
    assertEquals(metadata.rowsNum.apply(), 16L)
    assertEquals(metadata.rawHeaders, List())
  }

  test("CSVUtils#writeCSV ok") {
    given CSVFormat = DefaultCSVFormat
    val fileName    = FileName("./simple_data.csv")
    val status = CSVUtils.writeCSV(fileName, Metric.`simple_data_objs`) { m =>
      m.into
        .withFieldComputed(_.dimensions, dims => StringUtils.asString(dims.map(f => f.key -> f.value).toList))
        .encode
    }
    Files.delete(new File("./simple_data.csv").toPath)
    assertEquals(status, true)
  }

  test("CSVUtils#writeCSV with headers row") {
    given CSVFormat = new CSVFormat {
      override val hasHeaders: Boolean  = true
      override val hasColIndex: Boolean = true
    }

    val fileName = FileName("./header_simple_data.csv")
    val status = CSVUtils.writeCSV(fileName, Metric.`simple_data_objs`) { m =>
      m.into
        .withFieldComputed(_.dimensions, dims => StringUtils.asString(dims.map(f => f.key -> f.value).toList))
        .encode
    }
    Files.delete(new File("./header_simple_data.csv").toPath)
    assertEquals(status, true)
  }

  test("CSVUtils#readCSV multiple json columns") {
    given CSVFormat = new CSVFormat {
      override val hasHeaders: Boolean = true
    }

    val file = this.getClass.getClassLoader.getResource("multiple_json_columns_data.csv").getFile
    val (metadata, metrics) = CSVUtils.readCSV(FileName(file)) { line =>
      line
        .into[MultipleFieldsMetric]
        .withFieldComputed(_.dimensions, dims => StringUtils.asClasses(dims)((k, v) => Dimension(k, v)))
        .withFieldComputed(_.attributes, atts => StringUtils.asClasses(atts)((k, v) => Dimension(k, v)))
        .decode
    }
    assertEquals(metrics.toList, Metric.`multiple_field_data_objs`)
    assertEquals(
      metadata.classFieldNames,
      List("time", "entity", "dimensions", "metricName", "metricValue", "attributes")
    )
    assertEquals(metadata.rowsNum.apply(), 16L)
    assertEquals(metadata.rawHeaders, List("time", "entity", "dimensions", "metric_name", "metric_value", "attributes"))
  }

  test("CSVUtils#writeCSV multiple json columns") {
    given CSVFormat = DefaultCSVFormat

    val fileName = FileName("./multiple_json_columns_data.csv")
    val status = CSVUtils.writeCSV(fileName, Metric.`multiple_field_data_objs`) { m =>
      m.into
        .withFieldComputed(_.dimensions, dims => StringUtils.asString(dims.map(f => f.key -> f.value).toList))
        .withFieldComputed(_.attributes, atts => StringUtils.asString(atts.map(f => f.key -> f.value).toList))
        .encode
    }
    Files.delete(new File("./multiple_json_columns_data.csv").toPath)
    assertEquals(status, true)
  }
}
