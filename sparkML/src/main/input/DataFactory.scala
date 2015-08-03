package main.input

import main.input.recommend.{NetflixDataHolder4Directory, NetflixDataHolder4OneFile, RecDataHolder, YahooDataHolder}
import main.util.Conf

/**
 * Created by zhy on 2015/7/19 0019.
 */

/**
 * 数据集工厂
 */
trait DataFactory {
  def getName: String

  def getDesc: String

  def getInstance(conf: Conf): DataHolder
}

object DataFactory {
  val dataHolderList: List[DataFactory] = List(YahooFac, NetFlix2Fac, NetFlix1Fac, LR)
}

object YahooFac extends DataFactory {
  override def getName: String = "Yahoo"

  override def getDesc: String = "数据源：Yahoo数据集，单个文件\n" +
    "数据格式：userID itemID(musicID) rating(0-100)"

  override def getInstance(conf: Conf): RecDataHolder = {
    println(getDesc)
    new YahooDataHolder(conf.dir())
  }
}

object NetFlix1Fac extends DataFactory {
  override def getName: String = "NetFlixInFile"

  override def getDesc: String = "数据源：NetFlix数据集，单个文件\n数据格式：???"

  override def getInstance(conf: Conf): RecDataHolder = {
    println(getDesc)
    new NetflixDataHolder4OneFile(conf.dir())
  }
}

object NetFlix2Fac extends DataFactory {
  override def getName: String = "NetFlixInDirectory"

  override def getDesc: String = "数据源：NetFlix数据集，目录\n" +
    "数据格式：每个文件第一行为UserID，其余每行：movieID,rating(0-5),time"

  override def getInstance(conf: Conf): RecDataHolder = {
    println(getDesc)
    new NetflixDataHolder4Directory(conf.dir())
  }
}

object LR extends DataFactory {
  override def getName: String = "LR"

  override def getInstance(conf: Conf): DataHolder = {
    println(getDesc)
    new LRDataHolder(conf.dir())
  }

  override def getDesc: String = "数据源：逻辑回归数据集，单个文件\n" +
    "数据格式：每行 label 特征维度1:特征数据1 ...... 特征维度n:特征数据n"
}