# rhs

![CI][Badge-CI] [![Nexus (Snapshots)][Badge-Snapshots]][Link-Snapshots]

[Badge-CI]: https://github.com/bitlap/rhs/actions/workflows/ScalaCI.yml/badge.svg
[Badge-Snapshots]: https://img.shields.io/nexus/s/org.bitlap/rhs-compiler-plugin_3?server=https%3A%2F%2Fs01.oss.sonatype.org
[Link-Snapshots]: https://s01.oss.sonatype.org/content/repositories/snapshots/org/bitlap/rhs-compiler-plugin_3

----

**rhs** uses a standard compiler plugin at compile time to replace the `rhs` of the constant type `ValDef` with specifying a different one.

## 1. add dependency

```scala
    autoCompilerPlugins := true,
    addCompilerPlugin("org.bitlap" %% "rhs-compiler-plugin" % <Version>)
```

## 2. add config

> It means: mapping resource to id by sql: `select id from schema.table where resource = ???"`
```scala
    rhs-mapping {
      url = "jdbc:postgresql://localhost/db"
      tableName = "schema.table"
      nameColumn = "resource"
      port = 18000
      idColumn = "id"
    }
```

## 3. start RhsResolveHttpServer

## 4. examples

```scala
    // If not found, continue using `rhs`, otherwise use mapping by sql.
    @RhsMapping val re = "permission" // ast: mods val name: tpt = rhs

    @CustomRhsMapping(idColumn = "id", nameColumn = "resource", tableName = "schema.table") val re2 =
      "permission"

    println(re)  // permission is `select id from schema.table where resource = permission`
    println(re2) // permission is `select id from schema.table where resource = permission`
```

