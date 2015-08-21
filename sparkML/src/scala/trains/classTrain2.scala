package scala.trains

import java.awt

/**
 * Created by zhy on 2015/8/20 0020.
 */
class Point(val x:Int,val y:Int) {
}

class LabeledPoint(val label:String,override val x:Int,override val y:Int) extends Point(x, y) {
}

object aaa extends App{
  val a = new LabeledPoint("fds",1,2)
}

class Square(point:java.awt.Point, width:Int) extends java.awt.Rectangle(point.x,point.y,width,width) {
  def this(){
    this(new java.awt.Point(0,0),0)
  }

  def this(width:Int) {
    this(new awt.Point(0,0),width)
  }
}