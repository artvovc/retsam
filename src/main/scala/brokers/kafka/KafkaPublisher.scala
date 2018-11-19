package brokers.kafka

import akka.kafka.ProducerSettings
import akka.kafka.scaladsl.Producer
import akka.stream.scaladsl.Source
import brokers.common.{ActorInst, Bench, PidExtractor}
import com.typesafe.config.ConfigFactory
import formats.common.{LargeMessage, ShortMessage}
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer

object KafkaPublisher extends App with ActorInst with Bench with PidExtractor {

  println(s"Process started with pid: $pid")

  println("Extract producer configs")
  val config = ConfigFactory.load().getConfig("akka.kafka.producer")

  println("Initialize producer configs")
  val producerSettings = ProducerSettings.apply(config, new StringSerializer, new StringSerializer).withBootstrapServers("localhost:9092")

  println("Generate messages")
  val records = ShortMessage(10000)

  println("Map messages to kafka record")
  val mapped = records.map(it => new ProducerRecord("test", it.uuid, it.toString))

  println("Start core process and count elapsed time")
  val elapsedTime = timeConsumedOfFuture(Source(mapped).runWith(Producer.plainSink(producerSettings)))
  println(s"Time elapsed: $elapsedTime")

  println("Shout down materializer and actor")
  terminate

  println("Shout down jvm process")
  System.exit(0)

}
