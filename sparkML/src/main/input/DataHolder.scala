package main.input

import org.apache.spark.mllib.recommendation.Rating
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.rdd.RDD

/**
 * Created by zhy on 2015/8/3 0003.
 */
trait DataHolder extends Serializable {
  def getLRData(): RDD[LabeledPoint]

  def getData(): RDD[Rating]

  def getDataDesc: Unit
}
