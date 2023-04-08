---
title: @classSchema Annotation
custom_edit_url: https://github.com/bitlap/rolls/edit/master/docs/classSchema_annotation.md
---

## Using `@classSchema` on classes

**@classSchema** can bed added on classes or case classes. It only for all public concrete methods and **exclude** methods of product.

``` scala
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

## Query Schema

curl `http://localhost:18000/rolls-schema?className=SimpleClassTest`

It depends configs:
``` properties
classSchemaPostUri=http://localhost:18000/rolls-doc     # post data
postClassSchemaToServer=true                            # enable
classSchemaQueryUri=http://localhost:18000/rolls-schema # query data
```

By default, binary files will be stored in `/tmp/.compiler/classSchema_%s.txt`, which can be configured by `config.properties`.

## Json Result

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