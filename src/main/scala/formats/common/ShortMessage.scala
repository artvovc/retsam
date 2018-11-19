package formats.common

import org.joda.time.DateTime
import io.jvm.uuid._
import scala.xml._

case class ShortMessage(
  uuid: String = UUID.random.string,
  message: String = s"Time: ${DateTime.now}",
  time: String = DateTime.now.toString
)

object ShortMessage{
  def apply(count: Int): List[ShortMessage] = List.fill(count)(ShortMessage())

  def toXml(it: ShortMessage) =
    <ShortMessage>
      <uuid>{it.uuid}</uuid>
      <message>{it.message}</message>
      <time>{it.time}</time>
    </ShortMessage>

  def fromXml(it: String) = {
    val itxml = XML.loadString(it)
    new ShortMessage(
      uuid = (itxml \ "uuid").text,
      message = (itxml \ "message").text,
      time = (itxml \ "time").text,
    )
  }
}