package brokers.gcp

import akka.kafka.ProducerSettings
import akka.kafka.scaladsl.Producer
import akka.stream.scaladsl.Source
import brokers.common.{ActorInst, Bench, PidExtractor}
import com.typesafe.config.ConfigFactory
import formats.common.{LargeMessage, ShortMessage}
import formats.json.Json
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer

import scala.io.StdIn._

object PubSubPublisher extends App with ActorInst with Bench with PidExtractor {

  println(s"Process started with pid: $pid")

  println("Extract producer configs")
  val config = ConfigFactory.load().getConfig("akka.pubsub")

  println("Initialize producer configs")
  val producerSettings = ProducerSettings.apply(config, new StringSerializer, new StringSerializer).withBootstrapServers("localhost:9092")

  var length = 0

  do {
    print("\nIntroduce length: ")
    length = readInt()

    println("Generate messages")
    val recordsShort = ShortMessage(length)
    val recordsLarge = LargeMessage(length)

    println("Map messages to kafka record")
    val mappedShortJson = timeConsumed("mapping-short-json", recordsShort.map(it => new ProducerRecord("short_json", it.uuid, Json().serialize(it))))

    println("Start core process and count elapsed time")
    timeConsumedOfFuture(s"kafka-producer-json-short-$length", Source(mappedShortJson).runWith(Producer.plainSink(producerSettings)))

  } while (length > 0)

  println("Shout down materializer and actor")
  terminate

  println("Shout down jvm process")
  System.exit(0)

}
