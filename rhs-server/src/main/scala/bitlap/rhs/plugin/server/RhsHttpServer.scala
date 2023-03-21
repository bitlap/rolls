package bitlap.rhs.plugin.server

import com.sun.net.httpserver.{ HttpExchange, HttpHandler, HttpServer }

import java.net.InetSocketAddress

/** @author
 *    梦境迷离
 *  @version 1.0,2023/3/20
 */
object RhsHttpServer extends App {

  private lazy val server = HttpServer.create(new InetSocketAddress(ConfigUtils.port), 0)

  server.createContext("/rhs-mapping", new RhsResolveHandler())
  server.createContext("/rhs-doc", new RhsDocHandler)

  def stop = server.stop(3)

  def start = {
    println("""/\.__  /\        
              |______)/|  |_)/  ______
              |\_  __ \|  |  \ /  ___/
              | |  | \/|   Y  \\___ \ 
              | |__|   |___|  /____  >
              |             \/     \/ """.stripMargin)
    println(s"started at port: ${ConfigUtils.port}")
    server.start()
  }

  RhsHttpServer.start
}
