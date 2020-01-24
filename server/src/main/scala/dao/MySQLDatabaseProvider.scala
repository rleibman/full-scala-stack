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

import slick.basic.BasicBackend
import slick.jdbc.JdbcBackend._
import zio.{ UIO, ZIO }
import zioslick.DatabaseProvider

/**
 * Live database provider that provides a MySQL database, with a very specific configuration
 */
trait MySQLDatabaseProvider extends DatabaseProvider {
  val configKey: String

  override val databaseProvider: DatabaseProvider.Service = new DatabaseProvider.Service {
    override val db: UIO[BasicBackend#DatabaseDef] = ZIO.effectTotal(Database.forConfig(configKey))
  }
}
