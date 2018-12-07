package brokers.gcp

import java.security.spec.PKCS8EncodedKeySpec
import java.security.{KeyFactory, PrivateKey}
import java.util.Base64

import akka.stream.alpakka.googlecloud.pubsub.AcknowledgeRequest
import akka.stream.alpakka.googlecloud.pubsub.scaladsl.GooglePubSub
import brokers.common.{ActorInst, Bench, PidExtractor}
import com.typesafe.config.ConfigFactory
import formats.common.{LargeMessage, ShortMessage}
import formats.json.Json
import formats.plain.PlainText
import formats.xml.Xml

object PubSubConsumer extends App with ActorInst with Bench with PidExtractor {

  println(s"Process started with pid: $pid")

  println("Extract consumer configs")
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

  println("Initialize consumer configs")

  println("Start consuming")
  GooglePubSub.subscribe(projectId, apiKey, clientEmail, privateKey, "consumer-plaintext-short").map { it =>
    timeConsumed("mapping-short-plaintext", PlainText().deserialize[ShortMessage](it.message.data))
    AcknowledgeRequest(it.ackId::Nil)
  }.runWith(GooglePubSub.acknowledge(projectId, apiKey, clientEmail, privateKey, "consumer-plaintext-short"))

  GooglePubSub.subscribe(projectId, apiKey, clientEmail, privateKey, "consumer-plaintext-large").map { it =>
    timeConsumed("mapping-large-plaintext", PlainText().deserialize[LargeMessage](it.message.data))
    AcknowledgeRequest(it.ackId::Nil)
  }.runWith(GooglePubSub.acknowledge(projectId, apiKey, clientEmail, privateKey, "consumer-plaintext-large"))

  GooglePubSub.subscribe(projectId, apiKey, clientEmail, privateKey, "consumer-plaintext-short").map { it =>
    timeConsumed("mapping-short-json", Json().deserialize[ShortMessage](it.message.data))
    AcknowledgeRequest(it.ackId::Nil)
  }.runWith(GooglePubSub.acknowledge(projectId, apiKey, clientEmail, privateKey, "consumer-plaintext-short"))

  GooglePubSub.subscribe(projectId, apiKey, clientEmail, privateKey, "consumer-plaintext-large").map { it =>
    timeConsumed("mapping-large-json", Json().deserialize[LargeMessage](it.message.data))
    AcknowledgeRequest(it.ackId::Nil)
  }.runWith(GooglePubSub.acknowledge(projectId, apiKey, clientEmail, privateKey, "consumer-plaintext-large"))

  GooglePubSub.subscribe(projectId, apiKey, clientEmail, privateKey, "consumer-plaintext-short").map { it =>
    timeConsumed("mapping-short-xml", Xml().deserialize[ShortMessage](it.message.data))
    AcknowledgeRequest(it.ackId::Nil)
  }.runWith(GooglePubSub.acknowledge(projectId, apiKey, clientEmail, privateKey, "consumer-plaintext-short"))

  GooglePubSub.subscribe(projectId, apiKey, clientEmail, privateKey, "consumer-plaintext-large").map { it =>
    timeConsumed("mapping-large-xml", Xml().deserialize[LargeMessage](it.message.data))
    AcknowledgeRequest(it.ackId::Nil)
  }.runWith(GooglePubSub.acknowledge(projectId, apiKey, clientEmail, privateKey, "consumer-plaintext-large"))
}
