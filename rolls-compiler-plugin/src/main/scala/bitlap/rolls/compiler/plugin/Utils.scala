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
object Utils:

  final val OK    = 200
  final val Empty = ""

  final val ContentType = "Content-Type"

  @targetName("applicationJson")
  final val `application/json` = "application/json"

  private val timeout = Duration.ofSeconds(5)

  private lazy val client = HttpClient
    .newBuilder()
    .version(HttpClient.Version.HTTP_2)
    .connectTimeout(timeout)
    .followRedirects(HttpClient.Redirect.NEVER)
    .proxy(ProxySelector.getDefault)
    .build()

  private def postClassSchema(body: HttpRequest.BodyPublisher, config: RollsConfig): String =
    val request = HttpRequest.newBuilder
      .header(ContentType, `application/json`)
      .version(HttpClient.Version.HTTP_2)
      .uri(URI.create(config.classSchemaPostUri.getOrElse("")))
      .POST(body)
      .timeout(timeout)
      .build
    val response = client.send(request, HttpResponse.BodyHandlers.ofString)
    if response.statusCode == OK then response.body else Empty

  def sendClassSchema(classSchema: ClassSchema, config: RollsConfig): Unit =
    Using.resource(new ByteArrayOutputStream()) { byteArr =>
      Using.resource(new ObjectOutputStream(byteArr)) { outputStream =>
        outputStream.writeObject(classSchema)
        outputStream.flush()
        val file = Paths.get(config.classSchemaFolder, "/", config.classSchemaFileName.format(classSchema.className))
        if (file.toFile.exists()) {
          file.toFile.delete()
        }
        val f = new java.io.File(config.classSchemaFolder)
        if (f.exists()) {
          f.delete()
        }

        Files.createDirectories(Paths.get(config.classSchemaFolder))
        Files.createFile(file)
        Files.write(file, byteArr.toByteArray).getFileName.toUri.toString
        if (config.classSchemaPostUri.isDefined) {
          val buffer = BodyPublishers.ofByteArray(byteArr.toByteArray)
          postClassSchema(buffer, config)
        }
      }
    }

  def readObject(inputStream: InputStream): ClassSchema =
    Using.resource(new ObjectInputStreamWithCustomClassLoader(inputStream))(_.readObject().asInstanceOf[ClassSchema])

  def readObject(className: String, config: RollsConfig = RollsConfig.default): ClassSchema =
    Using.resource(
      new ObjectInputStreamWithCustomClassLoader(
        new ByteArrayInputStream(
          Files.readAllBytes(Paths.get(config.classSchemaFolder, "/", config.classSchemaFileName.format(className)))
        )
      )
    )(_.readObject().asInstanceOf[ClassSchema])

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
