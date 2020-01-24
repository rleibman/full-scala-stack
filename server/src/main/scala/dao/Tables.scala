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
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = slick.jdbc.MySQLProfile
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.jdbc.JdbcProfile
  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{ GetResult => GR }

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = SampleModelObjectQuery.schema
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table SampleModelObject
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param name Database column name SqlType(TEXT) */
  case class SampleModelObjectRow(id: Int, name: String)

  /** GetResult implicit for fetching SampleModelObjectRow objects using plain SQL queries */
  implicit def GetResultSampleModelObjectRow(implicit e0: GR[Int], e1: GR[String]): GR[SampleModelObjectRow] = GR {
    prs =>
      import prs._
      SampleModelObjectRow.tupled((<<[Int], <<[String]))
  }

  /** Table description of table SampleModelObject. Objects of this class serve as prototypes for rows in queries. */
  class SampleModelObjectTable(_tableTag: Tag)
      extends profile.api.Table[SampleModelObjectRow](_tableTag, Some("fullscalastack"), "SampleModelObject") {
    def * = (id, name) <> (SampleModelObjectRow.tupled, SampleModelObjectRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? =
      ((Rep.Some(id), Rep.Some(name))).shaped.<>({ r =>
        import r._; _1.map(_ => SampleModelObjectRow.tupled((_1.get, _2.get)))
      }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)

    /** Database column name SqlType(TEXT) */
    val name: Rep[String] = column[String]("name")
  }

  /** Collection-like TableQuery object for table SampleModelObject */
  lazy val SampleModelObjectQuery = new TableQuery(tag => new SampleModelObjectTable(tag))
}
