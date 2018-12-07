package brokers.aws

import akka.stream.alpakka.sqs.scaladsl.SqsSource
import akka.stream.scaladsl.Sink
import brokers.common.{ActorInst, Bench, PidExtractor}
import com.amazonaws.auth.{AWSStaticCredentialsProvider, BasicAWSCredentials}
import com.amazonaws.services.sqs.{AmazonSQSAsync, AmazonSQSAsyncClientBuilder}
import com.typesafe.config.ConfigFactory
import formats.common.{LargeMessage, ShortMessage}
import formats.json.Json
import formats.plain.PlainText
import formats.xml.Xml

object SqsConsumer extends App with ActorInst with Bench with PidExtractor {

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

  println("Start consuming")
//  SqsSource(s"${endpoint}plaintext-short").map { it =>
//    timeConsumed("mapping-short-plaintext", PlainText().deserialize[ShortMessage](it.getBody))
//  }.runWith(Sink.ignore)
//
  SqsSource(s"${endpoint}plaintext-large").map { it =>
    timeConsumed("mapping-large-plaintext", PlainText().deserialize[LargeMessage](it.getBody))
  }.runWith(Sink.ignore)
//
//  SqsSource(s"${endpoint}json-short").map { it =>
//    timeConsumed("mapping-short-json", Json().deserialize[ShortMessage](it.getBody))
//  }.runWith(Sink.ignore)
//
//  SqsSource(s"${endpoint}json-large").map { it =>
//    timeConsumed("mapping-large-json", Json().deserialize[LargeMessage](it.getBody))
//  }.runWith(Sink.ignore)
//
//  SqsSource(s"${endpoint}xml-short").map { it =>
//    timeConsumed("mapping-short-xml", Xml().deserialize[ShortMessage](it.getBody))
//  }.runWith(Sink.ignore)
//
//  SqsSource(s"${endpoint}xml-large").map { it =>
//    timeConsumed("mapping-large-xml", Xml().deserialize[LargeMessage](it.getBody))
//  }.runWith(Sink.ignore)

}
