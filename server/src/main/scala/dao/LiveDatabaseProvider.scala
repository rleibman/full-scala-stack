package dao

import slick.basic.BasicBackend
import slick.jdbc.JdbcBackend._
import zio.{ UIO, ZIO }
import zioslick.DatabaseProvider

trait LiveDatabaseProvider extends DatabaseProvider {
  val configKey: String

  override val databaseProvider: DatabaseProvider.Service = new DatabaseProvider.Service {
    override val db: UIO[BasicBackend#DatabaseDef] = ZIO.effectTotal(Database.forConfig(configKey))
  }
}
