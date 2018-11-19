package brokers.common

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

trait Bench {

  def timeConsumedOfFuture[A](`type`: String = "undefined", fn: => Future[A]) = {
    val startTime = System.currentTimeMillis()
    val result = Await.result(fn, Duration.Inf)
    println(s"For ${`type`} time elapsed: ${System.currentTimeMillis() - startTime}")
    result
  }

  def timeConsumed[A](`type`: String = "undefined", fn: => A) = {
    val startTime = System.currentTimeMillis()
    val result = fn
    System.currentTimeMillis() - startTime
    println(s"For ${`type`} time elapsed: ${System.currentTimeMillis() - startTime}")
    result
  }

}
