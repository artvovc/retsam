package formats.common

import org.joda.time.DateTime
import io.jvm.uuid._

case class ShortMessage(
  uuid: String = UUID.random.string,
  message: String = s"Time: ${DateTime.now}",
  time: String = DateTime.now.toString
)

object ShortMessage{
  def apply(count: Int): List[ShortMessage] = List.fill(count)(ShortMessage())
}