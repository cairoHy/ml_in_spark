package main.input.recommend

/**
 * Created by zhy on 2015/7/18 0018.
 */

import main.util.SparkEnv
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.mllib.recommendation.Rating
import org.apache.spark.rdd.RDD

import scala.collection.mutable.ArrayBuffer
;

/**
 * @param dataDirectoryPath NetFlix数据集根目录
 */
abstract class NetflixDataHolder(dataDirectoryPath: String) extends RecDataHolder {
  protected val productsIDsToNameMap = loadIDsToProductnameMapFromADirectory()

  /**
   * 从 "movie_titles.txt" 中获取电影名和ID的映射
   * @return Map: movieID -> title
   */
  protected def loadIDsToProductnameMapFromADirectory(): Map[Int, String] = {
    val sc = SparkEnv.sc
    val movies = sc.textFile(dataDirectoryPath + "movie_titles.txt").map { line =>
      val fields = line.split(",")
      // format: (movieID, movieName)
      (fields(0).toInt, fields(2) + " (" + fields(1) + ")")
    }.collect.toMap
    movies
  }
}

/**
 * 从一个文件读取NetFilx数据 文件格式: movieID>,userID,rating,date.
 * @param dataDirectoryPath NetFlix数据集目录
 * @param filename 文件名
 */
class NetflixDataHolder4OneFile(dataDirectoryPath: String, filename: String = "ratings.txt") extends NetflixDataHolder(dataDirectoryPath) with Serializable {
  protected val ratings = {
    val sc = SparkEnv.sc
    val ratingsRDD = sc.textFile(dataDirectoryPath + filename).map {
      line => val fields = line.split(",")
        (Rating(fields(1).toInt, fields(0).toInt, fields(2).toDouble))
    }
    ratingsRDD
  }
}

/**
 * 从一个目录下所有文件读取NetFilx数据 文件格式: movieID>,userID,rating,date.
 * @param dataDirectoryPath NetFlix数据集目录
 */
class NetflixDataHolder4Directory(dataDirectoryPath: String) extends NetflixDataHolder(dataDirectoryPath) with Serializable {
  protected val ratings = loadRatingsFromADirectory()

  protected def loadRatingsFromADirectory(): RDD[Rating] = {
    val conf = new Configuration()
    val hdfs = FileSystem.get(conf)
    val dataPath = new Path(dataDirectoryPath + "training_set")
    val stats = hdfs.listStatus(dataPath)
    var fileList = new ArrayBuffer[String]

    for (stat <- stats) fileList += stat.getPath.toString
    val ratingsRDDsArray = fileList.map(filePath => loadRatingsFromOneFile(filePath))
    val ratings = SparkEnv.sc.union(ratingsRDDsArray)
    ratings.persist.coalesce(77)
  }

  protected def loadRatingsFromOneFile(absoluteFilePath: String): RDD[Rating] = {
    val ratingsTxtRDD = SparkEnv.sc.textFile(absoluteFilePath)
    val movieIDLine = ratingsTxtRDD.first()
    val movieID = movieIDLine.split(":")(0).toInt

    val ratingsRDD = ratingsTxtRDD.map(line => if (line == movieIDLine) {
      Rating(-1, -1, -1)
    } else {
      val fields = line.split(",")
      (Rating(fields(0).toInt, movieID, fields(1).toDouble))
    })
    ratingsRDD.filter(rat => rat.user >= 0)
  }
}
