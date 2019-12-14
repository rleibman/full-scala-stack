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

package components

import app.AppState
import japgolly.scalajs.react.React.Context

/**
 * An abstract component trait from which all components in the app should derive. A good
 * place to put in global implicits, common code that should be in all pages, etc.
 */
trait AbstractComponent {
  val appContext: Context[AppState] = AppState.ctx
}
