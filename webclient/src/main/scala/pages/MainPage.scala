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

package pages

import components.AbstractComponent
import japgolly.scalajs.react.{ BackendScope, ScalaComponent }
import japgolly.scalajs.react.component.Scala.Unmounted
import japgolly.scalajs.react.vdom.html_<^._

object MainPage extends AbstractComponent {
  case class State()
  class Backend($ : BackendScope[_, State]) {
    def render(S: State): VdomElement =
      appContext.consume { appState =>
        <.div("Hello World!")
      }
  }
  private val component = ScalaComponent
    .builder[Unit]("MainPage")
    .initialState(State())
    .renderBackend[Backend]
    .build

  def apply(): Unmounted[Unit, State, Backend] = component()
}
