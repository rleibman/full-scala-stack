package api

import java.time.LocalDateTime

import akka.http.scaladsl.server.directives.DebuggingDirectives
import akka.http.scaladsl.server.{ Route, _ }
import model.SampleModelObject
import zio.{ DefaultRuntime, ZIO }
import zioslick.{ DatabaseProvider, SlickZIO }
import de.heikoseeberger.akkahttpupickle.UpickleSupport
import _root_.util.ModelPickler
import dao.ModelDAO

import scala.concurrent.ExecutionContext

/**
 * This is the main "service" that concatenates all of the routes of all of the sub-services
 */
abstract class FullStackScalaService
    extends Directives
    with LiveEnvironment
    with UpickleSupport
    with ModelPickler
    with ZIODirectives
    with ModelService
    //TODO If you split your full route into different services, add them here
    with HTMLService {
  private val runtime = new DefaultRuntime() {}

  val route: Route = DebuggingDirectives.logRequest("Request") {
    path("helloWorld") {
      complete {
        for {
          count <- modelDAO.count
        } yield s"Yay! Count: $count at ${LocalDateTime.now}"
      }
    } ~
      modelRoute ~
      htmlRoute
  }
}
