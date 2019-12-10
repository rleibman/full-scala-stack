package api

import java.time.LocalDateTime

import akka.http.scaladsl.server.directives.DebuggingDirectives
import akka.http.scaladsl.server.{ Route, _ }
import zio.DefaultRuntime
import zioslick.SlickZIO

import scala.concurrent.ExecutionContext

abstract class MealORamaService extends Directives with ZIODirectives with LiveEnvironment {
  private val runtime = new DefaultRuntime() {}

  val route: Route = DebuggingDirectives.logRequest("Request") {
    path("helloWorld") {
      complete {
        val zio: SlickZIO[String] = for {
          count <- recipeDAO.count
        } yield s"Yay! Count: $count at ${LocalDateTime.now}"
        zio.provide(this)
      }
    }
  }
}
