package bitlap.rolls.plugin.server

import java.net.InetSocketAddress

import com.sun.net.httpserver.{ HttpExchange, HttpHandler, HttpServer as NHttpServer }

/** @author
 *    梦境迷离
 *  @version 1.0,2023/3/20
 */
object HttpServer extends App {

  val port = 18000

  /** in sbt
   *
   *  > sbt
   *
   *  > rolls-server/runMain bitlap.rolls.server.HttpServer
   */
  private lazy val server = NHttpServer.create(new InetSocketAddress(port), 0)

  server.createContext("/rolls-doc", new PostSchemaHandler)
  server.createContext("/rolls-schema", new QuerySchemaHandler)

  def stop = server.stop(3)

  def start = {
    println("""
              |              .__  .__          
              |_______  ____ |  | |  |   ______
              |\_  __ \/  _ \|  | |  |  /  ___/
              | |  | \(  <_> )  |_|  |__\___ \ 
              | |__|   \____/|____/____/____  >
              |                             \/ 
              |""".stripMargin)
    println(s"started at port: $port")
    server.start()
  }

  HttpServer.start
}
