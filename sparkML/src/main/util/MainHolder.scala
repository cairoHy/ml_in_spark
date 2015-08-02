package main.util

import main.factory.{Algorithm, AlgorithmFactory}
import main.input.{DataFactory, DataHolder}

/**
 * Created by zhy on 2015/7/19 0019.
 */

/**
 * 初始化并获取DataHolder和Recommender
 */
object MainHolder {
  private var recommender: Option[Algorithm] = None
  private var dataHolder: Option[DataHolder] = None

  /**
   * 初始化DataHolder数据源和rcommender算法
   * @param conf 配置管理类
   */
  def setUp(conf: Conf): Unit = {
    val dataHolderNameToFactoryMap = DataFactory.dataHolderFactories.map(holder => holder.getName -> holder).toMap
    val dataHolderStr: String = conf.data()
    dataHolder = Some(dataHolderNameToFactoryMap.get(dataHolderStr).get.getDataHolderInstance(conf))

    val recommenderNameToFactoryMap = AlgorithmFactory.AlgList.map(rec => rec.getName -> rec).toMap
    val recommenderStr: String = conf.method()
    recommender = Some(recommenderNameToFactoryMap.get(recommenderStr).get.getAlg(conf))
  }

  /**
   * 计算该推荐算法对于测试集的均方根误差RMSE
   * @return Unit
   */
  def calculateRMSE() = getAlgInstance.getRMSE

  /**
   *
   * @return 机器学习算法实例
   */
  def getAlgInstance(): Algorithm = {
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
