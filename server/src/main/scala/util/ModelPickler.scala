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

import model._
import upickle.default.{ macroRW, ReadWriter => RW, _ }

/**
 * Here's where we define all of the model object's picklers and unpicklers.
 * You may want to move this to the shared project, though I like to keep them separately in case
 * you want to use different methods for marshalling json in the client and in server
 */
trait ModelPickler {
  import SortDirection._

  implicit val SortDirectionRW: RW[SortDirection] = upickle.default
    .readwriter[String]
    .bimap[SortDirection](
      x => x.toString,
      str => SortDirection.withName(str)
    )

  implicit val SampleModelObjectRW: RW[SampleModelObject] = macroRW
  implicit val SimpleSearchRW: RW[SimpleSearch]           = macroRW
  implicit val DefaultSortRW: RW[DefaultSort]             = macroRW
  implicit val SimpleTextSearchRW: RW[SimpleTextSearch]   = macroRW
}
