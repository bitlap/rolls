package bitlap.rhs.plugin.server

import com.sun.net.httpserver.{ HttpExchange, HttpHandler, HttpServer }

import java.net.InetSocketAddress

/** @author
 *    梦境迷离
 *  @version 1.0,2023/3/20
 */
object RhsResolveHttpServer {

  private lazy val server = HttpServer.create(new InetSocketAddress(ConfigUtils.port), 0)

  server.createContext("/rhs-mapping", new RhsResolveHandler())

  def stop = server.stop(3)

  def start = {
    println(""".__            
              |_______|  |__   ______
              |\_  __ \  |  \ /  ___/
              | |  | \/   Y  \\___ \ 
              | |__|  |___|  /____  >
              |            \/     \/ """.stripMargin)
    println(s"started at port: ${ConfigUtils.port}")
    server.start()
  }

}
