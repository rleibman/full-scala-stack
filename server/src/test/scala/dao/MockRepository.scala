package dao

import model.{SampleModelObject, SimpleSearch}
import zio.{IO, ZIO}
import zioslick.RepositoryException

/**
 * A mock repository used for testing.
 */
trait MockRepository extends Repository {
  abstract class MockOps extends CRUDOperations[SampleModelObject, Int, SimpleSearch, Any] {
    override def upsert(e: SampleModelObject)(implicit session: Any): IO[RepositoryException, SampleModelObject] =
      ZIO.succeed(e)

    override def get(pk: Int)(implicit session: Any): IO[RepositoryException, Option[SampleModelObject]] =
      ZIO.succeed(None)

    override def delete(pk: Int, softDelete: Boolean)(implicit session: Any): IO[RepositoryException, Boolean] = ZIO.succeed(true)

    override def search(search: Option[SimpleSearch])(
      implicit session: Any
    ): IO[RepositoryException, Seq[SampleModelObject]] = ZIO.succeed(Seq.empty)

    override def count(search: Option[SimpleSearch])(implicit session: Any): IO[RepositoryException, Long] =
      ZIO.succeed(0)
  }

  override def repository: Repository.Service = new Repository.Service {
    override val sampleModelObjectOps: CRUDOperations[SampleModelObject, Int, SimpleSearch, Any] = new MockOps {}
  }
}
