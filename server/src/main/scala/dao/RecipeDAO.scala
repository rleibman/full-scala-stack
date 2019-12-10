package dao

import zio.{ IO, ZIO }
import zio.macros.annotation.accessible
import zioslick.{ DatabaseProvider, RepositoryException, SlickZIO }

//@accessible
//@mockable
trait RecipeDAO {
  def recipeDAO: RecipeDAO.Service
}

object RecipeDAO {
  trait Service {
    def count: IO[RepositoryException, Int]
    def report: IO[RepositoryException, String]
  }
}
