package api

import java.time.LocalDateTime

import akka.http.scaladsl.server.directives.DebuggingDirectives
import akka.http.scaladsl.server.{Route, _}
import model.SampleModelObject
import zio.{DefaultRuntime, ZIO}
import zioslick.SlickZIO
import de.heikoseeberger.akkahttpupickle.UpickleSupport

import scala.concurrent.ExecutionContext

abstract class FullStackScalaService extends Directives  with LiveEnvironment with UpickleSupport with ModelPickler with ZIODirectives {
  private val runtime = new DefaultRuntime() {}

  val route: Route = DebuggingDirectives.logRequest("Request") {
    path("helloWorld") {
      complete {
        val zio: SlickZIO[String] = for {
          count <- modelDAO.count
        } yield s"Yay! Count: $count at ${LocalDateTime.now}"
        zio.provide(this)
      }
    } ~
      path("sampleModelObjects") {
        complete(modelDAO.sampleModelObjects())
      } ~
    pathPrefix("sampleModelObject") {
      (post | put) {
        entity(as[SampleModelObject]) { obj =>
          complete(modelDAO.upsert(obj))
        }
      } ~
      path(".*".r) { id =>
        get {
          complete {
            modelDAO.sampleModelObject(id.toInt)
          }
        } ~
          delete {
            complete {
              for {
                modelOpt <- modelDAO.sampleModelObject(id.toInt)
                deleted   <- ZIO.traverse(modelOpt.toSeq)(modelDAO.delete)
              } yield deleted

            }
          }
      }
    }
  }
}
