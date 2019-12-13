package api

import akka.http.scaladsl.server.Directives
import dao.ModelDAO
import de.heikoseeberger.akkahttpupickle.UpickleSupport
import model.SampleModelObject
import zio.ZIO

/**
 * Typically you'd have one of this for every model "family"... the idea being that you could eventually
 * move these to their own REST applications.
 */
trait ModelService extends Directives with UpickleSupport with ModelPickler with ZIODirectives {
  this: ModelDAO =>

  val modelRoute = path("sampleModelObjects") {
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
