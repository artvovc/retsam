package brokers.rmq

import akka.stream.alpakka.amqp
import akka.stream.alpakka.amqp._
import akka.stream.alpakka.amqp.scaladsl.AmqpSink
import akka.stream.scaladsl.Source
import akka.util.ByteString
import brokers.common.{ActorInst, Bench, PidExtractor}
import com.typesafe.config.ConfigFactory
import formats.common.{LargeMessage, ShortMessage}
import formats.json.Json
import formats.plain.PlainText
import formats.xml.Xml

import scala.io.StdIn._

object AmqpPublisher extends App with ActorInst with Bench with PidExtractor {

  println(s"Process started with pid: $pid")

  println("Extract producer configs")
  val config = ConfigFactory.load().getConfig("akka.amqp")
  val host = config.getString("host")
  val port = config.getInt("port")
  val username = config.getString("user")
  val password = config.getString("password")
  val virtualHost = config.getString("virtualHost")
  val exchange = config.getString("exchange")

  println("Initialize producer configs")
  val connection = AmqpDetailsConnectionProvider
    .create(host, port)
    .withCredentials(AmqpCredentials(username, password))
    .withVirtualHost(virtualHost)

  var length = 0

  do {
    print("\nIntroduce length: ")
    length = readInt()

    println("Generate messages")
    val recordsShort = ShortMessage(length)
    val recordsLarge = LargeMessage(length)

    println("Map messages to kafka record")
//    val source = timeConsumed("mapping-short-plaintext", Source(recordsShort).map(it => amqp.OutgoingMessage.create(ByteString(PlainText().serialize(it)), immediate = false, mandatory = false)))
    val source = timeConsumed("mapping-long-plaintext", Source(recordsLarge).map(it => amqp.OutgoingMessage.create(ByteString(PlainText().serialize(it)), immediate = false, mandatory = false)))
//    val source = timeConsumed("mapping-short-json", Source(recordsShort).map(it => amqp.OutgoingMessage.create(ByteString(Json().serialize(it)), immediate = false, mandatory = false)))
//    val source = timeConsumed("mapping-long-json", Source(recordsLarge).map(it => amqp.OutgoingMessage.create(ByteString(Json().serialize(it)), immediate = false, mandatory = false)))
//    val source = timeConsumed("mapping-short-xml", Source(recordsShort).map(it => amqp.OutgoingMessage.create(ByteString(Xml().serialize(it)), immediate = false, mandatory = false)))
//    val source = timeConsumed("mapping-long-xml", Source(recordsLarge).map(it => amqp.OutgoingMessage.create(ByteString(Xml().serialize(it)), immediate = false, mandatory = false)))

    println("Start core process and count elapsed time")
//    timeConsumedOfFuture(s"amqp-producer-plaintext-short-$length", source.runWith(AmqpSink(AmqpSinkSettings(connection).withExchange(s"$exchange-short-plaintext"))))
    timeConsumedOfFuture(s"amqp-producer-plaintext-long-$length", source.runWith(AmqpSink(AmqpSinkSettings(connection).withExchange(s"$exchange-long-plaintext"))))
//    timeConsumedOfFuture(s"amqp-producer-json-short-$length", source.runWith(AmqpSink(AmqpSinkSettings(connection).withExchange(s"$exchange-short-json"))))
//    timeConsumedOfFuture(s"amqp-producer-json-long-$length", source.runWith(AmqpSink(AmqpSinkSettings(connection).withExchange(s"$exchange-long-json"))))
//    timeConsumedOfFuture(s"amqp-producer-xml-short-$length", source.runWith(AmqpSink(AmqpSinkSettings(connection).withExchange(s"$exchange-short-xml"))))
//    timeConsumedOfFuture(s"amqp-producer-xml-long-$length", source.runWith(AmqpSink(AmqpSinkSettings(connection).withExchange(s"$exchange-long-xml"))))
  } while (length > 0)

  println("Shout down materializer and actor")
  terminate

  println("Shout down jvm process")
  System.exit(0)

}
