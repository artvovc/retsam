package brokers.aws

import akka.stream.alpakka.sqs.scaladsl.SqsPublishSink
import akka.stream.scaladsl.Source
import brokers.common.{ActorInst, Bench, PidExtractor}
import com.amazonaws.auth.{AWSStaticCredentialsProvider, BasicAWSCredentials}
import com.amazonaws.services.sqs.{AmazonSQSAsync, AmazonSQSAsyncClientBuilder}
import com.amazonaws.services.sqs.model.SendMessageRequest
import com.typesafe.config.ConfigFactory
import formats.common.{LargeMessage, ShortMessage}
import formats.plain.PlainText

import scala.io.StdIn._

object SqsPublisher extends App with ActorInst with Bench with PidExtractor {

  println(s"Process started with pid: $pid")

  println("Extract producer configs")
  val config = ConfigFactory.load().getConfig("akka.sqs")
  val accessKey = config.getString("access-key")
  val secretKey = config.getString("secret-key")
  val endpoint = "https://sqs.eu-west-2.amazonaws.com/359756149454/"

  println("Initialize producer configs")
  val credentialsProvider = new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey))

  implicit val awsSqsClient: AmazonSQSAsync = AmazonSQSAsyncClientBuilder
    .standard()
    .withCredentials(credentialsProvider)
    .withRegion("eu-west-2")
    .build()

  var length = 0

  do {
    print("\nIntroduce length: ")
    length = readInt()

    println("Generate messages")
    val recordsShort = ShortMessage(length)
    val recordsLarge = LargeMessage(length)

    println("Map messages to kafka record")
//    val mappedMessages = timeConsumed("mapping-short-plaintext", recordsShort.map(it => new SendMessageRequest().withMessageBody(PlainText().serialize(it))))
//    val mappedMessages = timeConsumed("mapping-large-plaintext", recordsLarge.map(it => new SendMessageRequest().withMessageBody(PlainText().serialize(it))))
//    val mappedMessages = timeConsumed("mapping-short-json", recordsShort.map(it => new SendMessageRequest().withMessageBody(Json().serialize(it))))
//    val mappedMessages = timeConsumed("mapping-large-json", recordsLarge.map(it => new SendMessageRequest().withMessageBody(Json().serialize(it))))
//    val mappedMessages = timeConsumed("mapping-short-xml", recordsShort.map(it => new SendMessageRequest().withMessageBody(Xml().serialize(it))))
//    val mappedMessages = timeConsumed("mapping-large-xml", recordsLarge.map(it => new SendMessageRequest().withMessageBody(Xml().serialize(it))))

    println("Start core process and count elapsed time")
//    timeConsumedOfFuture(s"sqs-producer-plaintext-short-$length", Source(mappedMessages).runWith(SqsPublishSink.messageSink(s"${endpoint}plaintext-short")))
//    timeConsumedOfFuture(s"sqs-producer-plaintext-large-$length", Source(mappedMessages).runWith(SqsPublishSink.messageSink(s"${endpoint}plaintext-large")))
//    timeConsumedOfFuture(s"sqs-producer-json-short-$length", Source(mappedMessages).runWith(SqsPublishSink.messageSink(s"${endpoint}json-short")))
//    timeConsumedOfFuture(s"sqs-producer-json-large-$length", Source(mappedMessages).runWith(SqsPublishSink.messageSink(s"${endpoint}json-large")))
//    timeConsumedOfFuture(s"sqs-producer-xml-short-$length", Source(mappedMessages).runWith(SqsPublishSink.messageSink(s"${endpoint}xml-short")))
//    timeConsumedOfFuture(s"sqs-producer-xml-large-$length", Source(mappedMessages).runWith(SqsPublishSink.messageSink(s"${endpoint}xml-large")))
  } while (length > 0)

  println("Shout down materializer and actor")
  terminate

  println("Shout down jvm process")
  System.exit(0)

}
