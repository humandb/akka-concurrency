package io.humandb.avionics

import akka.actor.Actor
import scala.concurrent.duration._

// This trait allows us to create different Flight Attendants with different levels of responsiveness.
trait AttendantResponsiveness {
  val maxResponseTimeMS: Int
  def responseDuration = scala.util.Random.nextInt(maxResponseTimeMS).millis
}

object FlightAttendant {
  case class GetDrink(drinkName: String)
  case class Drink(drinkName: String)

  // By default we will make attendants that respond within 3 minutes
  def apply() = new FlightAttendant with AttendantResponsiveness{
    val maxResponseTimeMS = 30000
  }
}

class FlightAttendant extends Actor {
  this: AttendantResponsiveness =>
  import FlightAttendant._

  // bring the execution context into implicit scope for the scheduler below
  implicit val ec = context.dispatcher
  def receive = {
    case GetDrink(drinkName) =>
      // We don't respond right away, but use the scheduler to ensure we do eventually
      context.system.scheduler.scheduleOnce(responseDuration, sender, Drink(drinkName))
  }
}