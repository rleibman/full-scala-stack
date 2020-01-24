package api

import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import de.heikoseeberger.akkahttpupickle.UpickleSupport
import model.{SampleModelObject, SimpleSearch}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.{AnyWordSpec, AnyWordSpecLike}
import routes.ModelRoutes
import upickle.default._
import util.ModelPickler

import scala.concurrent.ExecutionContext

/**
 * A set of integration tests of the ModelService.
 * Note that these go against a live database, so be careful that you don't point it at production.
 */
class ModelServiceIntegrationSpec
  extends AnyWordSpec
    with Matchers
    with ScalatestRouteTest
    with ZIODirectives
  with UpickleSupport
    with ModelPickler
    {

  val service = new ModelRoutes with LiveEnvironment

  //TODO test your route here, we would probably not have a test like the one below in reality, since it's super simple.
      "The Service" should  {
        "return one objects on a get" in {
          Get("/api/sampleModelObject/1") ~> service.apiRoute("") ~> check {
            val res = responseAs[Seq[SampleModelObject]].headOption

            println(res)
            assert(res.nonEmpty)
          }
        }
        "return some objects on a search" in {
          Post("/api/sampleModelObject/search", HttpEntity(ContentTypes.`application/json`, write(SimpleSearch()))) ~> service.apiRoute("") ~> check {
            val str = responseAs[ujson.Value]
            val res = responseAs[Seq[SampleModelObject]]

            println(res)
            assert(res.nonEmpty)
          }
        }
      }

}
