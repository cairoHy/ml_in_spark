package main.util

import main.factory.AlgorithmFactory
import main.input.DataFactory
import org.rogach.scallop.ScallopConf

/**
 * Created by zhy on 2015/7/19 0019.
 */

/**
 * 命令行参数解析类
 * @param arguments 命令行参数
 */
class Conf(arguments: Seq[String]) extends ScallopConf(arguments) {

  val datasetTypes = DataFactory.dataHolderFactories
  val algorithms = AlgorithmFactory.AlgList

  banner( """
Spark机器学习算法
----------------
基于Spark的机器学习算法库

示例:
spark-submit [Jar] --data Yahoo --dir /zhy/data/Yahoo/ --method ALS

参数:
          """)

  version("version 1.0")

  val data = opt[String](required = true, validate = { str => datasetTypes.map(_.getName).contains(str) }, descr = {
    "数据集类型。可选类型： " + datasetTypes.map(_.getName).reduce(_ + ", " + _)
  })

  val dir = opt[String](required = true, descr = "数据集根目录")

  val method = opt[String](required = true, validate = { str => algorithms.map(_.getName).contains(str) }, descr = {
    "推荐算法。可选类型： " + algorithms.map(_.getName).reduce(_ + ", " + _)
  })

}
