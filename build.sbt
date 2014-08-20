organization  := "io.humandb"

version       := "0.1"

scalaVersion  := "2.10.4"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

libraryDependencies ++= {
  val akkaV = "2.3.5"
  Seq(
    "com.typesafe.akka" %% "akka-actor"    % akkaV,
    "com.typesafe.akka" %% "akka-testkit"  % akkaV   % "test"
  )
}

