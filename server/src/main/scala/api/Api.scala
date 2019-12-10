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

import akka.http.scaladsl.server.{ Route, RouteConcatenation }
import core.{ Core, CoreActors }

import scala.concurrent.ExecutionContext

/**
 * @author rleibman
 */
trait Api extends RouteConcatenation { this: CoreActors with Core =>

  val service: MealORamaService = new MealORamaService {
    override implicit val dbExecutionContext: ExecutionContext = actorSystem.dispatcher
  }

  //Notice the wrapping of the swaggerRoutes into cors...
  //this is used to enable cors (Cross-origin resource sharing) IMHO,
  //that should be the responsibility of SwaggerHttpService, but I'll let it be for now
  val routes: Route = service.route
  //    ~ io.github.lhotari.akka.http.health.HealthEndpoint.createDefaultHealthRoute()
  //~ swaggerRoutes.routes //~ respondWithHeaders(web.CORSSupport.headers) { swaggerRoutes.routes }

  //  val rootService = system.actorOf(Props(new RoutedHttpService(routes)))
}
