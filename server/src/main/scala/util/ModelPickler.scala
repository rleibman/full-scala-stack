package util

import model.SampleModelObject
import upickle.default.{ macroRW, ReadWriter => RW, _ }

/**
 * Here's where we define all of the model object's picklers and unpicklers.
 * You may want to move this to the shared project, though I like to keep them separately in case
 * you want to use a different method for marshalling json between the client and server
 */
trait ModelPickler {
  implicit val SampleModelObjectRW: RW[SampleModelObject] = macroRW
}
