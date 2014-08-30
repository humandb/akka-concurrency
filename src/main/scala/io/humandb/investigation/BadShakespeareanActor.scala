package io.humandb.investigation

import akka.actor.{Actor, Props, ActorSystem}

class BadShakespeareanActor extends Actor {
  def receive = {
    case "Good Morning" =>
      println("Him: Forsooth 'tis the 'morn, but mourneth for thou doest I do!")
    case "You're terrible" =>
      println("Him: Yup")
  }
}

object BadShakespeareanMain {
  val system = ActorSystem("BadShakespearean")
  val actor = system.actorOf(Props[BadShakespeareanActor], "Shake")

  // We'll use this utility method to talk with our Actor
  def send(msg: String) = {
    println(s"Me: $msg")
    actor ! msg
    Thread.sleep(100)
  }

  def main(args: Array[String]): Unit = {
    send("Good Morning")
    send("You're terrible")
    system.shutdown()
  }
}