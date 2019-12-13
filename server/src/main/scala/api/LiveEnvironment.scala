package api

import dao.{MySQLDatabaseProvider, LiveModelDAO}
import mail.CourierPostman

/**
 * This Creates a live environment, with actual running stuff (real email, real database, etc)
 */
trait LiveEnvironment
  extends MySQLDatabaseProvider
    with LiveModelDAO
    with CourierPostman
    with Config {}
