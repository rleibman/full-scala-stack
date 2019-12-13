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

import api.{ Api, LiveEnvironment }
import core.{ BootedCore, CoreActors }
import web.Web

import scala.concurrent.ExecutionContext

/**
 * This is the actual application you run that contains everything it needs, the core, the actors, the api, the web, the environment.
 **/
object Rest
    extends App     //To run it
    with BootedCore //For stop and start
    with CoreActors
    with Api // The api
    with Web // As a web service
    with LiveEnvironment {
  override implicit val dbExecutionContext: ExecutionContext = actorSystem.dispatcher
}
