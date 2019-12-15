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
import japgolly.scalajs.react._
import japgolly.scalajs.react.component.Scala.Unmounted
import japgolly.scalajs.react.extra.Ajax
import japgolly.scalajs.react.vdom.html_<^._
import typingsJapgolly.semanticDashUiDashReact.components._
import model._
import org.scalajs.dom
import org.scalajs.dom.raw.HTMLButtonElement
import typingsJapgolly.semanticDashUiDashReact.distCommonjsElementsButtonButtonMod.ButtonProps
import ujson.Value.InvalidData
import upickle.default._

/**
 * A "page" in the application, in this same directory you'd put all of the other application "pages".
 * These are not html pages per se, since we're dealing with a single page app. But it's useful to treat
 * each of these as pages internally.
 */
object MainPage extends AbstractComponent {
  case class State(objects: Seq[SampleModelObject] = Seq.empty)

  class Backend($ : BackendScope[_, State]) {
    def init(state: State): Callback = Callback.empty
    def refresh(state: State): Callback =
      Ajax
        .get("http://localhost:8079/sampleModelObjects") //TODO move the root of the app to a config file
        .send
        .asAsyncCallback
        .map { xhr =>
          try {
            import util.ModelPickler._
            val objects = read[Seq[SampleModelObject]](xhr.responseText)
            $.modState(_.copy(objects = objects))
          } catch {
            case e: InvalidData =>
              dom.console.error(e.msg + ":" + e.data)
              throw e
          }
        }
        .completeWith(_.get)

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
