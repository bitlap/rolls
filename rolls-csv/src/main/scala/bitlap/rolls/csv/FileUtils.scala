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
object FileUtils {

  type Closable = {
    def close(): Unit
  }

  def using[R <: Closable, T](resource: => R)(f: R => T): T =
    try f(resource)
    finally
      ignoring(classOf[Throwable]) apply {
        resource.close()
      }

  def write(file: File, lines: List[String], format: CsvFormat): Boolean = {
    checkFile(file)
    val bufferedWriter = new BufferedWriter(
      new OutputStreamWriter(new FileOutputStream(file, format.append), format.encoding)
    )
    try
      using(new PrintWriter(bufferedWriter, true)) { r =>
        lines.zipWithIndex.foreach { case (line, index) =>
          if (format.ignoreEmptyLines && line.isEmpty) {} else if (format.prependHeader.nonEmpty && index == 0) {
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

  def read(file: InputStream, format: CsvFormat): List[String] =
    try
      using(Source.fromInputStream(file, format.encoding)) { lines =>
        val ls = if (format.ignoreHeader) {
          lines.getLines().toList.tail
        } else {
          lines.getLines().toList
        }
        if (format.ignoreEmptyLines) {
          ls.filter(_.nonEmpty)
        } else {
          ls
        }
      }
    finally file.close()

  private def checkFile(file: File): Unit = {
    if (file.isDirectory) {
      throw new Exception(s"File path: $file is a directory.")
    }
    if (!file.exists()) {
      file.createNewFile()
    }
  }

  def readFileFunc[T](reader: BufferedReader, func: String => Option[T]): List[Option[T]] = {
    val ts           = ListBuffer[Option[T]]()
    var line: String = null
    FileUtils.using(new BufferedReader(reader)) { input =>
      while ({
        line = input.readLine()
        line != null
      })
        ts.append(func(line))
    }
    ts.result()
  }

  def readCsvFromClassPath[T <: Product](fileName: String)(func: String => Option[T]): List[Option[T]] = {
    val reader = new InputStreamReader(ClassLoader.getSystemResourceAsStream(fileName))
    FileUtils.readFileFunc[T](new BufferedReader(reader), func)
  }

  def readCsvFromFile[T <: Product](file: File)(func: String => Option[T]): List[Option[T]] = {
    val reader = new BufferedReader(new FileReader(file))
    FileUtils.readFileFunc[T](reader, func)
  }
}
