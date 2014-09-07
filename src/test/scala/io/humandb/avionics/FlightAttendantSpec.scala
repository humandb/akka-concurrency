package io.humandb.avionics

import akka.actor.{Props, ActorSystem}
import akka.testkit.{TestActorRef, TestKit, ImplicitSender}
import com.typesafe.config.ConfigFactory
import org.scalatest.WordSpecLike
import org.scalatest.Matchers

object TestFlightAttendant {
  def apply() = new FlightAttendant with AttendantResponsiveness {
    val maxResponseTimeMS = 1
  }
}

class FlightAttendantSpec extends TestKit(ActorSystem("FlightAttendantSpec",
                                            ConfigFactory.parseString("akka.scheduler.tick-duration = 1ms")
                                          ))
                          with ImplicitSender
                          with WordSpecLike
                          with Matchers {
  import FlightAttendant._

  "FlightAttendant" should {
    "get a drink when asked" in {
      val a = TestActorRef(Props(TestFlightAttendant()))
      a ! GetDrink("Soda")
      expectMsg(Drink("Soda"))
    }
  }

}
