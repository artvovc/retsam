package formats.common

trait MessageWR {

  def serialize[T:Manifest](value: T): String

  def deserialize[T:Manifest](value: String): T

}
