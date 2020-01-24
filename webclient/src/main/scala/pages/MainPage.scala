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

package pages

import components.AbstractComponent
import japgolly.scalajs.react._
import japgolly.scalajs.react.component.Scala.Unmounted
import japgolly.scalajs.react.vdom.html_<^._
import model._
import org.scalajs.dom.raw.HTMLButtonElement
import service.SampleModelObjectRESTClient
import typingsJapgolly.semanticDashUiDashReact.components._
import typingsJapgolly.semanticDashUiDashReact.distCommonjsElementsButtonButtonMod.ButtonProps

import scala.util.{ Failure, Success }

/**
 * A "page" in the application, in this same directory you'd put all of the other application "pages".
 * These are not html pages per se, since we're dealing with a single page app. But it's useful to treat
 * each of these as pages internally.
 */
object MainPage extends AbstractComponent {
  case class State(objects: Seq[SampleModelObject] = Seq.empty)

  import util.ModelPickler._

  class Backend($ : BackendScope[_, State]) {
    def init(state: State): Callback = Callback.empty
    def refresh(state: State): Callback =
      SampleModelObjectRESTClient.remoteSystem.search(None).completeWith {
        case Success(objects) => $.modState(_.copy(objects = objects))
        case Failure(t) =>
          t.printStackTrace()
          Callback.empty //TODO do something else with the failure here
      }

    def onAddNewObject(event: ReactMouseEventFrom[HTMLButtonElement], data: ButtonProps): Callback =
      Callback.alert(
        "Clicked on 'Add New object'... did you expect something else? hey, I can't write everything for you!"
      )

    def render(state: State): VdomElement =
      appContext.consume { appState =>
        <.div(
          Table()(
            TableHeader()(
              TableRow()(
                TableHeaderCell()("Id"),
                TableHeaderCell()("Name")
              )
            ),
            TableBody()(
              state.objects.toVdomArray { obj =>
                TableRow()(
                  TableCell()(obj.id),
                  TableCell()(obj.name)
                )
              }
            )
          ),
          Button(onClick = onAddNewObject _)("Add new object")
        )
      }
  }
  private val component = ScalaComponent
    .builder[Unit]("MainPage")
    .initialState(State())
    .renderBackend[Backend]
    .componentDidMount($ => $.backend.init($.state) >> $.backend.refresh($.state))
    .build

  def apply(): Unmounted[Unit, State, Backend] = component()
}
