package bitlap.rolls.csv

import scala.io.Source
import scala.language.reflectiveCalls
import scala.util.control.Exception.ignoring

import scala.collection.mutable.ListBuffer
import java.io.*

/** @author
 *    梦境迷离
 *  @version 1.0,5/13/22
 */
object CSVUtils {

  private type Closable = {
    def close(): Unit
  }

  def using[R <: Closable, T](resource: => R)(f: R => T): T =
    try f(resource)
    finally
      ignoring(classOf[Throwable]) apply {
        resource.close()
      }

  def readFileFunc[T](reader: BufferedReader, func: String => T)(using
    format: CsvFormat = DefaultCsvFormat
  ): List[T] = {
    val ts           = ListBuffer[T]()
    var line: String = null
    var first        = true
    CSVUtils.using(new BufferedReader(reader)) { input =>
      while ({
        line = input.readLine()
        line != null
      })
        if (first && format.ignoreHeader) {
          first = false
        } else ts.append(func(line))
    }
    ts.result()
  }

  def writeCSV[T <: Product](file: File, objs: List[T])(func: T => String)(using format: CsvFormat): Boolean =
    writeCSV(file, objs.map(func))

  def writeCSV(fileName: String, lines: List[String])(using format: CsvFormat): Boolean =
    writeCSV(new File(fileName), lines)

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

  def readCSV[T <: Product](fileName: String)(func: String => T)(using format: CsvFormat): List[T] =
    readCSV[T](new File(fileName))(func)

  def readCSV[T <: Product](file: File)(func: String => T)(using format: CsvFormat): List[T] = {
    val reader = new BufferedReader(new FileReader(file))
    CSVUtils.readFileFunc[T](reader, func)
  }

  private def checkFile(file: File): Unit = {
    if (file.isDirectory) {
      throw new Exception(s"File path: $file is a directory.")
    }
    if (!file.exists()) {
      file.createNewFile()
    }
  }
}
