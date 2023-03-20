# id-mapping

> compile-time

# 1. dependency

```scala
    autoCompilerPlugins := true,
    addCompilerPlugin("org.bitlap" %% "id-mapping-compiler-plugin" % "0.0.1-SNAPSHOT")
```

# 2. config

> It means: mapping name to id by sql: `select id from schema.table where resource = ???"`
```scala
    id-mapping {
      url = "jdbc:postgresql://localhost/db"
      tableName = "schema.table"
      nameColumn = "resource"
      port = 18000
      idColumn = "id"
    }
```

# 3. start IdMappingHttpServer

# 4. examples

```scala
    // If not found, continue using `rhs`, otherwise use mapping by sql.
    @IdNameMapping(name = "permission") val re = "permission" // ast: mods val name: tpt = rhs

    @CustomIdNameMapping(name = "permission", idColumn = "id", nameColumn = "resource", tableName = "schema.table") val re2 =
      "permission"

    println(re)  // permission is `select id from schema.table where resource = permission`
    println(re2) // permission is `select id from schema.table where resource = permission`
```

