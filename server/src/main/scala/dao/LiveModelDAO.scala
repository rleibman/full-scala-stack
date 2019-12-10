package dao

import model.SampleModelObject
import slick.dbio.DBIO
import slick.jdbc.MySQLProfile.api._
import zio.IO
import zioslick.{DatabaseProvider, RepositoryException, ZioSlickSupport}

import scala.concurrent.ExecutionContext

trait LiveModelDAO extends ModelDAO with ZioSlickSupport with DatabaseProvider  {
  self =>

  implicit val dbExecutionContext: ExecutionContext

  implicit def provideDB[R](dbio: DBIO[R]): IO[RepositoryException, R] =
    fromDBIO(dbio).provide(self)

  override def modelDAO: ModelDAO.Service = new ModelDAO.Service with ModelSlickInterop {
    import Tables._

    def me: DBIO[Int] = sql"SELECT count(*) FROM recipe".as[Int].head

    override def count: IO[RepositoryException, Int] = sql"SELECT count(*) FROM recipe".as[Int].head

    override def report: IO[RepositoryException, String] = {
      for {
        recipeCount <- sql"SELECT count(*) FROM recipe".as[Int].head
        groceryItemCount <- sql"SELECT count(*) FROM GroceryItem".as[Int].head
      } yield {
        s"""Recipes = $recipeCount
           |GroceryItems = $groceryItemCount
           |""".stripMargin
      }
    }

    override def sampleModelObjects(): IO[RepositoryException, Seq[SampleModelObject]] =
      Samplemodelobject.result.map(_.map(SamplemodelobjectRow2SampleModelObject))

    override def sampleModelObject(id: Int): IO[RepositoryException, Option[SampleModelObject]] =
      Samplemodelobject.filter(_.id === id).result.headOption.map(_.map(SamplemodelobjectRow2SampleModelObject))

    override def upsert(obj: SampleModelObject): IO[RepositoryException, SampleModelObject] =
      (Samplemodelobject returning Samplemodelobject.map(_.id) into ((_, id) => obj.copy(id = id)))
        .insertOrUpdate(SampleModelObject2SamplemodelobjectRow(obj))
        .map(_.getOrElse(obj))

    override def delete(obj: SampleModelObject): IO[RepositoryException, Boolean] =
      Samplemodelobject.filter(_.id === obj.id).delete.map(_ > 0)
  }

}
