package main.classifier

import main.factory.Algorithm
import main.optimizer.Optimizer
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.rdd.RDD

/**
 * Created by zhy on 2015/8/2 0002.
 */

/**
 * 回归模型
 */
trait RegressionModel extends Algorithm with Serializable {

  //优化算法
  def optimizer: Optimizer

  //训练及预测
  def train(trainData: RDD[LabeledPoint]): Unit
}
