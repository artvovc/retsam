package formats.common

import io.jvm.uuid._
import org.joda.time.DateTime

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

object LargeMessage {
  def apply(count: Int): List[LargeMessage] = {
    val messageLarge = "abcd" * 4096
    val messageLargeNested = "abcd" * 512
    val ints = List.range(1, 5000)
    List.fill(count){
      LargeMessage(messageLarge = messageLarge, messageArray = ints, messageObject = NestedMessage(messageLargeNested))
    }
  }
}