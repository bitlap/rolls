package bitlap.rhs.compiler.plugin

import java.io.{ ByteArrayOutputStream, ObjectOutputStream }
import java.net.{ ProxySelector, URI }
import java.net.http.{ HttpClient, HttpRequest, HttpResponse }
import java.net.http.HttpRequest.BodyPublishers
import java.time.Duration
import scala.util.Using

/** @author
 *    梦境迷离
 *  @version 1.0,2023/3/22
 */
object Utils {

  private val timeout = Duration.ofSeconds(5)
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
      .header("Content-Type", "application/json")
      .version(HttpClient.Version.HTTP_2)
      .uri(URI.create(url))
      .GET
      .timeout(timeout)
      .build
    val response = client.send(request, HttpResponse.BodyHandlers.ofString)
    if response.statusCode == 200 then response.body else null

  private def sendRhsClassSchema(body: HttpRequest.BodyPublisher): String =
    val request = HttpRequest.newBuilder
      .header("Content-Type", "application/json")
      .version(HttpClient.Version.HTTP_2)
      .uri(URI.create(reqDocUrl))
      .POST(body)
      .timeout(timeout)
      .build
    val response = client.send(request, HttpResponse.BodyHandlers.ofString)
    if response.statusCode == 200 then response.body else null

  def sendClassSchema(classSchema: ClassSchema): String =
    Using.resource(new ByteArrayOutputStream()) { byteArr =>
      Using.resource(new ObjectOutputStream(byteArr)) { outputStream =>
        outputStream.writeObject(classSchema)
        outputStream.flush()
        val buffer = BodyPublishers.ofByteArray(byteArr.toByteArray)
        sendRhsClassSchema(buffer)
      }
    }

}
