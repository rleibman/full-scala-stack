package api

import akka.http.scaladsl.testkit.ScalatestRouteTest
import dao.{MockModelDAO, ModelDAO}
import model.SampleModelObject
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import util.ModelPickler
import zio.{IO, ZIO}
import zioslick.RepositoryException
import upickle.default._

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
    with ModelPickler {

  val objects = Seq(
    SampleModelObject(0, "Zero"),
    SampleModelObject(1, "One"),
    SampleModelObject(2, "Two"),
    SampleModelObject(3, "Three"),
    SampleModelObject(4, "Four"),
  )

  val service = new ModelService with MockModelDAO {
    override def modelDAO: ModelDAO.Service = new MockDAO {
      override def sampleModelObjects(): IO[RepositoryException, Seq[SampleModelObject]] = ZIO.succeed(objects)
    }

    override implicit val dbExecutionContext: ExecutionContext = ExecutionContext.Implicits.global
  }

  //TODO test your route here, we would probably not have a test like the one below in reality, since it's super simple.
  "The Service" should  {
    "return some objects on a get" in {
      Get("/sampleModelObjects") ~> service.modelRoute ~> check {
        val res = read[Seq[SampleModelObject]](responseAs[String])

        println(res)
        res shouldEqual objects
      }
    }
  }

}
