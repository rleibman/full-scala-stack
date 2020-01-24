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

import model.Search
import zio.IO
import zioslick.RepositoryException

/**
 * Collects the basic CRUD operations of a single object (or object graph) against a data source.
 * @tparam E
 * @tparam PK
 * @tparam SEARCH
 * @tparam SESSION
 */
trait CRUDOperations[E, PK, SEARCH <: Search[_], SESSION] {
  def upsert(e: E)(implicit session: SESSION): IO[RepositoryException, E]
  def get(pk: PK)(implicit session: SESSION): IO[RepositoryException, Option[E]]
  def delete(pk: PK)(implicit session: SESSION): IO[RepositoryException, Boolean]
  def search(search: Option[SEARCH] = None)(
    implicit session: SESSION
  ): IO[RepositoryException, Seq[E]]
  def count(search: Option[SEARCH] = None)(
    implicit session: SESSION
  ): IO[RepositoryException, Long]
}
