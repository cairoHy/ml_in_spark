package main.factory

import main.classifier.LRWithFTRL
import main.recommender.{ALSRec, Recommender, SlopOneRec}
import main.util.Conf

/**
 * Created by zhy on 2015/7/19 0019.
 */

/**
 * 机器学习算法工厂
 */
trait AlgorithmFactory {
  def getName: String

  def getAlg(conf: Conf): Algorithm

  def getAlgDes(): String

  def getParamDes(): String

  def getDescription(): String = {
    getAlgDes() + "\n参数:\n" + getParamDes()
  }
}

object AlgorithmFactory {
  val AlgList: List[AlgorithmFactory] = List(ALS, SlopOne, LRWithFTRL)
}

object ALS extends AlgorithmFactory {
  override def getName: String = "ALS"

  override def getAlgDes(): String = "MLLib中ALS算法"

  protected val rankStr = "rank"
  protected val lambdaStr = "λ"
  protected val iterStr = "numberOfIterations"

  override def getParamDes(): String = rankStr + " = <Int>，特征向量维度\n" + lambdaStr + " = <Double>，正则化参数\n" + iterStr + " = <Int>,迭代次数"

  override def getAlg(conf: Conf): Recommender = {
    val ranks = 12 to 15
    val lambdas = List(0.01, 0.05)
    val iters = 10 to 20

    println(getDescription)
    new ALSRec(ranks, lambdas, iters)
  }
}

object SlopOne extends AlgorithmFactory {
  override def getName: String = "Slop-One"

  override def getAlg(conf: Conf): Recommender = {
    println(getDescription)
    new SlopOneRec
  }

  override def getParamDes(): String = "无参数"

  override def getAlgDes(): String = "Slop-One算法"
}

object LRWithFTRL extends AlgorithmFactory {
  override def getName: String = "LR-FTRL"

  protected val numFea = "numFeatures"

  override def getParamDes(): String = numFea + "= <Int>，特征向量维度\n"

  override def getAlgDes(): String = "采用FTRL-Proximal优化的Logistic Regression算法"

  override def getAlg(conf: Conf): Algorithm = {
    //TODO 根据数据集特征提供维度
    val numFeatures = 1000

    println(getDescription)
    new LRWithFTRL(numFeatures)
  }
}