package scala.trains

import scala.collection.mutable.ArrayBuffer

/**
 * Created by zhy on 2015/8/20 0020.
 */
abstract class Item {
  def price(): Double

  def description(): String

  override def toString(): String = {
    "description:" + description() + "  price:" + price()
  }
}

class SimpleItem(override val price: Double, override val description: String) extends Item {
}

class Bundle() extends Item {
  private val items = new ArrayBuffer[Item]()

  def pack(item: Item) = {
    items += item
  }

  override def description() = "the overall price is :" + price

  override def price(): Double = {
    var total = 0d
    items.foreach(item => total += item.price)
    total
  }

  override def toString(): String = {
    items.mkString(" | ")
  }
}

object aa extends App {
  val a = new SimpleItem(12, "asdf1")
  val b = new SimpleItem(1234, "sadf")
  val c = new Bundle

  println(c.toString)
  c.pack(a)
  c.pack(b)
  println(c.toString)
}