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

package api

import akka.http.scaladsl.server.directives.ContentTypeResolver
import akka.http.scaladsl.server.{ Directives, Route }
import better.files.File

/**
 * This service serves the site's static content, including the scala.js generated content
 */
trait HTMLService extends Directives with Config {
  val staticContentDir: String = config.getString("full-scala-stack.staticContentDir")

  val dir = File(staticContentDir)

  override def getFromDirectory(
    directoryName: String
  )(implicit resolver: ContentTypeResolver): Route =
    extractUnmatchedPath { unmatchedPath =>
      getFromFile(s"$staticContentDir/$unmatchedPath")
    }

  def htmlRoute: Route =
    pathEndOrSingleSlash {
      get {
        getFromFile(s"$staticContentDir/index.html")
      }
    } ~
      get {
        extractUnmatchedPath { path =>
          encodeResponse {
            getFromDirectory(staticContentDir)
          }
        }
      }
}
