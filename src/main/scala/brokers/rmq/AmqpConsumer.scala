package brokers.rmq

import akka.stream.alpakka.amqp._
import akka.stream.alpakka.amqp.scaladsl.AmqpSource
import akka.stream.scaladsl.Sink
import brokers.common.{ActorInst, Bench, PidExtractor}
import com.typesafe.config.ConfigFactory
import formats.common.{LargeMessage, ShortMessage}
import formats.json.Json
import formats.plain.PlainText
import formats.xml.Xml

object AmqpConsumer extends App with ActorInst with Bench with PidExtractor {

  println(s"Process started with pid: $pid")

  println("Extract producer configs")
  val config = ConfigFactory.load().getConfig("akka.amqp")
  val host = config.getString("host")
  val port = config.getInt("port")
  val username = config.getString("user")
  val password = config.getString("password")
  val virtualHost = config.getString("virtualHost")
  val exchange = config.getString("exchange")

  println("Initialize consumer configs")
  val bufferSize = 100000
  val connection = AmqpDetailsConnectionProvider.create(host, port)
    .withCredentials(AmqpCredentials(username, password))
    .withVirtualHost(virtualHost)

  println("Start consuming")
//  AmqpSource.committableSource(NamedQueueSourceSettings.create(connection, "master-queue-short-plaintext"), bufferSize).map { it =>
//    timeConsumed("mapping-short-plaintext", PlainText().deserialize[ShortMessage](it.message.bytes.utf8String))
//    it.ack()
//  }.runWith(Sink.ignore)
//
  AmqpSource.committableSource(NamedQueueSourceSettings.create(connection, "master-queue-long-plaintext"), bufferSize).map { it =>
    timeConsumed("mapping-large-plaintext", PlainText().deserialize[LargeMessage](it.message.bytes.utf8String))
    it.ack()
  }.runWith(Sink.ignore)
//
//  AmqpSource.committableSource(NamedQueueSourceSettings.create(connection, "master-queue-short-json"), bufferSize).map { it =>
//    timeConsumed("mapping-short-json", Json().deserialize[ShortMessage](it.message.bytes.utf8String))
//    it.ack()
//  }.runWith(Sink.ignore)
//
//  AmqpSource.committableSource(NamedQueueSourceSettings.create(connection, "master-queue-long-json"), bufferSize).map { it =>
//    timeConsumed("mapping-large-json", Json().deserialize[LargeMessage](it.message.bytes.utf8String))
//    it.ack()
//  }.runWith(Sink.ignore)
//
//  AmqpSource.committableSource(NamedQueueSourceSettings.create(connection, "master-queue-short-xml"), bufferSize).map { it =>
//    timeConsumed("mapping-short-xml", Xml().deserialize[ShortMessage](it.message.bytes.utf8String))
//    it.ack()
//  }.runWith(Sink.ignore)
//
//  AmqpSource.committableSource(NamedQueueSourceSettings.create(connection, "master-queue-long-xml"), bufferSize).map { it =>
//    timeConsumed("mapping-large-xml", Xml().deserialize[LargeMessage](it.message.bytes.utf8String))
//    it.ack()
//  }.runWith(Sink.ignore)

}
