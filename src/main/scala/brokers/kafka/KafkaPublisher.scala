package brokers.kafka

import akka.kafka.ProducerSettings
import akka.kafka.scaladsl.Producer
import akka.stream.scaladsl.Source
import brokers.common.{ActorInst, Bench, PidExtractor}
import com.typesafe.config.ConfigFactory
import formats.common.{LargeMessage, ShortMessage}
import formats.json.Json
import formats.plain.PlainText
import formats.xml.Xml
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer

import scala.io.StdIn._

object KafkaPublisher extends App with ActorInst with Bench with PidExtractor {

  println(s"Process started with pid: $pid")

  var length = 0

  do {
    print("\nIntroduce length: ")
    length = readInt()

    println("Extract producer configs")
    val config = ConfigFactory.load().getConfig("akka.kafka.producer")

    println("Initialize producer configs")
    val producerSettings = ProducerSettings.apply(config, new StringSerializer, new StringSerializer).withBootstrapServers("localhost:9092")

    println("Generate messages")
    val recordsShort = ShortMessage(length)
    val recordsLarge = LargeMessage(length)

    println("Map messages to kafka record")
    //  val mappedShortPlainText = timeConsumed("mapping-short-plaintext", recordsShort.map(it => new ProducerRecord("short_plaintext", it.uuid, PlainText().serialize(it))))
    val mappedShortJson = timeConsumed("mapping-short-json", recordsShort.map(it => new ProducerRecord("short_json", it.uuid, Json().serialize(it))))
    //  val mappedShortXml = timeConsumed("mapping-short-xml", recordsShort.map(it => new ProducerRecord("short_xml", it.uuid, Xml().serialize(it))))
    //  val mappedLargePlainText = timeConsumed("mapping-large-plaintext", recordsLarge.map(it => new ProducerRecord("large_plaintext", it.uuid, PlainText().serialize(it))))
    //  val mappedLargeJson = timeConsumed("mapping-large-json", recordsLarge.map(it => new ProducerRecord("large_json", it.uuid, Json().serialize(it))))
    //  val mappedLargeXml = timeConsumed("mapping-large-xml", recordsLarge.map(it => new ProducerRecord("large_xml", it.uuid, Xml().serialize(it))))

    println("Start core process and count elapsed time")
    //    timeConsumedOfFuture(s"kafka-producer-plaintext-short-$length", Source(mappedShortPlainText).runWith(Producer.plainSink(producerSettings)))
    timeConsumedOfFuture(s"kafka-producer-json-short-$length", Source(mappedShortJson).runWith(Producer.plainSink(producerSettings)))
    //  timeConsumedOfFuture(s"kafka-producer-xml-short-$length", Source(mappedShortXml).runWith(Producer.plainSink(producerSettings)))
    //
    //  timeConsumedOfFuture(s"kafka-producer-plaintext-large-$length", Source(mappedLargePlainText).runWith(Producer.plainSink(producerSettings)))
    //  timeConsumedOfFuture(s"kafka-producer-json-large-$length", Source(mappedLargeJson).runWith(Producer.plainSink(producerSettings)))
    //  timeConsumedOfFuture(s"kafka-producer-xml-large-$length", Source(mappedLargeXml).runWith(Producer.plainSink(producerSettings)))
  } while (length > 0)

  println("Shout down materializer and actor")
  terminate

  println("Shout down jvm process")
  System.exit(0)

}
