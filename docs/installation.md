---
title: Installing Rolls
sidebar_label: Installing
custom_edit_url: https://github.com/bitlap/rolls/edit/master/docs/installation.md
---

## Installation using SBT (Recommended)

If you are building with sbt, add the following to your `build.sbt`:

```scala
autoCompilerPlugins := true
addCompilerPlugin("org.bitlap" %% "rolls-compiler-plugin" % "<version>")
libraryDependencies ++= Seq(
  "org.bitlap" %% "rolls-core" % "<version>", // depends rolls-compiler-plugin
  "org.bitlap" %% "rolls-csv" % "<version>" // if needed
)
```

## Configuration

`rolls-csv` does not have any configuration. This configuration only for annotations in `rolls-core`. 

`rolls-core` needs to work with `rolls-compiler-plugin`.

Add the following to `build.sbt`:
```scala
lazy val config =
  """|classSchema=bitlap.rolls.core.annotations.classSchema
    |prettyToString=bitlap.rolls.core.annotations.prettyToString
    |rhsMapping=bitlap.rolls.core.annotations.rhsMapping
    |customRhsMapping=bitlap.rolls.core.annotations.customRhsMapping
    |classSchemaFolder=/tmp/.compiler
    |classSchemaFileName=classSchema_%s.txt
    |rhsMappingUri=http://localhost:18000/rolls-mapping
    |classSchemaPostUri=http://localhost:18000/rolls-doc
    |postClassSchemaToServer=false
    |classSchemaQueryUri=http://localhost:18000/rolls-schema
    |stringMask=bitlap.rolls.core.annotations.stringMask
    |rollsRuntimeClass=bitlap.rolls.core.RollsRuntime
    |rollsRuntimeToStringMethod=toString_""".stripMargin
    
scalacOptions ++= Seq(
  s"-P:RollsCompilerPlugin:$config"
)
```

> The feature with experimental annotations has hardly been tested and is not available.