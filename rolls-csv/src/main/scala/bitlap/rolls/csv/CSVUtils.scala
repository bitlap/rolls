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

  def readFileFunc[T](reader: BufferedReader, func: String => T): List[T] = {
    val ts           = ListBuffer[T]()
    var line: String = null
    CSVUtils.using(new BufferedReader(reader)) { input =>
      while ({
        line = input.readLine()
        line != null
      })
        ts.append(func(line))
    }
    ts.result()
  }

  def readCSV[T <: Product](fileName: String)(func: String => T): List[T] =
    readCSV[T](new File(fileName))(func)

  def readCSV[T <: Product](file: File)(func: String => T): List[T] = {
    val reader = new BufferedReader(new FileReader(file))
    CSVUtils.readFileFunc[T](reader, func)
  }
}
