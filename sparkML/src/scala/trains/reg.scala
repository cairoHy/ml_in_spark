package scala.trains

import scala.io.Source

/**
 * Created by zhy on 2015/8/21 0021.
 */

object readLine {
  def getLine = {
    val sr = Source.fromFile("Control.scala","UTF-8")
    val lines = sr.getLines
    for(line <- lines) println(line)
  }

  def main(args: Array[String]) {
    this.getLine
  }
}
