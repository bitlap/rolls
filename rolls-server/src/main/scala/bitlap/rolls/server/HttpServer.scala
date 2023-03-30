package bitlap.rolls.server

import com.sun.net.httpserver.{ HttpExchange, HttpHandler, HttpServer as NHttpServer }

import java.net.InetSocketAddress

/** @author
 *    梦境迷离
 *  @version 1.0,2023/3/20
 */
object HttpServer extends App {

  /** in sbt
   *
   *  > sbt
   *
   *  > rolls-server/runMain bitlap.rolls.server.HttpServer
   */
  private lazy val server = NHttpServer.create(new InetSocketAddress(Configs.port), 0)

  server.createContext("/rolls-mapping", new ResolveHandler())
  server.createContext("/rolls-doc", new PostSchemaHandler)
  server.createContext("/rolls-schema", new QuerySchemaHandler)

  def stop = server.stop(3)

  def start = {
    println("""/\.__  /\        
              |______)/|  |_)/  ______
              |\_  __ \|  |  \ /  ___/
              | |  | \/|   Y  \\___ \ 
              | |__|   |___|  /____  >
              |             \/     \/ """.stripMargin)
    println(s"started at port: ${Configs.port}")
    server.start()
  }

  HttpServer.start
}
