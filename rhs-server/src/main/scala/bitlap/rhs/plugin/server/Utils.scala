package bitlap.rhs.plugin.server

import bitlap.rhs.compiler.plugin.ClassSchema

import java.io.*
import scala.util.Using

/** @author
 *    梦境迷离
 *  @version 1.0,2023/3/21
 */
object Utils {

  final val OK    = 200
  final val Empty = ""

  def readObject(inputStream: InputStream): ClassSchema =
    Using.resource(new ObjectInputStream(inputStream)) { objectInputStream =>
      objectInputStream.readObject().asInstanceOf[ClassSchema]
    }

}
