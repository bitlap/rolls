---
title: prettyToString Annotation
custom_edit_url: https://github.com/bitlap/rolls/edit/master/docs/prettyToString_annotation.md
---

## Installation using SBT (Recommended)

If you are building with sbt, add the following to your `build.sbt`:

```scala
autoCompilerPlugins := true
addCompilerPlugin("org.bitlap" %% "rolls-compiler-plugin" % "<version>")
libraryDependencies ++= Seq(
  "org.bitlap" %% "rolls-core" % "<version>",
  // if these jackson already exists in the classpath, there is no need to add them
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "<jacksonVersion>",
  "com.github.pjfanning" %% "jackson-module-scala3-enum" % "<jacksonVersion>",
  "com.fasterxml.jackson.datatype" % "jackson-datatype-jsr310" % "<jacksonVersion>",
)
```

## Use `@prettyToString` on classes

**@prettyToString** can be added on classes or case classes. It will ignore private fields.

- If `standard` is `true`, call `toString` yields `Test(field1=value1,field2=value2)`.
- Otherwise, call `toString` yields `{"field1":"value1","field2":"value2"}`.

```scala mdoc
import bitlap.rolls.core.annotations.*

@prettyToString(standard = false)
final case class TestCaseClassJsonNamedArg(
  id: String,
  tenantId: Map[String, String],
  private val resourceActions: List[String],
  deleted: Long,
  subPermissions: List[String]
)
```

**Call the `toString`**

```json
{"tenantId":{"7f6e55cf-18e8-498c-8444-c128ca6cc71a":"7f6e55cf-18e8-498c-8444-c128ca6cc71a","b86a1c62-b15b-4827-a617-770a2d975dbc":"b86a1c62-b15b-4827-a617-770a2d975dbc","0eb0d095-dfaf-431b-a57f-e49af575e2ce":"0eb0d095-dfaf-431b-a57f-e49af575e2ce","b5bf7e61-b8a2-479a-83fa-52b7d5a220bb":"b5bf7e61-b8a2-479a-83fa-52b7d5a220bb","79642570-62a0-4639-93b2-9a86bb13027f":"79642570-62a0-4639-93b2-9a86bb13027f"},"resourceActions":["f2d41db2-862c-45b4-bfe2-df85f85f2de3","23b8f097-0930-42e6-8951-84ac4103829f","70ca2742-05a1-4028-8ccc-3be553e867c3","fa52af1a-cafc-4bf3-9e28-425f5b8a9d9f","0d6bc9f4-42fc-4999-8d40-3d1f87fc2a58"],"deleted":-925601941732221555,"subPermissions":["d0b5f85e-3a0a-4708-998c-9bfcb0711220","112760b2-668d-4b3e-9ec6-3f742b77ece9","1cb1e312-137c-4ad2-a443-bb1dce8695ac","54678377-b840-4de9-9058-a5413097c813","9f37a1ca-06ad-4f8f-b7af-73b1c3594ee3"],"id":"63f796e3-da90-4adf-8537-486d5032d8b4"}
```

## Use `@stringMask` on fields

**@stringMask** can be added on the fields of class or case class, the field will output as `***`.

`@stringMask` can be added on the field type or on the field name.

```scala mdoc
import bitlap.rolls.core.annotations.*

@prettyToString
final case class TestCaseClassJson(
  id: String,
  tenantId: Map[String, String] @stringMask,
  private val resourceActions: List[String],
  @stringMask deleted: Long,
  @stringMask subPermissions: List[String]
)
```

**Call the `toString`**

```json
{"tenantId":"***","resourceActions":["63e67982-7cc3-49d1-bd7c-71f88f9cdd6f","1e0d3459-3ae7-4488-8f56-1f0a94128de8","2df5a658-3a85-4737-9f84-1ddcb08f7c34","b22fceaa-73ee-47b2-bf4e-7eb2908c7cf4","676d1c4d-6fd5-446c-b02e-7dae59d374b3"],"deleted":"***","subPermissions":"***","id":"f7f937c0-32de-4380-acca-9b4760d18cde"}
```
