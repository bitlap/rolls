package bitlap.rolls.csv.test.model

final case class Dimension(key: String, value: String)
case class Metric(time: Long, entity: Int, dimensions: List[Dimension], metricName: String, metricValue: Int)
