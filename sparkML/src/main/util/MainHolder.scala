package main.util

import main.input.{DataHolder, DataHolderFactory}
import main.recommender.{Recommender, RecommenderFactory}

/**
 * Created by zhy on 2015/7/19 0019.
 */

/**
 * 初始化并获取DataHolder和Recommender
 */
object MainHolder {
  private var recommender: Option[Recommender] = None
  private var dataHolder: Option[DataHolder] = None

  /**
   * 初始化DataHolder数据源和rcommender算法
   * @param conf 配置管理类
   */
  def setUp(conf: Conf): Unit = {
    val dataHolderNameToFactoryMap = DataHolderFactory.dataHolderFactories.map(holder => holder.getName -> holder).toMap
    val dataHolderStr: String = conf.data()
    dataHolder = Some(dataHolderNameToFactoryMap.get(dataHolderStr).get.getDataHolderInstance(conf))

    val recommenderNameToFactoryMap = RecommenderFactory.recommenderFactories.map(rec => rec.getName -> rec).toMap
    val recommenderStr: String = conf.method()
    recommender = Some(recommenderNameToFactoryMap.get(recommenderStr).get.getRecommender(conf))
  }

  /**
   * 计算该推荐算法对于测试集的均方根误差RMSE
   * @return Unit
   */
  def calculateRMSE() = getRecommenderInstance.getRMSE

  /**
   *
   * @return 推荐算法实例
   */
  def getRecommenderInstance(): Recommender = {
    recommender match {
      case Some(rec) => rec
      case None => throw new MainHolderNotInitializedException
    }
  }

  /**
   *
   * @return 数据源实例
   */
  def getDataHolder(): DataHolder = {
    dataHolder match {
      case Some(holder) => holder
      case None => throw new MainHolderNotInitializedException
    }
  }

  class MainHolderNotInitializedException extends Exception

}
