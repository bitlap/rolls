---
title: Validate Ident Prefix
custom_edit_url: https://github.com/bitlap/rolls/edit/master/docs/validate_ident_prefix.md
---

Add Config for compiler plugin:
``` scala
// Multiple annotations separated by comma.
lazy val config =
    """|validateIdentPrefix=caliban.schema.Annotations.GQLDescription
       |validateShouldStartsWith=star""".stripMargin

scalacOptions ++= Seq(
  s"-P:RollsCompilerPlugin:$config"
)
```

## What will be verified ? 

- Validate the parameter name within primary constructor:
  - when the parameter type is a case class or function type
  - when there are annotations on the parameter or parameter type
- Validate the case class name (use `.capitalize` to validate case classes):
  - when there are annotations on the primary constructor or type constructor
  - when there are annotations on the function type or case class parameters within primary constructor

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

The above code will provide compiler error:
```
The parameter name of the primary constructor does not startsWith star in StarDictInput
Expected: starDictFunction 
Actual: dictFunction

  final case class StarDictInput(
```

> Because dictFunction does not startsWith star