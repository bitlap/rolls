package bitlap.rolls.csv

import scala.io.Source
import scala.io.*
import java.io.*
import bitlap.rolls.core.*

import scala.deriving.Mirror
import scala.util.Using

/** @author
 *    梦境迷离
 *  @version 1.0,5/13/22
 */
object CSVUtils {

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

  private def writeCSV(file: File, headerRow: List[String], lines: List[String])(using
    format: CSVFormat
  ): Boolean = {
    checkFile(file)
    Using.resource(
      new PrintWriter(
        new FileWriter(file, format.stringCharset.charset, format.append),
        true
      )
    ) { r =>
      lines.zipWithIndex.foreach { case (line, index) =>
        if (line.isEmpty) {} else if (headerRow.nonEmpty && index == 0) {
          r.println(headerRow.mkString(format.delimiter.toString))
          r.println(line)
        } else {
          r.println(line)
        }
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

  inline private def readFromFileWithMetadata[T](file: File, func: String => T)(using
    mirror: Mirror.ProductOf[T],
    format: CSVFormat
  ): CSVData[T] = {
    val bufferedSource: BufferedSource = Source.fromFile(file)(Codec(format.stringCharset.charset))
    val (rawHeader, lazyList) =
      if format.ignoreHeader then
        LazyList.from(bufferedSource.getLines()).headOption -> LazyList.from(bufferedSource.getLines()).drop(1)
      else None                                             -> LazyList.from(bufferedSource.getLines())

    val fields: List[String] = mirrors.labels[T](using mirror)
    CSVMetadata(
      rawHeader.map(_.split(format.delimiter).toList).toList.flatten,
      fields,
      () => lazyList.size,
      () => lazyList.count(_.split(format.delimiter).length != fields.size)
    ) -> lazyList.map(func)

  }
}
