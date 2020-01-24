package api

import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model.{ContentType, ContentTypes, HttpEntity, HttpResponse, MessageEntity, RequestEntity}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import dao.{CRUDOperations, MockRepository, Repository}
import de.heikoseeberger.akkahttpupickle.UpickleSupport
import mail.{CourierPostman, Postman}
import model.{SampleModelObject, SimpleSearch}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import routes.{ModelRoutes, SampleModelObjectRoute}
import upickle.default._
import util.ModelPickler
import zio.{IO, ZIO}
import zioslick.RepositoryException

import scala.concurrent.ExecutionContext

/**
 * A set of tests of the ModelService.
 * Note that we have a mock database behind it, so it's only testing business logic in the service
 * without testing the database, if you want to test both, you should have an integration test
 */
class ModelServiceSpec
  extends AnyWordSpec
    with Matchers
    with ScalatestRouteTest
    with ZIODirectives
  with UpickleSupport
    with ModelPickler
 {

  val objects = Seq(
    SampleModelObject(0, "Zero"),
    SampleModelObject(1, "One"),
    SampleModelObject(2, "Two"),
    SampleModelObject(3, "Three"),
    SampleModelObject(4, "Four"),
  )

  val service = new SampleModelObjectRoute  with MockRepository with CourierPostman with Config {
    override def repository: Repository.Service = new Repository.Service {
      override val sampleModelObjectOps: CRUDOperations[SampleModelObject, Int, SimpleSearch, Any] = new MockOps {
        override def search(search: Option[SimpleSearch])(
          implicit session: Any
        ): IO[RepositoryException, Seq[SampleModelObject]] = ZIO.succeed(objects)
        override def get(pk: Int)(implicit session: Any): IO[RepositoryException, Option[SampleModelObject]] =
          ZIO.succeed(objects.headOption)

      }
    }
  }

  //TODO test your route here, we would probably not have a test like the one below in reality, since it's super simple.
  "The Service" should  {
    "return one objects on a get" in {
      Get("/sampleModelObject/1") ~> service.crudRoute.route("") ~> check {
        val res = responseAs[Seq[SampleModelObject]].headOption

        println(res)
        res shouldEqual objects.headOption
      }
    }
    "return some objects on a search" in {
      Post("/sampleModelObject/search", HttpEntity(ContentTypes.`application/json`, write(SimpleSearch()))) ~> service.crudRoute.route("") ~> check {
        val str = responseAs[ujson.Value]
        val res = responseAs[Seq[SampleModelObject]]

        println(res)
        res shouldEqual objects
      }
    }
  }
}
