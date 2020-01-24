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

import akka.http.scaladsl.server.Directives
import api.ZIODirectives
import dao.Repository
import mail.Postman
import model.{ SampleModelObject, SimpleSearch }

/**
 * You will likely have one of these for each of your model objects that require crud operations. Please look at
 * CRUDRoute, there are a few methods you can override if you want to support things other than just crud operations.
 *
 */
trait SampleModelObjectRoute extends CRUDRoute[SampleModelObject, Int, SimpleSearch, Any] with Repository with Postman {

  override def crudRoute: CRUDRoute.Service[SampleModelObject, Int, SimpleSearch, Any] =
    new CRUDRoute.Service[SampleModelObject, Int, SimpleSearch, Any]() with Directives with ZIODirectives {

      override val url: String = "sampleModelObject"

      override val ops = repository.sampleModelObjectOps

      override def getPK(obj: SampleModelObject): Int = obj.id
    }

}
