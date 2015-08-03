package main.input.recommend

import main.util.SparkEnv
import org.apache.spark.mllib.recommendation.Rating
import org.apache.spark.rdd.RDD

/**
 * Created by zhy on 2015/7/18 0018.
 */

/**
 * @param dataDirectoryPath Yahoo数据集根目录
 */
class YahooDataHolder(dataDirectoryPath: String) extends RecDataHolder with Serializable {
  override protected val ratings: RDD[Rating] = loadRatingsFromAFile()
  override protected val productsIDsToNameMap: Map[Int, String] = loadIDsToProductnameMapFromADirectory(dataDirectoryPath)

  /**
   * 从文件中读取Yahoo数据集评分
   * @return RDD[Rating]
   */
  protected def loadRatingsFromAFile(): RDD[Rating] = {
    val ratings = SparkEnv.sc.textFile(dataDirectoryPath + "data.txt")
      .filter(line => formatSpace(line).split(" ").length >= 3)
      .map { line =>
      val lineFormat = formatSpace(line)
      val fields = lineFormat.split(" ")
      (Rating(fields(0).toInt, fields(1).toInt, fields(2).toDouble))
    }
    ratings
  }

  /**
   * 去除字符串中多于一个连续的空格
   * @param line 输入字符串
   * @return 去除多余空格后的字符串
   */
  protected def formatSpace(line: String): String = {
    line.replaceAll("\\s+", " ")
  }

  /**
   *
   * @param dataDirectoryPath Yahoo数据集根目录
   * @return Map:musicID -> musicName
   */
  protected def loadIDsToProductnameMapFromADirectory(dataDirectoryPath: String): Map[Int, String] = {
    null
  }
}
