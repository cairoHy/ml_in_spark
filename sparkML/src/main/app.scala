package main

import main.util.{Conf, MainHolder}

/**
 * Created by zhy on 2015/7/19 0019.
 */
object app extends App {

  override def main(args: Array[String]) {
    val opt = new Conf(args)

    MainHolder.setUp(opt)

    MainHolder.calculateRMSE
  }

}
