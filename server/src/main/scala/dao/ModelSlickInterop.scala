package dao
import model.SampleModelObject

trait ModelSlickInterop {
  import Tables._
  implicit def SamplemodelobjectRow2SampleModelObject(row: SamplemodelobjectRow) = SampleModelObject(row.id, row.name)
  implicit def SampleModelObject2SamplemodelobjectRow(value: SampleModelObject) = SamplemodelobjectRow(value.id, value.name)
}
