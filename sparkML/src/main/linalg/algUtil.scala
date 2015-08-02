package main.linalg

import breeze.linalg.{SparseVector => BSV, Vector => BV}
import org.apache.spark.mllib.linalg.{SparseVector, Vector}

/**
 * Created by zhy on 2015/8/2 0002.
 */
object AlgUtil {
  /**
   * 向量->Breeze向量
   * @param v Vector
   * @return Breeze Vector
   */
  def VtoB(v: Vector): BV[Double] =
    new BSV[Double](v.toSparse.indices, v.toSparse.values, v.toSparse.size)

  /**
   * 稀疏向量->Breeze向量
   * @param v SparseVector
   * @return Breeze Vector
   */
  def StoB(v: SparseVector): BV[Double] = new BSV[Double](v.indices, v.values, v.size)
}
