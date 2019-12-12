package dao

import zio.{ IO, ZIO }
import zio.macros.annotation.accessible
import zioslick.{ DatabaseProvider, RepositoryException, SlickZIO }
import model.SampleModelObject

//@accessible
//@mockable
trait ModelDAO {
  def modelDAO: ModelDAO.Service
}

object ModelDAO {
  trait Service {
    def count: IO[RepositoryException, Int]
    def report: IO[RepositoryException, String]

    //SampleModelObject CRUD
    def sampleModelObjects(): IO[RepositoryException, Seq[SampleModelObject]]
    def sampleModelObject(id: Int): IO[RepositoryException, Option[SampleModelObject]]
    def upsert(obj: SampleModelObject): IO[RepositoryException, SampleModelObject]
    def delete(obj: SampleModelObject): IO[RepositoryException, Boolean]
  }
}
