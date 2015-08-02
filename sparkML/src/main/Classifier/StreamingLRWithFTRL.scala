package main.Classifier

import breeze.linalg.SparseVector
import breeze.numerics.exp
import main.factory.InputData
import main.linalg.AlgUtil
import main.optimizer.FTRLProximal
import org.apache.spark.mllib.linalg.Vector
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.rdd.RDD

/**
 * Created by zhy on 2015/8/2 0002.
 */

/**
 * Logistic Regression
 */
final class StreamingLRWithFTRL(val numFeatures: Int)
  extends RegressionModel with InputData with Serializable {

  //设定优化算法
  override val optimizer = new FTRLProximal()

  //初始化特征向量
  private var weights: SparseVector[Double] = SparseVector.zeros(numFeatures)

  //训练参数
  override def train(trainData: RDD[LabeledPoint]): Unit = {
    trainData.foreach { data =>
      weights = optimizer.optimize(data, weights)
    }
  }

  //预测样本集合
  def predict(testData: RDD[Vector]): RDD[Double] = {
    testData.map { data =>
      predict(data)
    }
  }

  //根据假设函数 预测单个样本
  def predict(testData: Vector): Double = {
    val x: Double = weights.dot(AlgUtil.VtoB(testData))
    val prob: Double = sigmod(x)
    if (prob > 0.5) return 1.0
    else return 0.0
  }

  //sigmod函数
  private def sigmod(x: Double): Double = 1.0 / (1 + exp(-x))
}
