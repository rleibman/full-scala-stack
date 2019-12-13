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
      val auth = config.getBoolean(s"$configKey.smtp.auth")
      if (auth)
        Mailer(config.getString(s"$configKey.smtp.host"), config.getInt(s"$configKey.smtp.port"))
          .auth(auth)
          .as(config.getString(s"$configKey.smtp.user"), config.getString(s"$configKey.smtp.password"))
          .startTls(config.getBoolean(s"$configKey.smtp.startTTLS"))()
      else
        Mailer(config.getString(s"$configKey.smtp.host"), config.getInt(s"$configKey.smtp.port"))
          .auth(auth)()
    }

    override def deliver(email: Envelope): ZIO[Any, Throwable, Unit] =
      ZIO.fromFuture(implicit ec => mailer(email))
  }
}
