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
  @prettyToString
  final case class TestCaseClass1(
    id: String,
    tenantId: Map[String, String],
    private val resourceActions: List[String],
    deleted: Int = 99,
    updateTime: Instant = Instant.now(),
    subPermissions: List[String]
  )
  @prettyToString(true)
  final class TestClass1(
    id: String,
    val tenantId: Map[String, String],
    private val resourceActions: List[String],
    deleted: Int = 99,
    updateTime: Instant = Instant.now(),
    subPermissions: List[String]
  )
```

</details>

<details>
<summary>toString Result</summary>


```
  {"id":"iddd","tenantId":{},"deleted":98,"updateTime":1680085179.395021000,"subPermissions":[]}
  TestClass1(tenantId=Map())
```

</details>
