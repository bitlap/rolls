package bitlap.rhs.compiler.plugin

import java.io.{ ByteArrayInputStream, ByteArrayOutputStream, InputStream, ObjectInputStream, ObjectOutputStream }
import java.net.{ ProxySelector, URI }
import java.net.http.{ HttpClient, HttpRequest, HttpResponse }
import java.net.http.HttpRequest.BodyPublishers
import java.nio.file.{ Files, OpenOption, Paths, StandardOpenOption }
import java.time.Duration
import scala.util.Using

/** @author
 *    梦境迷离
 *  @version 1.0,2023/3/22
 */
object Utils {

  final val OK    = 200
  final val Empty = ""

  final val ContentType        = "Content-Type"
  final val `application/json` = "application/json"
  final val folder             = "/tmp/.compiler"
  private val fileName         = s"$folder/classSchema_%s.txt"
  private val timeout          = Duration.ofSeconds(5)
  private lazy val client = HttpClient
    .newBuilder()
    .version(HttpClient.Version.HTTP_2)
    .connectTimeout(timeout)
    .followRedirects(HttpClient.Redirect.NEVER)
    .proxy(ProxySelector.getDefault)
    .build()

  val reqUrl            = "http://localhost:18000/rhs-mapping"
  private val reqDocUrl = "http://localhost:18000/rhs-doc"

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

  private def sendRhsClassSchema(body: HttpRequest.BodyPublisher): String =
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

        Files.createDirectories((Paths.get(folder)))
        Files.createFile(file)
        Files.write(file, byteArr.toByteArray).getFileName.toUri.toString
//        val buffer = BodyPublishers.ofByteArray(byteArr.toByteArray)
//        sendRhsClassSchema(buffer)
      }
    }

  def readObject(inputStream: InputStream): ClassSchema =
    Using.resource(new ObjectInputStream(inputStream)) { objectInputStream =>
      objectInputStream.readObject().asInstanceOf[ClassSchema]
    }

  def readObject(className: String): ClassSchema =
    Using.resource(
      new ObjectInputStream(new ByteArrayInputStream(Files.readAllBytes(Paths.get(fileName.format(className)))))
    ) { objectInputStream =>
      objectInputStream.readObject().asInstanceOf[ClassSchema]
    }

}
