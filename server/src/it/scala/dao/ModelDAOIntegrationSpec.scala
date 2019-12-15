package dao

import api.LiveEnvironment
import zio.console._
import zio.test.{Assertion, _}

import scala.concurrent.ExecutionContext

object ModelDAOIntegrationSpec extends LiveEnvironment {
  override implicit val dbExecutionContext: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global

  val modelDAOSuite = suite("ModelDAO Suite")(
    testM("sampleModelObjects returns some objects") {
      assertM(modelDAO.sampleModelObjects(), Assertion.isNonEmpty)
    },
    testM("Another way of testing the same") {
      for {
        objects <- modelDAO.sampleModelObjects()
        _ <- putStrLn("Hey, this happened")
      } yield {
        assert(objects, Assertion.isNonEmpty)
      }
    }
  )
}

object AllSuites extends DefaultRunnableSpec(ModelDAOIntegrationSpec.modelDAOSuite)


