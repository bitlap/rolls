package bitlap.rolls.compiler.plugin

import java.io.*
import java.net.*
import java.net.http.*
import java.net.http.HttpRequest.BodyPublishers
import java.nio.file.*
import java.time.Duration
import scala.annotation.*
import scala.util.Using

/** @author
 *    梦境迷离
 *  @version 1.0,2023/3/22
 */
object Utils {

  final val OK    = 200
  final val Empty = ""

  final val ContentType = "Content-Type"
  @targetName("applicationJson")
  final val `application/json` = "application/json"
  private final val folder     = "/tmp/.compiler"
  private val fileName         = s"$folder/classSchema_%s.txt"
  private val timeout          = Duration.ofSeconds(5)
  private lazy val client = HttpClient
    .newBuilder()
    .version(HttpClient.Version.HTTP_2)
    .connectTimeout(timeout)
    .followRedirects(HttpClient.Redirect.NEVER)
    .proxy(ProxySelector.getDefault)
    .build()

  val reqUrl            = "http://localhost:18000/rolls-mapping"
  private val reqDocUrl = "http://localhost:18000/rolls-doc"

  def sendRhsMapping(url: String): String =
    val request = HttpRequest.newBuilder
      .header(ContentType, `application/json`)
      .version(HttpClient.Version.HTTP_2)
      .uri(URI.create(url))
      .GET
      .timeout(timeout)
      .build
    val response = client.send(request, HttpResponse.BodyHandlers.ofString)
    if response.statusCode == OK then response.body else Empty

  private def sendClassSchema(body: HttpRequest.BodyPublisher): String =
    val request = HttpRequest.newBuilder
      .header(ContentType, `application/json`)
      .version(HttpClient.Version.HTTP_2)
      .uri(URI.create(reqDocUrl))
      .POST(body)
      .timeout(timeout)
      .build
    val response = client.send(request, HttpResponse.BodyHandlers.ofString)
    if response.statusCode == OK then response.body else Empty

  def sendClassSchema(classSchema: ClassSchema): String =
    Using.resource(new ByteArrayOutputStream()) { byteArr =>
      Using.resource(new ObjectOutputStream(byteArr)) { outputStream =>
        outputStream.writeObject(classSchema)
        outputStream.flush()
        val file = Paths.get(fileName.format(classSchema.className))
        if (file.toFile.exists()) {
          file.toFile.delete()
        }
        val f = new java.io.File(folder)
        if (f.exists()) {
          f.delete()
        }

        Files.createDirectories(Paths.get(folder))
        Files.createFile(file)
        Files.write(file, byteArr.toByteArray).getFileName.toUri.toString
//        val buffer = BodyPublishers.ofByteArray(byteArr.toByteArray)
//        sendClassSchema(buffer)
      }
    }

  def readObject(inputStream: InputStream): ClassSchema =
    Using.resource(new ObjectInputStreamWithCustomClassLoader(inputStream)) { objectInputStream =>
      objectInputStream.readObject().asInstanceOf[ClassSchema]
    }

  def readObject(className: String): ClassSchema =
    Using.resource(
      new ObjectInputStreamWithCustomClassLoader(
        new ByteArrayInputStream(Files.readAllBytes(Paths.get(fileName.format(className))))
      )
    ) { objectInputStream =>
      objectInputStream.readObject().asInstanceOf[ClassSchema]
    }

  private class ObjectInputStreamWithCustomClassLoader(
    inputStream: InputStream
  ) extends ObjectInputStream(inputStream):
    override def resolveClass(desc: java.io.ObjectStreamClass): Class[_] =
      try
        Class.forName(desc.getName, false, getClass.getClassLoader)
      catch {
        case ignore: ClassNotFoundException =>
          super.resolveClass(desc)
      }
}
