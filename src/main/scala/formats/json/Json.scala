package formats.json

import formats.common.MessageWR
import org.json4s.DefaultFormats
import org.json4s.native.Serialization._

sealed class Json extends MessageWR {

  implicit val formats = DefaultFormats

  override def serialize[T: Manifest](value: T): String = write(value)

  override def deserialize[T: Manifest](value: String): T = read[T](value)

}

object Json {

  def apply(): Json = new Json()

}