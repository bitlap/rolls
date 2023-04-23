---
title: ResultSetX
custom_edit_url: https://github.com/bitlap/rolls/edit/master/docs/resultset_x.md
---

## Installation using SBT (Recommended)

If you are building with sbt, add the following to your `build.sbt`:

```scala
libraryDependencies ++= Seq(
  "org.bitlap" %% "rolls-core" % "<version>"
)
```

## `java.sql.ResultSet` Data Extractor

If you want to quickly obtain all the data for the `ResultSet` without relying on the orm framework. For example, during testing.

```scala
import bitlap.rolls.core.annotations.*
import bitlap.rolls.core.jdbc.*
import java.sql.*

val statement = DriverManager
  .getConnection(
    "jdbc:h2:mem:zim?caseSensitive=false;MODE=MYSQL;TRACE_LEVEL_FILE=2;INIT=RUNSCRIPT FROM 'classpath:test.sql'"
  )
// rows is a Scala Tuple
val rows = ResultSetX[TypeRow4[Int, String, String, String]](sqlQ"select * from T_USER").fetch()
assert(rows.size == 2)
// Scala3 Tuple to List
assert(rows.head.values.size == 4)
```