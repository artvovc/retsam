package formats.plain

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, ObjectInputStream, ObjectOutputStream}
import java.nio.charset.StandardCharsets.UTF_8
import java.util.Base64

sealed class PlainText extends formats.common.MessageWR {

  override def serialize[T: Manifest](value: T): String = {
    val stream: ByteArrayOutputStream = new ByteArrayOutputStream()
    val oos = new ObjectOutputStream(stream)
    oos.writeObject(value)
    oos.close()
    new String(
      Base64.getEncoder.encode(stream.toByteArray),
      UTF_8
    )
  }

  override def deserialize[T: Manifest](value: String): T = {
    val bytes = Base64.getDecoder.decode(value.getBytes(UTF_8))
    val ois = new ObjectInputStream(new ByteArrayInputStream(bytes))
    val result = ois.readObject.asInstanceOf[T]
    ois.close()
    result
  }

}

object PlainText {

  def apply(): PlainText = new PlainText()

}
