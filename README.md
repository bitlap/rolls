# rolls

![CI][Badge-CI] [![Nexus (Snapshots)][Badge-Snapshots]][Link-Snapshots] [![codecov][Badge-Codecov]][Link-Codecov]

[Badge-CI]: https://github.com/bitlap/rolls/actions/workflows/ScalaCI.yml/badge.svg
[Badge-Snapshots]: https://img.shields.io/nexus/s/org.bitlap/rolls-compiler-plugin_3?server=https%3A%2F%2Fs01.oss.sonatype.org
[Link-Snapshots]: https://s01.oss.sonatype.org/content/repositories/snapshots/org/bitlap/rolls-compiler-plugin_3
[Badge-Codecov]: https://codecov.io/gh/bitlap/rolls/branch/master/graph/badge.svg?token=IA596YRTOT
[Link-Codecov]: https://codecov.io/gh/bitlap/rolls

----

- **@rhsMapping** on `val`
  - Replace the `rhs` of the constant `ValDef` with specifying one
  - Must start `HttpServer.scala`
- **@classSchema** on case classes or classes
  - Generate a schema for all public methods and **exclude** methods of product
  - Write binary data to `/tmp/.compiler/classSchema_%s.txt`，`%s` is a class short name
  - Start `HttpServer.scala` to query class schema
- **@prettyToString** on case classes or classes
  - Generate `toString`, has one `standard` arg for config, default is `false`
  - If `standard` is `true`,  `toString` get `Test(field1=value1,field2=value2)`
  - If `standard` is `false`,  `toString` get `{"field1":"value1","field2":"value2"}`
  - `@stringMask` add on the fields of classes or case classes, the field will output as `***`
- config `config.properties`

## config
``` sbt
autoCompilerPlugins := true
addCompilerPlugin("org.bitlap" %% "rolls-compiler-plugin" % "0.1.2-SNAPSHOT")
libraryDependencies += "org.bitlap" %% "rolls-core" % "0.1.2-SNAPSHOT"
```

## @classSchema

<details>
<summary>Example</summary>

``` scala
// query result: curl http://localhost:18000/rolls-schema?className=SimpleClassTest
@classSchema
final case class SimpleClassTest () {
  def testMethod(
    listField: List[SubSubSubAuthPermissionPO],
    stringField: String,
    longOptField: Option[SubSubSubAuthPermissionPO],
    NestedObjectField: SubSubSubAuthPermissionPO,
    eitherField: Either[String, SubSubSubAuthPermissionPO]
  ): SubSubSubAuthPermissionPO = ???
}
final case class SubSubSubAuthPermissionPO(list: List[String])
```

</details>
 

<details>
<summary>Query Result</summary>

``` json
{
  "className":"SimpleClassTest",
  "methods":[
    {
      "methodName":"testMethod",
      "params":[
        {
          "typeName":"List",
          "fieldName":"listField",
          "genericType":[
            {
              "typeName":"SubSubSubAuthPermissionPO",
              "fields":[
                {
                  "typeName":"List",
                  "fieldName":"list",
                  "genericType":[
                    {
                      "typeName":"String"
                    }
                  ]
                }
              ]
            }
          ]
        },
        {
          "typeName":"String",
          "fieldName":"stringField"
        },
        {
          "typeName":"Option",
          "fieldName":"optField",
          "genericType":[
            {
              "typeName":"SubSubSubAuthPermissionPO",
              "fields":[
                {
                  "typeName":"List",
                  "fieldName":"list",
                  "genericType":[
                    {
                      "typeName":"String"
                    }
                  ]
                }
              ]
            }
          ]
        },
        {
          "typeName":"SubSubSubAuthPermissionPO",
          "fields":[
            {
              "typeName":"List",
              "fieldName":"list",
              "genericType":[
                {
                  "typeName":"String"
                }
              ]
            }
          ],
          "fieldName":"NestedObjectField"
        },
        {
          "typeName":"Either",
          "fieldName":"eitherField",
          "genericType":[
            {
              "typeName":"String"
            },
            {
              "typeName":"SubSubSubAuthPermissionPO",
              "fields":[
                {
                  "typeName":"List",
                  "fieldName":"list",
                  "genericType":[
                    {
                      "typeName":"String"
                    }
                  ]
                }
              ]
            }
          ]
        }
      ],
      "resultType":{
        "typeName":"SubSubSubAuthPermissionPO",
        "fields":[
          {
            "typeName":"List",
            "fieldName":"list",
            "genericType":[
              {
                "typeName":"String"
              }
            ]
          }
        ]
      }
    }
  ]
}
```

</details>


## @prettyToString

<details>
<summary>Example</summary>

``` scala
  @prettyToString(standard = false)
  final case class TestCaseClassJsonNamedArg(
    id: String,
    tenantId: Map[String, String],
    private val resourceActions: List[String],
    deleted: Long,
    subPermissions: List[String]
  )
```

</details>

<details>
<summary>toString Result</summary>


``` json
  {"tenantId":{"7f6e55cf-18e8-498c-8444-c128ca6cc71a":"7f6e55cf-18e8-498c-8444-c128ca6cc71a","b86a1c62-b15b-4827-a617-770a2d975dbc":"b86a1c62-b15b-4827-a617-770a2d975dbc","0eb0d095-dfaf-431b-a57f-e49af575e2ce":"0eb0d095-dfaf-431b-a57f-e49af575e2ce","b5bf7e61-b8a2-479a-83fa-52b7d5a220bb":"b5bf7e61-b8a2-479a-83fa-52b7d5a220bb","79642570-62a0-4639-93b2-9a86bb13027f":"79642570-62a0-4639-93b2-9a86bb13027f"},"resourceActions":["f2d41db2-862c-45b4-bfe2-df85f85f2de3","23b8f097-0930-42e6-8951-84ac4103829f","70ca2742-05a1-4028-8ccc-3be553e867c3","fa52af1a-cafc-4bf3-9e28-425f5b8a9d9f","0d6bc9f4-42fc-4999-8d40-3d1f87fc2a58"],"deleted":-925601941732221555,"subPermissions":["d0b5f85e-3a0a-4708-998c-9bfcb0711220","112760b2-668d-4b3e-9ec6-3f742b77ece9","1cb1e312-137c-4ad2-a443-bb1dce8695ac","54678377-b840-4de9-9058-a5413097c813","9f37a1ca-06ad-4f8f-b7af-73b1c3594ee3"],"id":"63f796e3-da90-4adf-8537-486d5032d8b4"}
```

</details>

## @prettyToString with @stringMask

<details>
<summary>Example</summary>

``` scala
  @prettyToString
  final case class TestCaseClassJson(
    id: String,
    tenantId: Map[String, String] @stringMask,
    private val resourceActions: List[String],
    @stringMask deleted: Long,
    @stringMask subPermissions: List[String]
  )
```

</details>

<details>
<summary>toString Result</summary>


``` json
  {"tenantId":"***","resourceActions":["63e67982-7cc3-49d1-bd7c-71f88f9cdd6f","1e0d3459-3ae7-4488-8f56-1f0a94128de8","2df5a658-3a85-4737-9f84-1ddcb08f7c34","b22fceaa-73ee-47b2-bf4e-7eb2908c7cf4","676d1c4d-6fd5-446c-b02e-7dae59d374b3"],"deleted":"***","subPermissions":"***","id":"f7f937c0-32de-4380-acca-9b4760d18cde"}
```

</details>
