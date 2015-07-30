package main.recommender

import org.apache.spark.mllib.recommendation.Rating
import org.apache.spark.rdd.RDD

import scala.collection.mutable.ArrayBuffer

/**
 * Created by zhy on 2015/7/26 0026.
 */
final class SlopOneRecommender extends MyRecommender {
  val trainDataGroupByUser = trainData.map(rating => (rating.user, (rating.product, rating.rating)))
    .groupByKey.persist
  test


  /**
   * 使用测试集进行测试
   */
  private def test = {
    RMSE = calculateRmse(testData, numTest)
  }

  def numUserConsumer_ij(product_i: Int, product_j: Int): Long = {
    trainDataGroupByUser.filter { trainData4one =>
      trainData4one._2.exists(a => a._1 == product_i) && trainData4one._2.exists(b => b._1 == product_j)
    }.count()
  }

  /**
   *
   * @param user 用户ID
   * @param product 物品ID
   * @return 评分三元组
   */
  def predict(user: Int, product: Int): Rating = {
    val userRatings = {
      val ratings = trainDataGroupByUser.lookup(user)
      if (ratings.length <= 0) throw new UserNotFoundException
      ratings(0).toIterator
    }
    var prediction: Double = 0
    var sum_S_ij: Long = 0
    userRatings.foreach { rating =>
      val deviation_ij = calcuDeviation_ij(product, rating._1)
      val S_ij = numUserConsumer_ij(product, rating._1)
      val r_ui = rating._2
      prediction += (deviation_ij + r_ui) * S_ij
      sum_S_ij += S_ij
      if (rating._1 == product) return new Rating(user, product, rating._2)
    }
    new Rating(user, product, prediction / sum_S_ij)
  }


  private def calculateRmse(dataset: RDD[Rating], n: Long): Double = {
    println("开始计算RMSE")
    var predictions = ArrayBuffer[Rating]()
    val train = dataset.toLocalIterator
    train.foreach { x =>
      println("预测一个样本的评分")
      predictions += predict(x.user, x.product)
    }
    val predictionsRDD: RDD[Rating] = sc.parallelize(predictions.toSeq)
    val predictionsAndRatings = predictionsRDD.map { x =>
      ((x.user, x.product), x.rating)
    }.join(dataset.map { x =>
      ((x.user, x.product), x.rating)
    }
      ).values
    val tmp_RMSE = math.sqrt(predictionsAndRatings.map(x => (x._1 - x._2) * (x._1 - x._2)).reduce(_ + _) / n)
    println("计算得到的RMSE为： " + tmp_RMSE)
    tmp_RMSE
  }

  /**
   *
   * @param product_i 物品i
   * @param product_j 物品j
   * @return 物品i与j的偏差
   */
  private def calcuDeviation_ij(product_i: Int, product_j: Int): Double = {
    val userList4i = trainData.filter(rating => rating.product == product_i)
    val userList4j = trainData.filter(rating => rating.product == product_j)
    val userList4ij = userList4i.intersection(userList4j)
    val numUser4ij = userList4ij.count()
    if (numUser4ij == 0) return 0
    var deviation_ij: Double = 0
    userList4ij.foreach { rating =>
      val user = rating.user
      val rating_ui = userList4ij.filter(rating => rating.user == user && rating.product == product_i)
        .toLocalIterator.next().rating
      val rating_uj = userList4ij.filter(rating => rating.user == user && rating.product == product_j)
        .toLocalIterator.next().rating
      deviation_ij += (rating_ui - rating_uj)
    }
    deviation_ij / numUser4ij
  }

  class UserNotFoundException extends Exception

}
