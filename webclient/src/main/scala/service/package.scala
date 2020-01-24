/*
 * Copyright 2020 Roberto Leibman
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

import model.{ SampleModelObject, SimpleSearch }
import util.Config

package object service extends Config {

  object SampleModelObjectRESTClient extends LiveRESTClient[SampleModelObject, Int, SimpleSearch] {
    override val baseUrl: String = s"$mealoramaHost/api/sampleModelObjects"
    //If you want anything more than CRUD out of this client, you override remoteSystem and add other methods you may want
  }

}
