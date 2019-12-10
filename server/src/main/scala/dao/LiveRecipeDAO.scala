package dao

import slick.dbio
import slick.jdbc.MySQLProfile.api._
import zio.IO
import zioslick.{ DatabaseProvider, RepositoryException, SlickZIO, ZioSlickSupport }

import scala.concurrent.ExecutionContext

trait LiveRecipeDAO extends RecipeDAO with ZioSlickSupport with DatabaseProvider { self =>

  implicit val dbExecutionContext: ExecutionContext

  implicit def provideDB[R](zio: SlickZIO[R]): IO[RepositoryException, R] = zio.provide(self)

  override def recipeDAO: RecipeDAO.Service = new RecipeDAO.Service {
    def me: DBIO[Int]                                = sql"SELECT count(*) FROM recipe".as[Int].head
    override def count: IO[RepositoryException, Int] = sql"SELECT count(*) FROM recipe".as[Int].head.provide(self)

    override def report: IO[RepositoryException, String] = {
      val dbio = for {
        recipeCount      <- sql"SELECT count(*) FROM recipe".as[Int].head
        groceryItemCount <- sql"SELECT count(*) FROM GroceryItem".as[Int].head
      } yield {
        s"""Recipes = $recipeCount
           |GroceryItems = $groceryItemCount
           |""".stripMargin
      }
      provideDB(dbio)
    }
  }

}
