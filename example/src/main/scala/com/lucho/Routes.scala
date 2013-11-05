package com.lucho

import spray.routing.Directives
import akka.actor.ActorSystem
import akka.util.Timeout
import scala.concurrent.duration._
import org.slf4j.LoggerFactory
import Json4sJacksonSupport._
import Dyn._

case class SampleCaseClass(address: String, postalCode: Int, city: String)

trait Routes extends Directives {

  val log = LoggerFactory.getLogger(classOf[Routes])

  implicit val timeout = Timeout(90.seconds)

  implicit val system: ActorSystem

  def routes: spray.routing.Route = {
    get {
      complete {
        val d = new DynObject()
        d.name = "Bruce"
        d.name = 3
        d.place.latitude = 35
        d.place.longitude = 35
        d.firstArray = Array[String]("a", "b", "c")
        d.secondArray = List[Int](1, 2, 3)
        d.location = SampleCaseClass("123 Fake St", 1000, "Springfield")
        d
      }
    }
  }


}

