package scala.trains

/**
 * Created by zhy on 2015/8/17 0017.
 */
package random {

package object random {
  var seed: Int = _
  var a = BigDecimal(1664525)
  var b = BigDecimal(1013904223)
  var n = 32

  def nextInt(): Int = {
    val tmp = (seed * a + b) % BigDecimal(2).pow(n)
    seed = tmp.toInt
    seed
  }

  def nextDouble(): Double = {
    val tmp = (seed * a + b) % BigDecimal(2).pow(n)
    seed = tmp.toInt
    seed.toDouble
  }

  def setSeed(seed: Int): Unit = {
    this.seed = seed
  }
}

}

package test {

import scala.trains.random.random

object test extends App {
  random.seed = 4
  println(random.nextDouble())
  println(random.nextDouble())
  println(random.nextDouble())
  println(random.nextDouble())
  println(random.nextInt())
  println(random.nextInt())
  println(random.nextInt())
  println(random.nextInt())
}

}

package hash {

package object transfer {

  import java.util.{HashMap => JavaHashMap}

  import scala.collection.immutable.HashMap

  def J2S(map: JavaHashMap[String, String]): HashMap[String, String] = {
    var sMap = new HashMap[String, String]
    for (key <- map.keySet.toArray) sMap += (key.toString -> map.get(key))
    sMap
  }
}

}

object test2 extends App {
  var password = Console.readLine()
  if (password.equals("secret")) println("hello " + System.getProperty("user.name"))
  else System.err.println("password wrong")
}