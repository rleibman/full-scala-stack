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

package mail

import com.typesafe.config.Config
import courier.{ Envelope, Mailer }
import zio.ZIO

/**
 * A trait that knows how to deliver email, and uses ZIO
 * //TODO Might want to move to a separate project.
 */
trait Postman {
  val postman: Postman.Service[Any]
}

object Postman {
  trait Service[R] {
    def deliver(email: Envelope): ZIO[R, Throwable, Unit]
  }
}

/**
 * An instatiation of the Postman that user the courier mailer
 */
trait CourierPostman extends Postman {
  val configKey: String
  val config: Config

  override val postman: Postman.Service[Any] = new Postman.Service[Any] {
    lazy val mailer: Mailer = {
      val localhost = config.getString(s"$configKey.smtp.localhost")
      System.setProperty("mail.smtp.localhost", localhost)
      System.setProperty("mail.smtp.localaddress", localhost)
      val auth = config.getBoolean(s"$configKey.smtp.auth")
      if (auth)
        Mailer(config.getString(s"$configKey.smtp.host"), config.getInt(s"$configKey.smtp.port"))
          .auth(auth)
          .as(
            config.getString(s"$configKey.smtp.user"),
            config.getString(s"$configKey.smtp.password")
          )
          .startTls(config.getBoolean(s"$configKey.smtp.startTTLS"))()
      else
        Mailer(config.getString(s"$configKey.smtp.host"), config.getInt(s"$configKey.smtp.port"))
          .auth(auth)()
    }

    override def deliver(email: Envelope): ZIO[Any, Throwable, Unit] =
      ZIO.fromFuture(implicit ec => mailer(email))
  }
}
