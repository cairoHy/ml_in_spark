package main.recommender

import main.util.Conf

/**
 * Created by zhy on 2015/7/19 0019.
 */

/**
 * 推荐算法工厂
 */
trait RecommenderFactory {
  def getName: String

  def getRecommender(conf: Conf): Recommender

  def getAlgorithmDescription(): String

  def getParametersDescription(): String

  def getDescription(): String = {
    getAlgorithmDescription() + "\n参数:\n" + getParametersDescription()
  }
}

object RecommenderFactory {
  val recommenderFactories: List[RecommenderFactory] = List(ALSRecommenderFactory)
}

object ALSRecommenderFactory extends RecommenderFactory {
  override def getName: String = "ALS"

  override def getAlgorithmDescription(): String = "MLLib中ALS算法"

  protected val rankStr = "rank"
  protected val lambdaStr = "λ"
  protected val iterStr = "numberOfIterations"

  override def getParametersDescription(): String = rankStr + " = <Int>，特征向量维度\n" + lambdaStr + " = <Double>，正则化参数\n" + iterStr + " = <Int>,迭代次数"

  override def getRecommender(conf: Conf): Recommender = {
    val ranks = 12 to 15
    val lambdas = List(0.01, 0.05)
    val iters = 10 to 20

    println(getDescription)
    new ALSRecommender(ranks, lambdas, iters)
  }
}