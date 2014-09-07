package io.humandb.avionics

import akka.actor.{Actor, ActorRef, Props}
import io.humandb.avionics.LeadFlightAttendant.GetFlightAttendant
import io.humandb.avionics.{AttendantCreationPolicy, LeadFlightAttendant, LeadFlightAttendantProvider}

// The Lead is going to construct its own subordinates. We'll have a policy to vary that
trait AttendantCreationPolicy {
  // Feel free to make this configurable!
  val numberOfAttendants: Int = 8
  def createAttendant = FlightAttendant()
}

// We'll also provide a mechanism for altering how we create the LeadFlightAttendant
trait LeadFlightAttendantProvider {
  def newLeadFlightAttendant: Actor = LeadFlightAttendant()
}

object LeadFlightAttendant {
  case object GetFlightAttendant
  case class Attendant(a: ActorRef)
  def apply() = new LeadFlightAttendant with AttendantCreationPolicy

}

class LeadFlightAttendant extends Actor {
  this: AttendantCreationPolicy =>

  import LeadFlightAttendant._

  // After we've successfully spooled up the LeadFlightAttendant, we're going to have it create
  // all of its subordinates
  override def preStart() {
    import scala.collection.JavaConverters._
    val attendantNames = context.system.settings.config.getStringList("io.humandb.avionics.flightCrew.attendantNames").asScala
    attendantNames take numberOfAttendants foreach { name =>
      context.actorOf(Props(createAttendant), name)
    }
  }

  // 'children' is an Iterable. This method return a random one.
  def randomAttendant(): ActorRef = {
    context.children.take(scala.util.Random.nextInt(numberOfAttendants) + 1).last
  }

  def receive = {
    case GetFlightAttendant =>
      sender ! Attendant(randomAttendant())
    case m =>
      randomAttendant() forward m
  }
}