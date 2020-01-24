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

package css

import scalacss.ProdDefaults._
import scalacss.internal.mutable.GlobalRegistry

object AppCSS {
  object Style extends StyleSheet.Inline {

    import dsl._

    val navMenu: StyleA = style(
      display.flex,
      alignItems.center,
      backgroundColor(c"#F2706D"),
      margin.`0`,
      listStyle := "none"
    )

    val menuItem: Boolean => StyleA = styleF.bool(
      selected =>
        styleS(
          padding(20.px),
          fontSize(1.5.em),
          cursor.pointer,
          color(c"rgb(244, 233, 233)"),
          whiteSpace.nowrap,
          mixinIfElse(selected)(backgroundColor(c"#E8433F"), fontWeight._500)(
            &.hover(backgroundColor(c"#B6413E"))
          )
        )
    )
  }

  def load(): Unit = {
    GlobalRegistry.register(
      GlobalStyle,
      Style
    )
    GlobalRegistry.onRegistration(_.addToDocument())
  }
}
