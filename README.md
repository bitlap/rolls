# rhs

![CI][Badge-CI] [![Nexus (Snapshots)][Badge-Snapshots]][Link-Snapshots]

[Badge-CI]: https://github.com/bitlap/rhs/actions/workflows/ScalaCI.yml/badge.svg
[Badge-Snapshots]: https://img.shields.io/nexus/s/org.bitlap/rhs-compiler-plugin_3?server=https%3A%2F%2Fs01.oss.sonatype.org
[Link-Snapshots]: https://s01.oss.sonatype.org/content/repositories/snapshots/org/bitlap/rhs-compiler-plugin_3

----

- **@RhsMapping** on `val`
  - Replace the `rhs` of the constant `ValDef` with specifying one
  - MUST start ResolveHandler.scala
- **@ClassSchema** on case classes (must be primary constructor) or class
  - Generate a schema for all public methods and will **exclude** methods of product
  - Write binary data to `/tmp/.compiler/classSchema_%s.txt`，`%s` is a class short name
  - Start `ResolveHandler.scala` to query class schema

## config
```scala
    autoCompilerPlugins := true,
    addCompilerPlugin("org.bitlap" %% "rhs-compiler-plugin" % <Version>),
    libraryDependencies += "org.bitlap" %% "rhs-annotations" % "0.1.0-SNAPSHOT"
```


## Use @ClassSchema annotation

Example:
```scala
final case class SimpleClassTest @ClassSchema() () {

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

> curl http://localhost:18000/rhs-schema?className=SimpleClassTest
 
Schema Json: 
```json
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