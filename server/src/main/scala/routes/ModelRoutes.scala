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
package routes

import akka.http.scaladsl.server.{ Directives, Route }
import api.LiveEnvironment
import util.ModelPickler

/**
 * For convenience, this trait aggregates all of the model routes.
 */
trait ModelRoutes extends Directives with ModelPickler {

  private val sampleModelObjectRouteRoute = new SampleModelObjectRoute with LiveEnvironment {}

  private val crudRoutes: List[CRUDRoute[_, _, _, Any]] = List(
    sampleModelObjectRouteRoute
  )

  def unauthRoute: Route =
    crudRoutes.map(_.crudRoute.unauthRoute).reduceOption(_ ~ _).getOrElse(reject)

  //TODO: it would be nice to be able to do this, but it's hard to define the readers and writers for marshalling
  //  def apiRoute(session: Any): Route =
  //    pathPrefix("api") {
  //    crudRoutes.map(_.crudRoute.route(session)).reduceOption(_ ~ _).getOrElse(reject)
  //  }

  def apiRoute(session: Any): Route = pathPrefix("api") {
    sampleModelObjectRouteRoute.crudRoute.route(session)
  }
}
