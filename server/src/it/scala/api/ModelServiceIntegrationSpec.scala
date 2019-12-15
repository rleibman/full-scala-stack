package api

import akka.actor.ActorSystem
import akka.actor.testkit.typed.javadsl.ActorTestKit
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.testkit.TestKit
import dao.{MockModelDAO, ModelDAO}
import model.SampleModelObject
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.{AnyWordSpec, AnyWordSpecLike}
import upickle.default._
import util.ModelPickler
import zio.{IO, ZIO}
import zioslick.RepositoryException

import scala.concurrent.ExecutionContext

/**
 * A set of integration tests of the ModelService.
 * Note that these go against a live database, so be careful that you don't point it at production.
 */
class ModelServiceIntegrationSpec
  extends AnyWordSpecLike
    with Matchers
    with ScalatestRouteTest
    with ZIODirectives
    with ModelPickler
    {

  val service = new ModelService with LiveEnvironment {
    override implicit val dbExecutionContext: ExecutionContext = system.dispatcher
  }

  //TODO test your route here, we would probably not have a test like the one below in reality, since it's super simple.
  "The Service" should  {
    "return some objects on a get" in {
      Get("/sampleModelObjects") ~> service.modelRoute ~> check {
        val res = read[Seq[SampleModelObject]](responseAs[String])

        println(res)
        assert(res.nonEmpty)
      }
    }
  }

}
