package model

object SortDirection extends Enumeration {
  type SortDirection = Value

  def reverse(direction: SortDirection): SortDirection = if (direction == asc) dsc else asc
  val asc, dsc                                         = Value
}

import SortDirection._

trait Sort {
  val direction: SortDirection
}

case class DefaultSort(direction: SortDirection) extends Sort

trait Search[SORT <: Sort] {
  val pageIndex: Int //NOTE!!!! PageIndex is zero based, if you're doing database work, you'll likely want the offset, which is 1 based
  val pageSize: Int
  val sort: Option[SORT]
}

case class SimpleSearch(pageIndex: Int = 0, pageSize: Int = 10, sort: Option[DefaultSort] = None)
    extends Search[DefaultSort]

case class SimpleTextSearch(
  text: String = "",
  pageIndex: Int = 0,
  pageSize: Int = 10,
  sort: Option[DefaultSort] = None
) extends Search[DefaultSort]
