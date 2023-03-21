package bitlap.rhs.plugin.server

import bitlap.rhs.compiler.plugin.DocSchema

import java.io.*

/** @author
 *    梦境迷离
 *  @version 1.0,2023/3/21
 */
object Utils {

  final val OK    = 200
  final val Empty = ""

  def readObject(inputStream: InputStream): DocSchema =
    var objectInputStream: ObjectInputStream = null
    try {
      objectInputStream = new ObjectInputStream(inputStream)
      objectInputStream.readObject().asInstanceOf[DocSchema]
    } catch {
      case e: Exception =>
        e.printStackTrace()
        null
    } finally
      try
        if (objectInputStream != null) objectInputStream.close()
      catch {
        case ignore: Exception =>
      }

}
