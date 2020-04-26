/*
 * Copyright 2019 Roberto Leibman
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dao

import api.Config
import mail.Postman
import model.{ SampleModelObject, SimpleSearch }
import slick.dbio.DBIO
import slick.jdbc.MySQLProfile.api._
import zio.{ DefaultRuntime, IO }
import zioslick.{ DatabaseProvider, RepositoryException, ZioSlickSupport }

import scala.concurrent.ExecutionContext

/**
 * Implements the model's database methods using slick and a given database provider
 * Note that it isn't fully detached from MySQL, unfortunately, that would be nice
 */
trait LiveRepository
    extends Repository
    with ZioSlickSupport
    with Postman
    with DatabaseProvider
    with Config
    with ModelSlickInterop {
  private val runtime                               = new DefaultRuntime {}
  implicit val dbExecutionContext: ExecutionContext = runtime.platform.executor.asEC

  implicit def provideDB[R](dbio: DBIO[R]): IO[RepositoryException, R] =
    fromDBIO(dbio).provide(this)

  override def repository: Repository.Service = new Repository.Service {

    import dao.Tables._

    override val sampleModelObjectOps: CRUDOperations[SampleModelObject, Int, SimpleSearch, Any] =
      new CRUDOperations[SampleModelObject, Int, SimpleSearch, Any] {

        override def upsert(obj: SampleModelObject)(implicit session: Any): IO[RepositoryException, SampleModelObject] =
          (SampleModelObjectQuery returning SampleModelObjectQuery.map(_.id) into ((_, id) => obj.copy(id = id)))
            .insertOrUpdate(SampleModelObject2SampleModelObjectRow(obj))
            .map(_.getOrElse(obj))

        override def get(pk: Int)(implicit session: Any): IO[RepositoryException, Option[SampleModelObject]] =
          SampleModelObjectQuery
            .filter(_.id === pk)
            .result
            .headOption
            .map(_.map(SampleModelObjectRow2SampleModelObject))

        override def delete(pk: Int, softDelete: Boolean)(implicit session: Any): IO[RepositoryException, Boolean] =
          SampleModelObjectQuery
            .filter(_.id === pk)
            .delete
            .map(_ > 0)

        //TODO add any search and sort parameters here
        override def search(
          search: Option[SimpleSearch]
        )(implicit session: Any): IO[RepositoryException, Seq[SampleModelObject]] =
          SampleModelObjectQuery.result
            .map(_.map(SampleModelObjectRow2SampleModelObject))

        //TODO add any search parameters here
        override def count(search: Option[SimpleSearch])(implicit session: Any): IO[RepositoryException, Long] =
          SampleModelObjectQuery.length.result
            .map(_.toLong)

      }
  }

}
