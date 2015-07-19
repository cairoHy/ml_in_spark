package main.recommender

import main.util.{MainHolder, SparkEnv}

/**
 * Created by zhy on 2015/7/19 0019.
 */

trait Recommender extends Serializable {
  protected var RMSE: Double = Double.MaxValue

  /**
   * @return 推荐算法对于指定数据集推荐结果的均方根误差(RMSE)
   */
  def getRMSE = println("测试集的RMSE为 " + RMSE + "\n----------测试完毕----------")
}

/**
 * 具体算法的公共父类，用于数据初始化
 */
class MyRecommender extends Recommender {
  //获取数据
  protected val sc = SparkEnv.sc
  protected val ratings = MainHolder.getDataHolder().getRatings
  protected val ratingsGroupedByUser = ratings.map(rat => (rat.user, rat)).groupByKey().persist
  MainHolder.getDataHolder().printRatingDesc

  //分割数据集为训练集、验证集、测试集
  protected val RDD = ratings.randomSplit(Array(0.7, 0.2, 0.1))
  protected val trainData = RDD(0).persist
  protected val validateData = RDD(1).persist
  protected val testData = RDD(2).persist
  protected val numValidation = validateData.count
  protected val numTest = testData.count
}
