package brokers.aws

import akka.kafka.scaladsl.Consumer
import akka.kafka.{ConsumerSettings, Subscriptions}
import akka.stream.scaladsl.Sink
import brokers.common.{ActorInst, Bench, PidExtractor}
import com.typesafe.config.ConfigFactory
import formats.common.ShortMessage
import formats.json.Json
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer

object SqsConsumer extends App with ActorInst with Bench with PidExtractor {

  println(s"Process started with pid: $pid")

  println("Extract consumer configs")
  val config = ConfigFactory.load().getConfig("akka.kafka.consumer")

  println("Initialize consumer configs")
  val consumerSettings = ConsumerSettings.apply(config, new StringDeserializer, new StringDeserializer)
    .withBootstrapServers("localhost:9092")
    .withGroupId("group")
    .withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest")

  println("Start consuming")
//  Consumer.plainSource(consumerSettings, Subscriptions.topics("short_plaintext")).map(
//    it => timeConsumed("mapping-short-plaintext", PlainText().deserialize[ShortMessage](it.value))
//  ).runWith(Sink.ignore)

  Consumer.plainSource(consumerSettings, Subscriptions.topics("short_json")).map(
    it => timeConsumed("mapping-short-json", Json().deserialize[ShortMessage](it.value))
  ).runWith(Sink.ignore)

//  Consumer.plainSource(consumerSettings, Subscriptions.topics("short_xml")).map(
//    it => timeConsumed("mapping-short-xml", Xml().deserialize[ShortMessage](it.value))
//  ).runWith(Sink.ignore)
//
//  Consumer.plainSource(consumerSettings, Subscriptions.topics("large_plaintext")).map(
//    it => timeConsumed("mapping-large-plaintext", PlainText().deserialize[ShortMessage](it.value))
//  ).runWith(Sink.ignore)
//
//  Consumer.plainSource(consumerSettings, Subscriptions.topics("large_json")).map(
//    it => timeConsumed("mapping-large-json", Json().deserialize[ShortMessage](it.value))
//  ).runWith(Sink.ignore)
//
//  Consumer.plainSource(consumerSettings, Subscriptions.topics("large_xml")).map(
//    it => timeConsumed("mapping-large-xml", Xml().deserialize[ShortMessage](it.value))
//  ).runWith(Sink.ignore)

}
