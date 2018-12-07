package brokers.aws

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

object SqsPublisher extends App with ActorInst with Bench with PidExtractor {

  println(s"Process started with pid: $pid")

  println("Extract producer configs")
  val config = ConfigFactory.load().getConfig("akka.kafka.producer")

  println("Initialize producer configs")

  var length = 0

  do {
    print("\nIntroduce length: ")
    length = readInt()

    println("Generate messages")
    val recordsShort = ShortMessage(length)
    val recordsLarge = LargeMessage(length)

    println("Map messages to kafka record")
    //  val mappedShortPlainText = timeConsumed("mapping-short-plaintext", recordsShort.map(it => new ProducerRecord("short_plaintext", it.uuid, PlainText().serialize(it))))

    println("Start core process and count elapsed time")
    //  timeConsumedOfFuture(s"kafka-producer-plaintext-short-$length", Source(mappedShortPlainText).runWith(Producer.plainSink(producerSettings)))
  } while (length > 0)

  println("Shout down materializer and actor")
  terminate

  println("Shout down jvm process")
  System.exit(0)

}
