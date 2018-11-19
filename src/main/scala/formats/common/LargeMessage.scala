package formats.common

import io.jvm.uuid._
import org.joda.time.DateTime

import scala.xml._

case class LargeMessage(
  messageLarge: String,
  messageObject: NestedMessage,
  messageArray: List[Int],
  uuid: String = UUID.random.string,
  messageShort: String = "abcd",
  time: String = DateTime.now.toString
)

case class NestedMessage(
  messageLarge: String,
  uuid: String = UUID.random.string
)

object NestedMessage {

  def toXml(it: NestedMessage) =
    <NestedMessage>
      <messageLarge>{it.messageLarge}</messageLarge>
      <uuid>{it.uuid}</uuid>
    </NestedMessage>

  def fromXml(it: String) = {
    val itxml = XML.loadString(it)
    new NestedMessage(
      uuid = (itxml \ "uuid").text,
      messageLarge = (itxml \ "messageLarge").text
    )
  }

}

object LargeMessage {

  def apply(count: Int): List[LargeMessage] = {
    val messageLarge = "abcd" * 4096
    val messageLargeNested = "abcd" * 512
    val ints = List.range(1, 5000)
    List.fill(count){
      LargeMessage(messageLarge = messageLarge, messageArray = ints, messageObject = NestedMessage(messageLargeNested))
    }
  }

  def toXml(it: LargeMessage) =
    <LargeMessage>
      <uuid>{it.uuid}</uuid>
      <messageLarge>{it.messageLarge}</messageLarge>
      <messageObject>{NestedMessage.toXml(it.messageObject)}</messageObject>
      <messageArray>{it.messageArray.mkString(",")}</messageArray>
      <messageShort>{it.messageShort}</messageShort>
      <time>{it.time}</time>
    </LargeMessage>

  def fromXml(it: String) = {
    val itxml = XML.loadString(it)
    new LargeMessage(
      uuid = (itxml \ "uuid").text,
      messageLarge = (itxml \ "messageLarge").text,
      messageShort = (itxml \ "messageShort").text,
      messageArray = (itxml \ "messageArray").text.split(",").map(_.toInt).toList,
      messageObject = NestedMessage.fromXml((itxml \ "messageObject" \ "NestedMessage").toString()),
      time = (itxml \ "time").text,
    )
  }
}