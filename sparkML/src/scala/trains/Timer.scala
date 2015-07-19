package scala.trains

/**
 * Created by zhy on 2015/6/30 0030.
 */
object Timer {
  def oncePerSec(callback: () => Unit): Unit = {
    while (true) {
      callback()
      Thread.sleep(1000)
    }
  }

  def timeflies(): Unit = {
    Console.print("time flies")
  }

  def main(args: Array[String]): Unit = {
    oncePerSec(timeflies)
  }
}

object Timer3 {
  def oncePerSec(callback: () => Unit): Unit = {
    while (true) {
      callback()
      Thread.sleep(1000)
    }
  }

  def main(args: Array[String]): Unit = {
    oncePerSec(() => Console.println("time flies"))
  }
}

object Timer4 {
  def periodCall(second: Int, callback: () => Unit): Unit = {
    while (true) {
      callback()
      Thread.sleep(second * 1000)
    }
  }

  def main(args: Array[String]): Unit = {
    periodCall(1, () => Console.println("time flies"))
  }
}

object helloIte {
  def main(args: Array[String]): Unit = {
    args.filter((arg: String) => arg.startsWith("G"))
      .foreach((arg: String) => Console.println("Found" + arg))
  }
}