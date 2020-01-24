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

package dao

import model.SampleModelObject

/**
 * Converters between model objects and database objects. We could use one object for both,
 * but I find that I usually want the row objects as simple as possible
 */
trait ModelSlickInterop {

  import Tables._

  implicit def SampleModelObjectRow2SampleModelObject(row: SampleModelObjectRow) = SampleModelObject(row.id, row.name)

  implicit def SampleModelObject2SampleModelObjectRow(value: SampleModelObject) =
    SampleModelObjectRow(value.id, value.name)
}
