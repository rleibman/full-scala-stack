package api

import dao.{ LiveDatabaseProvider, LiveModelDAO }
import mail.LivePostman

trait LiveEnvironment extends LiveDatabaseProvider with LiveModelDAO with LivePostman with Config {}
