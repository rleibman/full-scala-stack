package dao

import api.LiveEnvironment
import zio.console._
import zio.test.{Assertion, _}

object ModelDAOIntegrationSpec extends LiveEnvironment {

  val modelDAOSuite = suite("ModelDAO Suite")(
    testM("sampleModelObjects returns some objects") {
      assertM(repository.sampleModelObjectOps.search(None)(""), Assertion.isNonEmpty)
    },
    testM("Another way of testing the same") {
      for {
        objects <- repository.sampleModelObjectOps.search(None)("")
        _ <- putStrLn("Hey, this happened")
      } yield {
        assert(objects, Assertion.isNonEmpty)
      }
    }
  )
}

object AllSuites extends DefaultRunnableSpec(ModelDAOIntegrationSpec.modelDAOSuite)
