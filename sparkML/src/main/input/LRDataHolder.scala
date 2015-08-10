package main.input

import main.util.SparkEnv
import org.apache.spark.mllib.linalg.SparseVector
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.rdd.RDD

import scala.collection.mutable.ArrayBuffer

/**
 * Created by zhy on 2015/8/3 0003.
 */

/**
 * 逻辑回归数据集
 * @param dataDirectoryPath 数据集根目录
 */
class LRDataHolder(dataDirectoryPath: String) extends DataHolder with Serializable {
  private val data: RDD[LabeledPoint] = loadDataFromFile
  private val dimensions = 1000

  def loadDataFromFile: RDD[LabeledPoint] = {
    val data = SparkEnv.sc.textFile(dataDirectoryPath + "data.txt")
      .map { line =>
      var indices = ArrayBuffer[Int]()
      var values = ArrayBuffer[Double]()
      val fields = line.split(" ")
      val label = fields(0).toDouble
      fields.foreach { field =>
        val featureI = field.split(":")
        if (featureI.length == 2) {
          indices += featureI(0).toInt
          values += featureI(1).toDouble
        }
      }
      new LabeledPoint(label, new SparseVector(dimensions, indices.toArray, values.toArray))
    }
    data
  }

  override def getLRData = data

  override def getData = ???

  override def getDataDesc = println("数据集包含" + data.count + "条数据")
}
