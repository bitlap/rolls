---
title: classSchema Annotation
custom_edit_url: https://github.com/bitlap/rolls/edit/master/docs/classSchema_annotation.md
---

## Installation using SBT (Recommended)

If you are building with sbt, add the following to your `build.sbt`:

```scala
autoCompilerPlugins := true
addCompilerPlugin("org.bitlap" %% "rolls-compiler-plugin" % "<version>")
libraryDependencies ++= Seq(
  "org.bitlap" %% "rolls-core" % "<version>"
)
```

## Use `@classSchema` on classes

**@classSchema** can be added on classes or case classes. It only resolves schemas for public concrete methods and **excludes** the methods of `Product` and `Any`.

```scala mdoc
import bitlap.rolls.core.annotations.classSchema

@classSchema
final case class SimpleClassTest() {
  def testMethod(
    listField: List[SubSubSubAuthPermissionPO],
    stringField: String,
    longOptField: Option[SubSubSubAuthPermissionPO],
    nestedObjectField: SubSubSubAuthPermissionPO,
    eitherField: Either[String, SubSubSubAuthPermissionPO]
  ): SubSubSubAuthPermissionPO = ???
}

final case class SubSubSubAuthPermissionPO(list: List[String])
```

## Get Data

By default, binary files (use java serialization) will be stored in `/tmp/.compiler/classSchema_%s.txt`, which can be configured by `config.properties`.

If you need to obtain data through HTTP, add the following to your `build.sbt`:

```scala
lazy val reader = scala.io.Source.fromFile("config.properties")
lazy val config = {
  val ret = reader.getLines().toList.map(p => s"-P:RollsCompilerPlugin:$p")
  reader.close()
  ret
}

scalacOptions ++= config
libraryDependencies ++= Seq(
  "org.bitlap" %% "rolls-compiler-plugin" % "<version>"
)
```

Then add the following properties to **config.properties**:
```properties
classSchemaPostUri=http://localhost:18000/rolls-doc
```

The data will be written to the uri service as a post request through the `ObjectOutputStream`, We read a case class object through the tool method:

```scala
val inputStream: InputStream = ???
val schema: ClassSchema = Utils.readObject(inputStream)
```