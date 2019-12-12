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

package css

import scalacss.ProdDefaults._

object GlobalStyle extends StyleSheet.Inline {

  import dsl._

  style(
    unsafeRoot("body")(
      margin.`0`,
      padding(2.px),
      fontSize(14.px),
      font := "normal small 'Roboto', sans-serif"
    ),
    unsafeRoot("h2")(marginTop(1.em))
  )
}
