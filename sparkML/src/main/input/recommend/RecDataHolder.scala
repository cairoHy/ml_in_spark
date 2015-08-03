package main.input.recommend

import main.input.DataHolder
import org.apache.spark.mllib.recommendation.Rating
import org.apache.spark.rdd.RDD

/**
 * Created by zhy on 2015/7/18 0018.
 */

/**
 * 推荐算法数据接口，可获取相应的Rating和ID2Name映射
 */

trait RecDataHolder extends DataHolder with Serializable {
  protected val ratings: RDD[Rating]
  protected val productsIDsToNameMap: Map[Int, String]

  override def getLRData = ???

  override def getData = getRatings

  override def getDataDesc = printRatingDesc

  def getRatings(): RDD[Rating] = ratings

  def printRatingDesc = println("数据集包含 " + ratings.count + " 条数据，来自 "
    + ratings.map(_.user).distinct.count + " 个用户和 " + ratings.map(_.product).distinct.count + "件产品")

  def getIDToProductnameMap(): Map[Int, String] = productsIDsToNameMap

  def getNumOfProducts(): Int = productsIDsToNameMap.keys.max + 1
}