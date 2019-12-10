package util

import api.LiveEnvironment
import dao.Tables
import model.SampleModelObject

import scala.concurrent.ExecutionContext

object CodeGen extends LiveEnvironment
 with App
{
  override implicit val dbExecutionContext: ExecutionContext = ExecutionContext.Implicits.global

  println(Tables.schema.createStatements.mkString(";\n"))
  println

  slick.codegen.SourceCodeGenerator.main(
    Array(
      "slick.jdbc.MySQLProfile",                                                                                                                                                       //profile
      "com.mysql.cj.jdbc.Driver",                                                                                                                                                      //jdbc driver
      "jdbc:mysql://localhost:3306/fullscalastack?current_schema=fullscalastack&nullNamePatternMatchesAll=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", //url
      "/Volumes/Personal/projects/meal-o-rama/full-scala-stack/server/src/main/scala",                                                                                                      //destination dir
      "dao",                                                                                                                                                                           //package
      "root",                                                                                                                                                                          //user
      ""
    ) //password
  )
}
