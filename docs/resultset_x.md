---
title: ResultSetX
custom_edit_url: https://github.com/bitlap/rolls/edit/master/docs/resultset_x.md
---

## `java.sql.ResultSet` Data Extractor

If you want to quickly obtain all the data for the `ResultSet` without relying on the orm framework. For example, during testing.

``` scala
val rowSet: ResultSet = statement.getResultSet
// ret is a Scala Tuple
val rows = ResultSetX[TypeRow4[Int, String, String, String]](rowSet).fetch()
assert(rows.size == 2)
// Scala3 Tuple to List
assert(rows.head.values.size == 4)
```