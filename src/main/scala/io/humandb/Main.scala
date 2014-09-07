package io.humandb

import akka.actor.{ActorSystem, Props}
import io.humandb.avionics.{LeadFlightAttendant, AttendantCreationPolicy}

object Main {
	def main(args: Array[String]): Unit = {
		val system = akka.actor.ActorSystem("PlaneSimulation")
    val lead = system.actorOf(Props(new LeadFlightAttendant with AttendantCreationPolicy), "paul")
    Thread.sleep(4000)
    system.shutdown()
	}
}
