package brokers.common

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

import scala.concurrent.{ExecutionContext, _}
import scala.concurrent.duration._

trait ActorInst {

  implicit def actorSystem = ActorSystem("master")

  implicit val executionContext: ExecutionContext = actorSystem.dispatcher

  implicit val materializer: ActorMaterializer = ActorMaterializer()

  def terminate =
    Await.ready({
      materializer.shutdown()
      actorSystem.terminate()
    }, Duration.Inf)

}
