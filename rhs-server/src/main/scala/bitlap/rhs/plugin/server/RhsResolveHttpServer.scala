package bitlap.rhs.plugin.server

import com.sun.net.httpserver.{ HttpExchange, HttpHandler, HttpServer }

import java.net.InetSocketAddress

/** @author
 *    梦境迷离
 *  @version 1.0,2023/3/20
 */
object RhsResolveHttpServer extends App {

  val server = HttpServer.create(new InetSocketAddress(ConfigUtils.port), 0)
  println(""".__    .___                              .__                
            ||__| __| _/   _____ _____  ______ ______ |__| ____    ____  
            ||  |/ __ |   /     \\__  \ \____ \\____ \|  |/    \  / ___\ 
            ||  / /_/ |  |  Y Y  \/ __ \|  |_> >  |_> >  |   |  \/ /_/  >
            ||__\____ |  |__|_|  (____  /   __/|   __/|__|___|  /\___  / 
            |        \/        \/     \/|__|   |__|           \//_____/  """.stripMargin)
  println(s"started at port: ${ConfigUtils.port}")

  server.createContext("/rhs-mapping", new RhsResolveHandler())
  server.start()

}
