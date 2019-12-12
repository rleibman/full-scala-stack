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

package api

import better.files.File
import com.typesafe.config.ConfigFactory

trait Config {
  val configKey = "full-scala-stack"
  val config: com.typesafe.config.Config = {
    val confFileName =
      System.getProperty("application.conf", "./src/main/resources/application.conf")
    val confFile = File(confFileName)
    val config = ConfigFactory
      .parseFile(confFile.toJava)
      .withFallback(ConfigFactory.load())
    config
  }
}
