package formats.xml

import formats.common.{LargeMessage, MessageWR, ShortMessage}

import scala.xml._

sealed class Xml extends MessageWR {

  override def serialize[T: Manifest](value: T): String = value match {
    case it:ShortMessage => ShortMessage.toXml(it).toString()
    case it:LargeMessage => LargeMessage.toXml(it).toString()
  }

  override def deserialize[T: Manifest](value: String): T = {
    val xml = XML.loadString(value)
    xml.label match {
      case "ShortMessage" => ShortMessage.fromXml(value).asInstanceOf[T]
      case _ => LargeMessage.fromXml(value).asInstanceOf[T]
    }
  }

}

object Xml {

  def apply(): Xml = new Xml()

}