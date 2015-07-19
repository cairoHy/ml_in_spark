package main.recommender

import org.apache.spark.mllib.recommendation.{ALS, MatrixFactorizationModel, Rating}
import org.apache.spark.rdd.RDD

/**
 * Created by zhy on 2015/7/19 0019.
 */

final class ALSRecommender(ranks: Range, lambdas: List[Double], numIters: Range) extends MyRecommender {
  //训练模型并测试
  val model = train(numValidation)
  test

  /**
   * 使用测试集进行测试
   */
  private def test = {
    RMSE = calculateRmse(model.get, testData, numTest)
  }

  /**
   * 计算rmse均方根误差
   * @param model 算法模型
   * @param dataset 数据集
   * @param n 数据集大小
   * @return 该算法模型在该验证数据集上的RMSE
   */
  private def calculateRmse(model: MatrixFactorizationModel, dataset: RDD[Rating], n: Long): Double = {
    val predictions: RDD[Rating] = model.predict(dataset.map(x => (x.user, x.product)))
    val predictionsAndRatings = predictions.map { x =>
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
   * 训练模型
   * @param numValidation 验证集大小
   * @return 训练完成的模型
   */
  private def train(numValidation: Long): Option[MatrixFactorizationModel] = {
    RMSE = Double.MaxValue
    var bestModel: Option[MatrixFactorizationModel] = None
    var bestRank = 0
    var bestLambda = -1.0
    var bestNumIter = -1

    for (rank <- ranks; lambda <- lambdas; numIter <- numIters) {
      val model = ALS.train(trainData, rank, numIter, lambda)
      val validataionRmse = calculateRmse(model, validateData, numValidation)
      if (validataionRmse < RMSE) {
        bestModel = Some(model)
        RMSE = validataionRmse
        bestRank = rank
        bestLambda = lambda
        bestNumIter = numIter
      }
    }
    println("模型训练完毕。最优参数为： （rank = " + bestRank + "; numIter = " +
      bestNumIter + "; lambda = " + bestLambda + ")")

    bestModel
  }
}