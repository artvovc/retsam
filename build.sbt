name := "master"

version := "0.1"

scalaVersion := "2.12.6"

libraryDependencies ++= Seq(
  "com.lightbend.akka" %% "akka-stream-alpakka-sqs" % "1.0-M1",
  "com.lightbend.akka" %% "akka-stream-alpakka-google-cloud-pub-sub" % "1.0-M1",
  "com.lightbend.akka" %% "akka-stream-alpakka-amqp" % "1.0-M1",
  "com.typesafe.akka" %% "akka-stream-kafka" % "1.0-M1"
)