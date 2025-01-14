package bitlap.rolls.csv

import java.io.*

import scala.deriving.Mirror
import scala.io.*
import scala.io.Source
import scala.util.Using

import bitlap.rolls.core.*

/** @author
 *    梦境迷离
 *  @version 1.0,5/13/22
 */
object CSVUtils:

  opaque type FileName = String
  type CSVData[T]      = (CSVMetadata, LazyList[T])

  def FileName(fileName: String): FileName = fileName

  inline def writeCSV[T](fileName: FileName, objs: List[T])(
      encodeLine: T => String
  )(using mirror: Mirror.ProductOf[T], format: CSVFormat = DefaultCSVFormat): Boolean =
    val fields = mirrors.labels[T](using mirror)
    writeCSV(new File(fileName), fields, objs.map(encodeLine))(using format)

  inline def writeCSV[T](file: File, objs: List[T])(
      encodeLine: T => String
  )(using mirror: Mirror.ProductOf[T], format: CSVFormat): Boolean =
    val fields = mirrors.labels[T](using mirror)
    writeCSV(file, fields, objs.map(encodeLine))(using format)

  private def writeCSV(file: File, fields: List[String], lines: List[String])(using
      format: CSVFormat
  ): Boolean = {
    checkFile(file)
    // write class fieldName as csv header
    val headerRows = fields.map(StringUtils.lowerUnderscore)
    Using.resource(
      new PrintWriter(
        new FileWriter(file, format.stringCharset.charset, format.append),
        true
      )
    ) { r =>

      if (format.hasHeaders && headerRows.nonEmpty) {
        if (format.hasColIndex) {
          r.println((List(0) ::: headerRows).mkString(format.delimiter.toString))
        } else {
          r.println(headerRows.mkString(format.delimiter.toString))
        }
        r.flush()
      }

      lines.zipWithIndex.foreach { case (line, index) =>
        if (line.nonEmpty) {
          if (format.hasColIndex) {
            r.println(s"${index + 1}${format.delimiter.toString}$line")
          } else {
            r.println(line)
          }
        } else {}
        r.flush()
      }
    }
    true
  }

  inline def readCSV[T](file: File)(
      decodeLine: String => T
  )(using mirror: Mirror.ProductOf[T], format: CSVFormat): CSVData[T] =
    CSVUtils.readFromFileWithMetadata[T](file, decodeLine)

  inline def readCSV[T](fileName: FileName)(decodeLine: String => T)(using
      mirror: Mirror.ProductOf[T],
      format: CSVFormat = DefaultCSVFormat
  ): CSVData[T] =
    CSVUtils.readFromFileWithMetadata[T](new File(fileName), decodeLine)

  private def checkFile(file: File): Unit = {
    if (file.isDirectory) {
      throw new Exception(s"File path: $file is a directory.")
    }
    if (!file.exists()) {
      if (!file.getParentFile.exists())
        file.getParentFile.mkdirs()
      file.createNewFile()
    }
  }

  inline private def readFromFileWithMetadata[T](file: File, decodeLine: String => T)(using
      mirror: Mirror.ProductOf[T],
      format: CSVFormat
  ): CSVData[T] = {
    val lines: Iterator[String] = Source.fromFile(file)(Codec(format.stringCharset.charset)).getLines()

    val (_rawHeaders, lazyList) =
      if format.hasHeaders && lines.hasNext then Some(lines.next()) -> LazyList.from(lines)
      else None                                                     -> LazyList.from(lines)

    val skipIndexCol =
      if (format.hasColIndex) lazyList.map(l => l.substring(StringUtils.firstDelimiterIndex(l))) else lazyList

    val fields: List[String] = mirrors.labels[T](using mirror)

    val rawHeaders = _rawHeaders.map(_.split(format.delimiter).toList).toList.flatten

    CSVMetadata(
      if (format.hasColIndex) rawHeaders.tail else rawHeaders,
      fields,
      () => skipIndexCol.size,
      () => skipIndexCol.count(_.split(format.delimiter).length != fields.size)
    ) -> skipIndexCol.map(decodeLine)

  }
