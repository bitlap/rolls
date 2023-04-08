---
title: CSV
custom_edit_url: https://github.com/bitlap/rolls/edit/master/docs/csv.md
---

Add import `import bitlap.rolls.csv.*`

## Read from CSV file

``` scala
val file = ClassLoader.getSystemResource("simple_data.csv").getFile
val metrics: List[Metric] = CSVUtils.readCSV(file) { line =>
  line
    .into[Metric]
    .withFieldComputed(_.dimensions, dims => StringUtils.extractJsonValues(dims)((k, v) => Dimension(k, v)))
    .decode
```

## Basics Decoder

``` scala
val obj = "hello world,2,0.4,"
  .into[SimpleClass]
  .withFieldComputed(_.field1, _ => 1)
  .decode
assertEquals(obj, SimpleClass(field1 = 1, field2 = "2", field3 = 0.4, None))
```

## Write to CSV file

``` scala
if (file.exists()) file.delete() else file.createNewFile()
val status: Boolean = CSVUtils.writeCSV(file, Metric.`simple_data_objs`) { m =>
  m.into
    .withFieldComputed(_.dimensions, dims => StringUtils.asJsonString(dims.map(f => f.key -> f.value).toList))
    .encode
}
```

## Basics Encoder

``` scala
val simpleClass = SimpleClass(field1 = 1, field2 = "2", field3 = 0.4, None)
val csv: String = simpleClass.into
  .withFieldComputed(_.field1, _ => "hello world")
  .encode
assertEquals(csv, "hello world,2,0.4,")
```

## Configuration

``` scala
given CsvFormat = DefaultCsvFormat
```