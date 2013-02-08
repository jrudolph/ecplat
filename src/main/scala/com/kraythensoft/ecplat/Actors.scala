package org.kraythensoft.store

import akka.actor.{Props, ActorSystem, Actor}
import akka.event.Logging

/** A general actor that will respond to simple text messages. */
class MyActor extends Actor {
  val log = Logging(context.system, this);

  def receive = {
    case "test" => log.info("received test")
    case _ => log.info("received unknown message")
  }
}

/** Application that runs the server. */
object Main extends App {
  val system = ActorSystem()
  val myActor = system.actorOf(Props[MyActor], name = "myactor")
  myActor ! "foo"
  myActor ! "test"
  system.shutdown
}
