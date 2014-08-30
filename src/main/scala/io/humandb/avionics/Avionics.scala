package io.humandb.avionics

import akka.actor.{Props, Actor, ActorRef, ActorSystem}
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.Await
import scala.concurrent.duration._

// The futures created by the ask syntax need an execution context on which to run, and we will use
// the default global instance for that context
import scala.concurrent.ExecutionContext.Implicits.global

object Avionics {
  // needed for '?' below
  implicit val timeout = Timeout(5.seconds)
  val system = ActorSystem("PlaneSimulation")
  val plane  = system.actorOf(Props[Plane], "Plane")

  def main(args: Array[String]): Unit = {
    // Grab the controls
    val controls = Await.result((plane ? Plane.GiveMeControl).mapTo[ActorRef], 5.seconds)

    // Take off!!
    system.scheduler.scheduleOnce(200.millis) {
      controls ! ControlSurfaces.StickBack(1f)
    }

    // Level out
    system.scheduler.scheduleOnce(1.seconds) {
      controls ! ControlSurfaces.StickBack(0f)
    }

    // Climb
    system.scheduler.scheduleOnce(3.seconds) {
      controls ! ControlSurfaces.StickBack(0.5f)
    }

    // Level out
    system.scheduler.scheduleOnce(4.seconds) {
      controls ! ControlSurfaces.StickBack(0)
    }

    // Shut down
    system.scheduler.scheduleOnce(5.seconds) {
      system.shutdown()
    }
  }
}