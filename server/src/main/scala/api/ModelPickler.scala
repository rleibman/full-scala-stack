package api

import model.SampleModelObject
import ujson._
import upickle.default.{macroRW, ReadWriter => RW, _}

trait ModelPickler {
  implicit val SampleModelObjectRW: RW[SampleModelObject] = macroRW
}
