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
package util

import api.LiveEnvironment
import dao.Tables
import model.SampleModelObject

import scala.concurrent.ExecutionContext

/**
 * Used to generate the database code automatically, or if you start there to generate the database schema.
 * Note that it generates it into the test directory, so that it doesn't override any changes you might have made.
 */
object CodeGen
    extends LiveEnvironment
// with App
    {
  override implicit val dbExecutionContext: ExecutionContext = ExecutionContext.Implicits.global

  println(Tables.schema.createStatements.mkString(";\n"))
  println

  slick.codegen.SourceCodeGenerator.main(
    Array(
      "slick.jdbc.MySQLProfile",                                                                                                                                                                   //profile
      "com.mysql.cj.jdbc.Driver",                                                                                                                                                                  //jdbc driver
      "jdbc:mysql://localhost:3306/fullscalastack?current_schema=fullscalastack&nullNamePatternMatchesAll=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", //url
      "/Volumes/Personal/projects/full-scala-stack/server/src/test/scala",                                                                                                                         //destination dir
      "dao",                                                                                                                                                                                       //package
      "root",                                                                                                                                                                                      //user
      ""
    ) //password
  )
}
