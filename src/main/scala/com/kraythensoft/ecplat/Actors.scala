package com.kraythensoft.ecplat

import akka.actor.{Props, ActorSystem}
import akka.event.Logging
import akka.camel.{CamelExtension, Consumer, CamelMessage}
import scala.concurrent.duration._
import org.apache.camel.component.jetty.JettyHttpComponent


/** A general actor that will respond to simple text messages. */
class MainActor extends Consumer {
  val log = Logging(context.system, this)

  def endpointUri = "jetty:http://localhost:8877/ecplat"

  def receive = {
    case msg: CamelMessage => {
      log.info("Recieved Message from user: " + msg.toString())
      sender ! ("You sent me the following data: %s" format msg.bodyAs[String])
    }
  }
}

/** Application that runs the server. */
object Main extends App {
  val system = ActorSystem()
  val camel = CamelExtension(system)
  val jetty = new JettyHttpComponent()
  camel.context.addComponent("jetty", jetty)
  val mainActor = system.actorOf(Props[MainActor], name = "MainActor")
  val activationFuture = camel.activationFutureFor(mainActor)(timeout = 10 seconds, executor = system.dispatcher)
}
