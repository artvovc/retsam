package brokers.gcp

import java.security.spec.PKCS8EncodedKeySpec
import java.security.{KeyFactory, PrivateKey}
import java.util.Base64

import akka.kafka.ProducerSettings
import akka.kafka.scaladsl.Producer
import akka.stream.alpakka.googlecloud.pubsub.{PubSubMessage, PublishRequest}
import akka.stream.alpakka.googlecloud.pubsub.scaladsl.GooglePubSub
import akka.stream.scaladsl.{Sink, Source}
import brokers.common.{ActorInst, Bench, PidExtractor}
import com.typesafe.config.ConfigFactory
import formats.common.{LargeMessage, ShortMessage}
import formats.json.Json
import formats.plain.PlainText
import formats.xml.Xml
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer

import scala.io.StdIn._

object PubSubPublisher extends App with ActorInst with Bench with PidExtractor {

  println(s"Process started with pid: $pid")

  println("Extract producer configs")
  val config = ConfigFactory.load().getConfig("akka.pubsub")
  val projectId = config.getString("project-id")
  val apiKey = config.getString("api-key")
  val clientEmail = config.getString("client-email")
  val privateKey: PrivateKey = {
    val pk = config.getString("private-key")
    val kf = KeyFactory.getInstance("RSA")
    val encodedPv = Base64.getDecoder.decode(pk)
    val keySpecPv = new PKCS8EncodedKeySpec(encodedPv)
    kf.generatePrivate(keySpecPv)
  }

  println("Initialize producer configs")

  var length = 0

  do {
    print("\nIntroduce length: ")
    length = readInt()

    println("Generate messages")
    val recordsShort = ShortMessage(length)
    val recordsLarge = LargeMessage(length)

    println("Map messages to kafka record")
//    val mappedMessages = timeConsumed("mapping-short-plaintext", recordsShort.map(it => PublishRequest.apply(PubSubMessage.create(data = PlainText().serialize(it), messageId = it.uuid)::Nil)))
//    val mappedMessages = timeConsumed("mapping-large-plaintext", recordsLarge.map(it => PublishRequest.apply(PubSubMessage.create(data = PlainText().serialize(it), messageId = it.uuid)::Nil)))
//    val mappedMessages = timeConsumed("mapping-short-json", recordsShort.map(it => PublishRequest.apply(PubSubMessage.create(data = Json().serialize(it), messageId = it.uuid)::Nil)))
//    val mappedMessages = timeConsumed("mapping-large-json", recordsLarge.map(it => PublishRequest.apply(PubSubMessage.create(data = Json().serialize(it), messageId = it.uuid)::Nil)))
//    val mappedMessages = timeConsumed("mapping-short-xml", recordsShort.map(it => PublishRequest.apply(PubSubMessage.create(data = Xml().serialize(it), messageId = it.uuid)::Nil)))
//    val mappedMessages = timeConsumed("mapping-large-xml", recordsLarge.map(it => PublishRequest.apply(PubSubMessage.create(data = Xml().serialize(it), messageId = it.uuid)::Nil)))

    println("Start core process and count elapsed time")
//    timeConsumedOfFuture(s"pubsub-producer-plaintext-short-$length", Source(mappedMessages)
//      .via(GooglePubSub.publish(projectId, apiKey, clientEmail, privateKey, "plaintext-short"))
//      .runWith(Sink.ignore))
//
//    timeConsumedOfFuture(s"pubsub-producer-plaintext-large-$length", Source(mappedMessages)
//      .via(GooglePubSub.publish(projectId, apiKey, clientEmail, privateKey, "plaintext-large"))
//      .runWith(Sink.ignore))
//
//    timeConsumedOfFuture(s"pubsub-producer-json-short-$length", Source(mappedMessages)
//      .via(GooglePubSub.publish(projectId, apiKey, clientEmail, privateKey, "json-short"))
//      .runWith(Sink.ignore))
//
//    timeConsumedOfFuture(s"pubsub-producer-json-large-$length", Source(mappedMessages)
//      .via(GooglePubSub.publish(projectId, apiKey, clientEmail, privateKey, "json-large"))
//      .runWith(Sink.ignore))
//
//    timeConsumedOfFuture(s"pubsub-producer-xml-short-$length", Source(mappedMessages)
//      .via(GooglePubSub.publish(projectId, apiKey, clientEmail, privateKey, "xml-short"))
//      .runWith(Sink.ignore))
//
//    timeConsumedOfFuture(s"pubsub-producer-xml-large-$length", Source(mappedMessages)
//      .via(GooglePubSub.publish(projectId, apiKey, clientEmail, privateKey, "xml-large"))
//      .runWith(Sink.ignore))

  } while (length > 0)

  println("Shout down materializer and actor")
  terminate

  println("Shout down jvm process")
  System.exit(0)

}
