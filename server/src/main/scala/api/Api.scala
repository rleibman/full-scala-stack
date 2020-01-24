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

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.directives.DebuggingDirectives
import akka.http.scaladsl.server.{ Directives, Route, RouteConcatenation }
import core.{ Core, CoreActors }
import routes.{ HTMLRoute, ModelRoutes }

/**
 * This class puts all of the live services together with all of the routes
 * @author rleibman
 */
trait Api
    extends RouteConcatenation
    with Directives
    with LiveEnvironment
    with HTMLRoute
    with ModelRoutes
    with ZIODirectives {
  this: CoreActors with Core =>

  private implicit val _ = actorSystem.dispatcher

  //TODO This particular example app doesn't use sessions, look up "com.softwaremill.akka-http-session" if you want sessions
  val sessionResult = Option("validsession")

  val routes: Route = DebuggingDirectives.logRequest("Request") {
    extractLog { log =>
      unauthRoute ~ {
        extractRequestContext { requestContext =>
          sessionResult match {
            case Some(session) =>
              apiRoute(session)
            case None =>
              log.info(
                s"Unauthorized request of ${requestContext.unmatchedPath}, redirecting to login"
              )
              redirect("/loginForm", StatusCodes.Found)
          }
        }
      } ~
      htmlRoute
    }
  }
}
