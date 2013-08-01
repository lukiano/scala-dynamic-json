package com.lucho

import spray.routing.SimpleRoutingApp
import akka.actor.ActorSystem

object Main extends App with SimpleRoutingApp with Routes {

  implicit val system: ActorSystem = ActorSystem("spray-sample-2")

  startServer(interface = "0.0.0.0", port = 8080) {
    routes
  }
}
