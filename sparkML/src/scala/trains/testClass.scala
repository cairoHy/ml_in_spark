package scala.trains

import scala.beans.BeanProperty

/**
 * Created by zhy on 2015/7/20 0020.
 */
object testClass {
  @BeanProperty
  var name = "zhy"

  def main(args: Array[String]) {
    print(testClass.setName("adf"))
  }
}
