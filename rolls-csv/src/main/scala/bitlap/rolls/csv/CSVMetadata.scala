package bitlap.rolls.csv

/** @author
 *    梦境迷离
 *  @version 1.0,2023/4/16
 */
final case class CSVMetadata(
    rawHeaders: List[String],
    classFieldNames: List[String],
    rowsNum: () => Long,
    invalidRowsNum: () => Long
)
