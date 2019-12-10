package api

import dao.{LiveDatabaseProvider, LiveRecipeDAO}
import mail.LivePostman

trait LiveEnvironment
  extends LiveDatabaseProvider
    with LiveRecipeDAO
    with LivePostman
    with Config {
}


