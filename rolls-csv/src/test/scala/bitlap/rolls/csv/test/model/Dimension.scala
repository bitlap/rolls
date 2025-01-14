package bitlap.rolls.csv.test.model

final case class Dimension(key: String, value: String)
final case class Metric(time: Long, entity: Int, dimensions: List[Dimension], metricName: String, metricValue: Int)

final case class MultipleFieldsMetric(
    time: Long,
    entity: Int,
    dimensions: List[Dimension],
    metricName: String,
    metricValue: Int,
    attributes: List[Dimension]
)

object Metric:

  lazy val `simple_data_string` =
    """100,1,"{""city"":""北京"",""os"":""Mac""}",vv,1
      |100,1,"{""city"":""北京"",""os"":""Mac""}",pv,2
      |100,1,"{""city"":""北京"",""os"":""Windows""}",vv,1
      |100,1,"{""city"":""北京"",""os"":""Windows""}",pv,3
      |100,2,"{""city"":""北京"",""os"":""Mac""}",vv,1
      |100,2,"{""city"":""北京"",""os"":""Mac""}",pv,5
      |100,3,"{""city"":""北京"",""os"":""Mac""}",vv,1
      |100,3,"{""city"":""北京"",""os"":""Mac""}",pv,2
      |200,1,"{""city"":""北京"",""os"":""Mac""}",vv,1
      |200,1,"{""city"":""北京"",""os"":""Mac""}",pv,2
      |200,1,"{""city"":""北京"",""os"":""Windows""}",vv,1
      |200,1,"{""city"":""北京"",""os"":""Windows""}",pv,3
      |200,2,"{""city"":""北京"",""os"":""Mac""}",vv,1
      |200,2,"{""city"":""北京"",""os"":""Mac""}",pv,5
      |200,3,"{""city"":""北京"",""os"":""Mac""}",vv,1
      |200,3,"{""city"":""北京"",""os"":""Mac""}",pv,2""".stripMargin

  lazy val `simple_data_objs` = List(
    Metric(100, 1, List(Dimension("city", "北京"), Dimension("os", "Mac")), "vv", 1),
    Metric(100, 1, List(Dimension("city", "北京"), Dimension("os", "Mac")), "pv", 2),
    Metric(100, 1, List(Dimension("city", "北京"), Dimension("os", "Windows")), "vv", 1),
    Metric(100, 1, List(Dimension("city", "北京"), Dimension("os", "Windows")), "pv", 3),
    Metric(100, 2, List(Dimension("city", "北京"), Dimension("os", "Mac")), "vv", 1),
    Metric(100, 2, List(Dimension("city", "北京"), Dimension("os", "Mac")), "pv", 5),
    Metric(100, 3, List(Dimension("city", "北京"), Dimension("os", "Mac")), "vv", 1),
    Metric(100, 3, List(Dimension("city", "北京"), Dimension("os", "Mac")), "pv", 2),
    Metric(200, 1, List(Dimension("city", "北京"), Dimension("os", "Mac")), "vv", 1),
    Metric(200, 1, List(Dimension("city", "北京"), Dimension("os", "Mac")), "pv", 2),
    Metric(200, 1, List(Dimension("city", "北京"), Dimension("os", "Windows")), "vv", 1),
    Metric(200, 1, List(Dimension("city", "北京"), Dimension("os", "Windows")), "pv", 3),
    Metric(200, 2, List(Dimension("city", "北京"), Dimension("os", "Mac")), "vv", 1),
    Metric(200, 2, List(Dimension("city", "北京"), Dimension("os", "Mac")), "pv", 5),
    Metric(200, 3, List(Dimension("city", "北京"), Dimension("os", "Mac")), "vv", 1),
    Metric(200, 3, List(Dimension("city", "北京"), Dimension("os", "Mac")), "pv", 2)
  )

  lazy val `multiple_field_data_objs` = List(
    MultipleFieldsMetric(
      100,
      1,
      List(Dimension("city", "北京"), Dimension("os", "Mac")),
      "vv",
      1,
      List(Dimension("city", "北京"), Dimension("os", "Mac"))
    ),
    MultipleFieldsMetric(
      100,
      1,
      List(Dimension("city", "北京"), Dimension("os", "Mac")),
      "pv",
      2,
      List(Dimension("city", "北京"), Dimension("os", "Mac"))
    ),
    MultipleFieldsMetric(
      100,
      1,
      List(Dimension("city", "北京"), Dimension("os", "Windows")),
      "vv",
      1,
      List(Dimension("city", "北京"), Dimension("os", "Windows"))
    ),
    MultipleFieldsMetric(
      100,
      1,
      List(Dimension("city", "北京"), Dimension("os", "Windows")),
      "pv",
      3,
      List(Dimension("city", "北京"), Dimension("os", "Windows"))
    ),
    MultipleFieldsMetric(
      100,
      2,
      List(Dimension("city", "北京"), Dimension("os", "Mac")),
      "vv",
      1,
      List(Dimension("city", "北京"), Dimension("os", "Mac"))
    ),
    MultipleFieldsMetric(
      100,
      2,
      List(Dimension("city", "北京"), Dimension("os", "Mac")),
      "pv",
      5,
      List(Dimension("city", "北京"), Dimension("os", "Mac"))
    ),
    MultipleFieldsMetric(
      100,
      3,
      List(Dimension("city", "北京"), Dimension("os", "Mac")),
      "vv",
      1,
      List(Dimension("city", "北京"), Dimension("os", "Mac"))
    ),
    MultipleFieldsMetric(
      100,
      3,
      List(Dimension("city", "北京"), Dimension("os", "Mac")),
      "pv",
      2,
      List(Dimension("city", "北京"), Dimension("os", "Mac"))
    ),
    MultipleFieldsMetric(
      200,
      1,
      List(Dimension("city", "北京"), Dimension("os", "Mac")),
      "vv",
      1,
      List(Dimension("city", "北京"), Dimension("os", "Mac"))
    ),
    MultipleFieldsMetric(
      200,
      1,
      List(Dimension("city", "北京"), Dimension("os", "Mac")),
      "pv",
      2,
      List(Dimension("city", "北京"), Dimension("os", "Mac"))
    ),
    MultipleFieldsMetric(
      200,
      1,
      List(Dimension("city", "北京"), Dimension("os", "Windows")),
      "vv",
      1,
      List(Dimension("city", "北京"), Dimension("os", "Windows"))
    ),
    MultipleFieldsMetric(
      200,
      1,
      List(Dimension("city", "北京"), Dimension("os", "Windows")),
      "pv",
      3,
      List(Dimension("city", "北京"), Dimension("os", "Windows"))
    ),
    MultipleFieldsMetric(
      200,
      2,
      List(Dimension("city", "北京"), Dimension("os", "Mac")),
      "vv",
      1,
      List(Dimension("city", "北京"), Dimension("os", "Mac"))
    ),
    MultipleFieldsMetric(
      200,
      2,
      List(Dimension("city", "北京"), Dimension("os", "Mac")),
      "pv",
      5,
      List(Dimension("city", "北京"), Dimension("os", "Mac"))
    ),
    MultipleFieldsMetric(
      200,
      3,
      List(Dimension("city", "北京"), Dimension("os", "Mac")),
      "vv",
      1,
      List(Dimension("city", "北京"), Dimension("os", "Mac"))
    ),
    MultipleFieldsMetric(
      200,
      3,
      List(Dimension("city", "北京"), Dimension("os", "Mac")),
      "pv",
      2,
      List(Dimension("city", "北京"), Dimension("os", "Mac"))
    )
  )
end Metric
