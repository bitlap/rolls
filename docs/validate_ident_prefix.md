---
title: Validate Ident Prefix
custom_edit_url: https://github.com/bitlap/rolls/edit/master/docs/validate_ident_prefix.md
---

Add Config:
``` scala
lazy val config =
    """|validatePrefixPhaseBy=caliban.schema.Annotations.GQLDescription
       |validateShouldStartWith=star""".stripMargin

scalacOptions ++= Seq(
  s"-P:RollsCompilerPlugin:$config"
)
```

## Validate it

- Validate the parameter name within primary constructor:
  - When the parameter type is a case class or function type
  - when there are annotations on the parameter or parameter type.
- Validate the case class name (use `.capitalize` to validate case classes):
  - when there are annotations on the primary constructor or type constructor.
  - when there are annotations on the function type or case class parameters within primary constructor.

``` scala
final case class StarDictInput(
  @GQLDescription("dictName")
  dictName: Option[String],
  @GQLDescription("dictCode")
  dictCode: Option[String],
  @GQLDescription("starDictPayload")
  starDictPayload: StarDictPayload,
  @GQLDescription("dictFunction")
  dictFunction: String => StarDictPayload
)

final case class StarDictPayload(
  @GQLDescription("id")
  id: Option[String],
  @GQLDescription("name")
  name: Option[String]
)
```

Compile Error:
```
The parameter name of the primary constructor does not startWith star in StarDictInput
Expected: starDictFunction 
Actual: dictFunction

  final case class StarDictInput(
```