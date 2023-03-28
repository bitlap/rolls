package bitlap.rolls.annotations

object Utils {
  def toString_(p: Product): String =
    (p.productElementNames zip p.productIterator).map(e => s"${e._1}=${e._2}").mkString(p.productPrefix + "(", ",", ")")
}
