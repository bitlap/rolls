---
title: CSV
custom_edit_url: https://github.com/bitlap/rolls/edit/master/docs/csv.md
---

## Installation using SBT (Recommended)

This simple csv tool is specifically to handle one-dimensional text, but allows for custom parsing of column values for complex structures within it.

If you are building with sbt, add the following to your `build.sbt`:

```scala
libraryDependencies ++= Seq(
  "org.bitlap" %% "rolls-csv" % "<version>"
)
```

Add import:
```scala mdoc
import bitlap.rolls.csv.*

// example classes
final case class Dimension(key: String, value: String)
final case class Metric(time: Long, entity: Int, dimensions: List[Dimension], metricName: String, metricValue: Int)
```

## Read from CSV file

```scala
import bitlap.rolls.csv.CSVUtils.FileName

val file = ClassLoader.getSystemResource("simple_data.csv").getFile
val (metadata, metrics) = CSVUtils.readCSV(FileName(file)) { line =>
  line
    .into[Metric]
    .withFieldComputed(_.dimensions, dims => StringUtils.asClasses(dims)((k, v) => Dimension(k, v)))
    .decode
}
```

## Basics Decoder

```scala mdoc
final case class SimpleClass(field1: Int, field2: String, field3: Double, field4Opt: Option[String])

val obj = "hello world,2,0.4,"
  .into[SimpleClass]
  .withFieldComputed(_.field1, _ => 1)
  .decode
```

## Write to CSV file

```scala mdoc
import java.io.File
import bitlap.rolls.csv.CSVUtils.*

object Metric:
  lazy val `simple_data_objs` = List(
      Metric(100, 1, List(Dimension("city", "北京"), Dimension("os", "Mac")), "vv", 1),
      Metric(100, 1, List(Dimension("city", "北京"), Dimension("os", "Mac")), "pv", 2),
    )
end Metric

val fileName = FileName("./simple_data.csv")
val status = CSVUtils.writeCSV(fileName, Metric.`simple_data_objs`) { m =>
  m.into
    .withFieldComputed(_.dimensions, dims => StringUtils.asString(dims.map(f => f.key -> f.value).toList))
    .encode
}
```

## Basics Encoder

```scala mdoc
val simpleClass = SimpleClass(field1 = 1, field2 = "2", field3 = 0.4, None)
val csv: String = simpleClass.into
  .withFieldComputed(_.field1, _ => "hello world")
  .encode
```

## Configuration

```scala mdoc
given CSVFormat = DefaultCSVFormat
```