package main.util

/**
 * Created by zhy on 2015/7/18 0018.
 */

import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkConf, SparkContext}

/**
 * 初始化SparkContext
 */
object SparkEnv {

  Logger.getLogger("org.apache.spark").setLevel(Level.WARN)
  Logger.getLogger("org.eclipse.jetty.server").setLevel(Level.OFF)

  val conf = new SparkConf().setAppName("MachineLearningInSpark").setMaster("local[2]")
  val sc = new SparkContext(conf)
}