# rhs

rhs uses standard compiler plugin to replace ValDel for constant type in compile-time.

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

