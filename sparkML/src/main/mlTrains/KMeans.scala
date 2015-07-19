package main.mlTrains

import org.apache.spark.mllib.clustering.KMeans
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.{SparkConf, SparkContext}

/**
 * Created by zhy on 2015/7/6 0006.
 */
object KMeans {
  def main(args: Array[String]) {
    if (args.length < 2) {
      System.err.println("Usage :  <hdfs path> <file save path>")
      System.exit(1)
    }

    val conf = new SparkConf().setMaster("local").setAppName("KMeans")
    val sc = new SparkContext(conf)
    //读取数据，并转化成密集向量
    val data = sc.textFile(args(1)).map { lines =>
      Vectors.dense(lines.split(" ").map(_.toDouble))
    }
    //实例化KMeans类，该类用来做算法的一些设置和运行算法
    val km = new KMeans()
    //设置聚类中心点个数为2，最大迭代次数为20，run方法开始运算，传入测试数据集
    val model = km.setK(2).setMaxIterations(20).run(data)
    //输出得到的模型的聚类中心
    println("cluster num :" + model.k)
    for (i <- model.clusterCenters) {
      println(i.toString)
    }

    println("----------------------------------------")

    //使用自定义的数据对模型进行测试，让其判断这些向量分别属于哪个聚类中心
    println("Vector 0.2 0.2 0.2 is closing to : " + model.predict(Vectors.dense("0.2 0.2 0.2".split(" ").map(_.toDouble))))
    println("Vector 0.25 0.25 0.25 is closing to : " + model.predict(Vectors.dense("0.25 0.25 0.25".split(" ").map(_.toDouble))))
    println("Vector 8 8 8 is closing to : " + model.predict(Vectors.dense("8 8 8".split(" ").map(_.toDouble))))

    println("----------------------------------------")

    //将测试数据再次作为预测数据传入模型中进行预测
    val result0 = model.predict(data).collect().foreach(println)

    println("----------------------------------------")

    //数据得到的结果，保存在hdfs中（直接打印也可以）
    val result = data.map { lines =>
      val res = model.predict(lines)
      lines + " clustingCenter: " + res
    }.saveAsTextFile(args(2))
  }
}
