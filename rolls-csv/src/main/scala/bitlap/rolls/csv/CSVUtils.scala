package bitlap.rolls.csv

import scala.io.Source
import scala.reflect.Selectable.reflectiveSelectable
import scala.util.control.Exception.ignoring
import scala.collection.mutable.ListBuffer
import scala.io.*
import java.io.*
import bitlap.rolls.core.*

import scala.compiletime.summonFrom
import scala.deriving.Mirror
import scala.language.reflectiveCalls

/** @author
 *    梦境迷离
 *  @version 1.0,5/13/22
 */
object CSVUtils {

  opaque type FileName   = String
  opaque type CSVData[T] = (CSVMetadata, LazyList[T])

  def FileName(fileName: String): FileName = fileName

  private type Closable = {
    def close(): Unit
  }

  def using[R <: Closable, T](resource: => R)(f: R => T): T =
    try f(resource)
    finally
      ignoring(classOf[Throwable]) apply {
        resource.close()
      }

  def writeCSV[T](file: File, objs: List[T])(encodeLine: T => String)(using format: CsvFormat): Boolean =
    writeCSV(file, objs.map(encodeLine))

  def writeCSV(file: File, lines: List[String])(using format: CsvFormat): Boolean = {
    checkFile(file)
    val bufferedWriter = new BufferedWriter(
      new OutputStreamWriter(new FileOutputStream(file, format.append), format.encoding)
    )
    try
      using(new PrintWriter(bufferedWriter, true)) { r =>
        lines.zipWithIndex.foreach { case (line, index) =>
          if (line.isEmpty) {} else if (format.prependHeader.nonEmpty && index == 0) {
            r.println(format.prependHeader.mkString(format.delimiter.toString))
            r.println(line)
          } else {
            r.println(line)
          }
          r.flush()
        }
      }
    finally bufferedWriter.close()
    true
  }

  def readCSV[T](fileName: FileName)(decodeLine: String => T)(using format: CsvFormat): LazyList[T] =
    readCSV[T](new File(fileName))(decodeLine)

  def readCSV[T](file: File)(decodeLine: String => T)(using format: CsvFormat = DefaultCsvFormat): LazyList[T] =
    CSVUtils.readFromFile[T](file, decodeLine)

  inline def readCSVWithMetadata[T](file: File)(
    decodeLine: String => T
  )(using format: CsvFormat = DefaultCsvFormat, mirror: Mirror.ProductOf[T]): CSVData[T] =
    CSVUtils.readFromFileWithMetadata[T](file, decodeLine)

  inline def readCSVWithMetadata[T](fileName: FileName)(decodeLine: String => T)(using
    format: CsvFormat,
    mirror: Mirror.ProductOf[T]
  ): CSVData[T] =
    CSVUtils.readFromFileWithMetadata[T](new File(fileName), decodeLine)

  private def checkFile(file: File): Unit = {
    if (file.isDirectory) {
      throw new Exception(s"File path: $file is a directory.")
    }
    if (!file.exists()) {
      file.createNewFile()
    }
  }

  private def readFromFile[T](file: File, decodeLine: String => T)(using
    format: CsvFormat
  ): LazyList[T] = {
    val bufferedSource: BufferedSource = Source.fromFile(file)(Codec(format.encoding))
    val lazyList =
      if format.ignoreHeader then LazyList.from(bufferedSource.getLines()).drop(1)
      else LazyList.from(bufferedSource.getLines())
    lazyList.map(decodeLine)
  }

  private inline def readFromFileWithMetadata[T](file: File, func: String => T)(using
    format: CsvFormat,
    mirror: Mirror.ProductOf[T]
  ): CSVData[T] = {
    val bufferedSource: BufferedSource = Source.fromFile(file)(Codec(format.encoding))
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
