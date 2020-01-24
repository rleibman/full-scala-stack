/*
 * Copyright (c) 2019 Roberto Leibman -- All Rights Reserved
 *
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *
 */

package util

import org.scalatest.Assertion
import org.scalatest.flatspec.AsyncFlatSpecLike
import zio.{Cause, DefaultRuntime, ZIO}

import scala.concurrent.Future

trait ZioTestSupport extends AsyncFlatSpecLike {

  val runtime = new DefaultRuntime {}

  // used to allow pattern matching
  case class ZIOTestException[E](c: Cause[E]) extends RuntimeException(c.toString)

  implicit def toTestFuture2[E <: Throwable](zio: ZIO[Any, E, Assertion]): Future[Assertion] =
    runtime.unsafeRunToFuture(zio)

}
