package brokers.gcp

import akka.kafka.scaladsl.Consumer
import akka.kafka.{ConsumerSettings, Subscriptions}
import akka.stream.scaladsl.Sink
import brokers.common.{ActorInst, Bench, PidExtractor}
import com.typesafe.config.ConfigFactory
import formats.common.ShortMessage
import formats.json.Json
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer

object PubSubConsumer extends App with ActorInst with Bench with PidExtractor {

  println(s"Process started with pid: $pid")

  println("Extract consumer configs")
  val config = ConfigFactory.load().getConfig("akka.kafka.consumer")

  println("Initialize consumer configs")

  println("Start consuming")


}
