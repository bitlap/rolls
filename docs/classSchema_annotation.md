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

## Query Schema

If you are building with sbt, add the following to your `build.sbt`:

```scala
autoCompilerPlugins := true
addCompilerPlugin("org.bitlap" %% "rolls-compiler-plugin" % "<version>")
libraryDependencies ++= Seq(
  "org.bitlap" %% "rolls-core" % "<version>"
)

lazy val reader = scala.io.Source.fromFile("config.properties")
lazy val config = {
  val ret = reader.getLines().toList.map(p => s"-P:RollsCompilerPlugin:$p")
  reader.close()
  ret
}

scalacOptions ++= config
```

Add the following properties to **config.properties**:
```properties
classSchemaPostUri=http://localhost:18000/rolls-doc     # post data
postClassSchemaToServer=true                            # enable
classSchemaQueryUri=http://localhost:18000/rolls-schema # query data
```

**Run Server**
```scala
sbt "rolls-server/runMain bitlap.rolls.server.HttpServer"
```

`curl http://localhost:18000/rolls-schema?className=SimpleClassTest`

By default, binary files will be stored in `/tmp/.compiler/classSchema_%s.txt`, which can be configured by `config.properties`.

## Get JSON output

```json
{
  "className":"SimpleClassTest",
  "methods":[
    {
      "name":"testMethod",
      "params":[
        {
          "typeName":"List",
          "name":"listField",
          "typeArgs":[
            {
              "typeName":"SubSubSubAuthPermissionPO",
              "fields":[
                {
                  "typeName":"List",
                  "name":"list",
                  "typeArgs":[
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
          "name":"stringField"
        },
        {
          "typeName":"Option",
          "name":"optField",
          "typeArgs":[
            {
              "typeName":"SubSubSubAuthPermissionPO",
              "fields":[
                {
                  "typeName":"List",
                  "name":"list",
                  "typeArgs":[
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
          "name":"nestedObjectField",
          "fields":[
            {
              "typeName":"List",
              "name":"list",
              "typeArgs":[
                {
                  "typeName":"String"
                }
              ]
            }
          ]
        },
        {
          "typeName":"Either",
          "name":"eitherField",
          "typeArgs":[
            {
              "typeName":"String"
            },
            {
              "typeName":"SubSubSubAuthPermissionPO",
              "fields":[
                {
                  "typeName":"List",
                  "name":"list",
                  "typeArgs":[
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
            "name":"list",
            "typeArgs":[
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
