package brokers.common

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

trait Bench {

  def timeConsumedOfFuture[A](fn: => Future[A]) = {
    val startTime = System.currentTimeMillis()
    Await.result(fn, Duration.Inf)
    System.currentTimeMillis() - startTime
  }

  def timeConsumed[A](fn: => A) = {
    val startTime = System.currentTimeMillis()
    fn
    System.currentTimeMillis() - startTime
  }

}
