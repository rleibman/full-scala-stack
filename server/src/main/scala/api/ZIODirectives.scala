/*
 * Copyright 2019 Roberto Leibman
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package api

import akka.http.scaladsl.marshalling.{ Marshaller, Marshalling }
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.server.{ Directive, Directive1, Route, RouteResult }
import akka.http.scaladsl.marshalling.ToResponseMarshaller
import akka.http.scaladsl.server.directives.RouteDirectives
import akka.http.scaladsl.util.FastFuture._
import zio.{ DefaultRuntime, Task, ZIO }

import scala.concurrent.{ Future, Promise }
import scala.util.{ Failure, Success }

/**
 * A special set of akka-http directives that take ZIOs, run them and marshalls them.
 */
trait ZIODirectives extends DefaultRuntime {
  import RouteDirectives._

  implicit def zioMarshaller[A](
    implicit m1: Marshaller[A, HttpResponse],
    m2: Marshaller[Throwable, HttpResponse]
  ): Marshaller[Task[A], HttpResponse] =
    Marshaller { implicit ec => a =>
      {
        val r = a.foldM(
          e => Task.fromFuture(implicit ec => m2(e)),
          a => Task.fromFuture(implicit ec => m1(a))
        )

        val p = Promise[List[Marshalling[HttpResponse]]]()

        unsafeRunAsync(r) { exit =>
          exit.fold(e => p.failure(e.squash), s => p.success(s))
        }

        p.future
      }
    }

  private def fromFunction[A, B](f: A => Future[B]): ZIO[A, Throwable, B] =
    for {
      a <- ZIO.fromFunction(f)
      b <- ZIO.fromFuture(_ => a)
    } yield b

  implicit def zioRoute(z: ZIO[Any, Throwable, Route]): Route = ctx => {
    val p = Promise[RouteResult]()

    val f = z.flatMap(r => fromFunction(r)).provide(ctx)

    unsafeRunAsync(f) { exit =>
      exit.fold(e => p.failure(e.squash), s => p.success(s))
    }

    p.future
  }

  /**
   * "Unwraps" a `Task[T]` and runs the inner route when the task has failed
   * with the task's failure exception as an extraction of type `Throwable`.
   * If the task succeeds the request is completed using the values marshaller
   * (This directive therefore requires a marshaller for the task's type to be
   * implicitly available.)
   *
   * @group task
   */
  def zioCompleteOrRecoverWith(magnet: ZIOCompleteOrRecoverWithMagnet): Directive1[Throwable] =
    magnet.directive

}

object ZIODirectives extends ZIODirectives

trait ZIOCompleteOrRecoverWithMagnet {
  def directive: Directive1[Throwable]
}

object ZIOCompleteOrRecoverWithMagnet extends ZIODirectives {
  implicit def apply[T](
    task: => Task[T]
  )(implicit m: ToResponseMarshaller[T]): ZIOCompleteOrRecoverWithMagnet =
    new ZIOCompleteOrRecoverWithMagnet {
      override val directive: Directive1[Throwable] = Directive[Tuple1[Throwable]] { inner => ctx =>
        val future = unsafeRunToFuture(task)
        import ctx.executionContext
        future.fast.transformWith {
          case Success(res)   => ctx.complete(res)
          case Failure(error) => inner(Tuple1(error))(ctx)
        }
      }
    }
}
