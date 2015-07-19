package main.mlTrains

import org.apache.log4j.{Level, Logger}
import org.apache.spark.mllib.recommendation.{ALS, MatrixFactorizationModel, Rating}
import org.apache.spark.rdd._
import org.apache.spark.{SparkConf, SparkContext}

object ALSTrain {

  def main(args: Array[String]) {

    println("----------算法：MLLib库中ALS算法----------\n" +
      "----------适用数据集：Yahoo数据集----------\n"
    )

    // 设置Log
    Logger.getLogger("org.apache.spark").setLevel(Level.WARN)
    Logger.getLogger("org.eclipse.jetty.server").setLevel(Level.OFF)

    // 判断参数
    if (args.length != 1) {
      println("spark-submit [yourJar] datasetFileName")
      sys.exit(1)
    }

    // 初始化环境

    val conf = new SparkConf()
      .setAppName("MovieLensALS")
      .setMaster("local[2]")
    val sc = new SparkContext(conf)

    val DataPath = args(0)

    // 去掉字符串中多余的空格
    def formatSpace(line: String): String = {
      line.replaceAll("\\s+", " ")
    }

    // 载入训练数据
    val trainData = sc.textFile(DataPath + "data.txt")
      .filter(line => formatSpace(line).split(" ").length >= 3)
      .map { line =>
      val lineFormat = formatSpace(line)
      val fields = lineFormat.split(" ")
      (Rating(fields(0).toInt, fields(1).toInt, fields(2).toDouble))
    }

    val numTrainData = trainData.count
    val numUsers = trainData.map(_.user).distinct.count
    val numProducts = trainData.map(_.product).distinct.count

    val numPartitions = 20
    val RDD = trainData.randomSplit(Array(0.7, 0.2, 0.1))
    val trainingRDD = RDD(0).repartition(numPartitions).persist
    val validateRDD = RDD(1).repartition(numPartitions).persist
    val testRDD = RDD(2).repartition(numPartitions).persist

    val numValidate = validateRDD.count
    val numTest = testRDD.count

    println("训练集包含 " + numTrainData + " 条数据，来自 "
      + numUsers + " 个用户和 " + numProducts + "件产品")
    println("训练集分为模型训练集和模型验证集，模型训练集包含 " + trainingRDD.count + "条数据，模型验证集包含 " + validateRDD.count + "条数据")
    println("测试集包含" + numTest + " 条数据")

    val bestModel: MatrixFactorizationModel = trainModel(trainingRDD, validateRDD, numValidate)

    val testRmse = computeRmse(bestModel, testRDD, numTest)

    println("测试集的RMSE为 " + testRmse + "\n----------测试完毕----------")

    // 关闭context
    sc.stop()
  }

  // 使用训练集和验证集训练模型参数
  def trainModel(trainingData: RDD[Rating], validationData: RDD[Rating], numValidation: Long): MatrixFactorizationModel = {
    val ranks = List(8, 12)
    val lambdas = List(0.1, 10.0)
    val numIters = List(10, 20)

    var bestModel: Option[MatrixFactorizationModel] = None
    var bestValidationRmse = Double.MaxValue
    var bestRank = 0
    var bestLambda = -1.0
    var bestNumIter = -1

    for (rank <- ranks; lambda <- lambdas; numIter <- numIters) {
      val model = ALS.train(trainingData, rank, numIter, lambda)
      val validataionRmse = computeRmse(model, validationData, numValidation)
      println("对于使用参数（rank = " + rank + "; numIter = " +
        numIter + "; lambda = " + lambda + " ）训练得到的模型，验证集得出的RMSE为 " + validataionRmse)

      if (validataionRmse < bestValidationRmse) {
        bestModel = Some(model)
        bestValidationRmse = validataionRmse
        bestRank = rank
        bestLambda = lambda
        bestNumIter = numIter
      }
    }
    println("模型最优参数为（rank = " + bestRank + "; numIter = " +
      bestNumIter + "; lambda = " + bestLambda + ")")
    bestModel.get
  }

  // 计算rmse均方根误差
  def computeRmse(model: MatrixFactorizationModel, dataset: RDD[Rating], n: Long): Double = {
    val predictions: RDD[Rating] = model.predict(dataset.map(x => (x.user, x.product)))
    val predictionsAndRatings = predictions.map { x =>
      ((x.user, x.product), x.rating)
    }.join(dataset.map { x =>
      ((x.user, x.product), x.rating)
    }
      ).values
    math.sqrt(predictionsAndRatings.map(x => (x._1 - x._2) * (x._1 - x._2)).reduce(_ + _) / n)
  }
}