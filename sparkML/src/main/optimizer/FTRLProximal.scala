package main.optimizer

import breeze.linalg.SparseVector
import breeze.numerics.abs
import org.apache.spark.mllib.regression.LabeledPoint

/**
 * Created by zhy on 2015/8/1 0001.
 */

/**
 *
 * @param beta 添加到梯度的协方差矩阵中避免学习速率过高
 * @param alpha 初始学习速率
 * @param L1 L1正则项权重
 * @param L2 L2正则项权重
 * @param D  特征向量维度
 */
final class FTRLProximal(val beta: Double = 0.1, val alpha: Double = 0.1, val L1: Double = 0.0, val L2: Double = 0.0, val D: Int = 100000)
  extends Optimizer {

  //TODO 这里需要把N和Z的更新返回到对应的Model
  private var N: SparseVector[Double] = SparseVector.zeros(D)
  private var Z: SparseVector[Double] = SparseVector.zeros(D)
  private var W: SparseVector[Double] = SparseVector.zeros(D)

  def optimize(data: LabeledPoint, initialWeights: SparseVector[Double]): SparseVector[Double] = {
    W = initialWeights
    step(data.features.toArray, data.label.toInt)
  }

  def step(feature: Array[Double], label: Int): SparseVector[Double] = {
    var p: Double = 0.0
    feature.foreach { dimen =>
      val i = feature.indexOf(dimen)
      var sign: Int = 0
      if (Z(i) < 0)
        sign = -1
      else
        sign = 1
      if (abs(Z(i)) <= L1) {
        W(i) = 0.0
      } else {
        W(i) = (sign * L1 - Z(i)) / ((beta + Math.sqrt(N(i))) / alpha + L2)
      }
      p += W(i)
    }

    // predict
    p = 1 / (1 + Math.exp(-p))

    // update
    val g: Double = p - label
    feature.foreach { dimen =>
      val i = feature.indexOf(dimen)
      val sigma: Double = (Math.sqrt(N(i) + g * g) - Math.sqrt(N(i))) / alpha
      Z(i) += g - sigma * W(i)
      N(i) += g * g
    }

    W
  }

}
