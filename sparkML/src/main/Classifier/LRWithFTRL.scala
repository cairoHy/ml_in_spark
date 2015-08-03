package main.Classifier

import breeze.linalg.SparseVector
import breeze.numerics.exp
import main.factory.InputLRData
import main.linalg.AlgUtil
import main.optimizer.FTRLProximal
import org.apache.spark.mllib.linalg.Vector
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.rdd.RDD

/**
 * Created by zhy on 2015/8/2 0002.
 */

/**
 * Logistic Regression逻辑回归模型
 */
final class LRWithFTRL(val numFeatures: Int)
  extends RegressionModel with InputLRData with Serializable {

  train(trainData)
  predictAccuracy(testData)

  //设定优化算法
  override val optimizer = new FTRLProximal(D = numFeatures)

  //初始化特征向量
  private var weights: SparseVector[Double] = SparseVector.zeros(numFeatures)

  def train(data: LabeledPoint): Unit = {
    println("训练数据" + data.toString)
    val tmpVector: Array[SparseVector[Double]] = optimizer.optimize(data, weights)
    weights = tmpVector(0)
    optimizer.updateOptimizer(tmpVector(1), tmpVector(2))
  }

  //训练参数
  override def train(trainData: RDD[LabeledPoint]): Unit = {
    val localTrainData = trainData.toLocalIterator
    localTrainData.foreach(data => train(data))
  }

  /**
   * 分类预测准确率
   * @param testData 测试数据集合
   * @return 准确率
   */
  def predictAccuracy(testData: RDD[LabeledPoint]): Unit = {
    val predictions = testData.map { data =>
      val prediction = (data.label, predict(data.features))
      train(data)
      prediction
    }
    val numData = predictions.count
    val numCorrect = predictions.filter { data =>
      data._1 == data._2
    }.count
    RMSE = numCorrect * 1.0 / numData
  }

  /**
   * 根据假设函数 预测单个样本
   * @param testData 测试样本数据
   * @return 分类数据：  1 or 0
   */
  def predict(testData: Vector): Double = {
    val x: Double = weights.dot(AlgUtil.VtoB(testData))
    val prob: Double = sigmod(x)
    if (prob > 0.5) return 1.0
    else return 0.0
  }

  override def getRMSE =
    println("使用FTRL-Proximal的逻辑回归在测试集上的预测准确率为" + RMSE + "\n----------测试完毕----------")

  //sigmod函数
  private def sigmod(x: Double): Double = 1.0 / (1 + exp(-x))
}
