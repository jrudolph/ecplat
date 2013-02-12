package com.kraythensoft.ecplat

import akka.event.Logging
import akka.camel.{CamelExtension, Consumer, CamelMessage}
import org.apache.camel.component.file.FileComponent
import spray.can.server.SprayCanHttpServerApp
import spray.json.DefaultJsonProtocol._
import scala.concurrent.duration._
import akka.actor._
import spray.http._
import spray.json._

class ReplyData(val name: String)

/** A general actor that will respond to simple text messages. */
class CamelFileActor extends Consumer {
  val log = Logging(context.system, this)

  /** TODO Configure this with some property, maybe mix in zookeeper */
  def endpointUri = "file:target/data/inbox"

  // old jetty uri "jetty:http://localhost:8877/ecplat"

  def receive = {
    case msg: CamelMessage => {
      log.info("Detected new file: ");
      val headers = msg.getHeaders
      log.info("Headers are: " + headers.toString)
      log.info("Body is: %s" format msg.bodyAs[String])
    }
  }
}

class HTTPRoutingActor extends Actor with spray.util.SprayActorLogging {
  val jsonContentType = new ContentType(MediaTypes.`application/json`, Option(HttpCharsets.`UTF-8`))

  def receive = {
    case HttpRequest(HttpMethods.GET, "/ecplat", headers, _, _) =>
      val headerValues = Map[String, String](headers map { h => (h.name, h.value) }: _*)
      val hdrJSON = headerValues.toJson.prettyPrint.stripMargin
      //val hdrJSON = headers.toJson.prettyPrint.stripMargin
      val response = HttpResponse(
        entity = HttpBody(jsonContentType, hdrJSON)
      )
      sender ! response
    case _: HttpRequest => sender ! HttpResponse(status = StatusCodes.NotFound, entity = "Unknown resource!")
  }
}

/** Application that runs the server. */
object Main extends App with SprayCanHttpServerApp {
  // Start the Akka Main Actor System
  override val system = ActorSystem("Ecplat")

  // Start up the Spray related HTTP Server and related actors
  val httpServerActor = system.actorOf(Props[HTTPRoutingActor], name = "httpRoutingActor")
  httpServerActor ! Bind(interface = "localhost", port = 8080)

  // Start up the Apache Camel system and related actors
  val camel = CamelExtension(system)
  camel.context.addComponent("file", new FileComponent)
  val camelFileActor = system.actorOf(Props[CamelFileActor], name = "CamelFileActor")
  val activationFuture = camel.activationFutureFor(camelFileActor)(timeout = 10 seconds, executor = system.dispatcher)
}
