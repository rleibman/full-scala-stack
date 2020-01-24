package api

import dao.{ LiveRepository, MySQLDatabaseProvider }
import mail.CourierPostman

/**
 * This Creates a live environment, with actual running stuff (real email, real database, etc)
 */
trait LiveEnvironment extends MySQLDatabaseProvider with LiveRepository with CourierPostman with Config {}
