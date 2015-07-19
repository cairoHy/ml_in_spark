package scala.trains

import java.net.{MalformedURLException, URL}

/**
 * Created by zhy on 2015/7/1 0001.
 */
class Control(possibleURL: String) {
  var url =
    try {
      new URL(possibleURL)
    }
    catch {
      case ex: MalformedURLException =>
        new URL("www.tednew.com")
    }
}

object Application {
  def main(args: Array[String]) {
    /*tryWithLogging {
      generateExcept
    }*/
    System.out.println("exiting main()")
  }

  def generateExcept(): Unit = {
    System.out.println("generate lkjlk")
    throw new Exception("Generated exception")
  }

  /*def tryWithLogging (s: => _) {
    try {
      s
    }
    catch {
      case ex: Exception =>
        // where would you like to log this?
        // I choose the console window, for now
        ex.printStackTrace()
    }
  }*/

}


/**
 * the left-arrow means "assignment" in Scala
 * also learn about the "to" method of int
 */
object App2 {
  def main(args: Array[String]) {
    for (i <- 1 to 10) {
      System.out.println(i)
    }
  }
}

object App3 {
  def main(args: Array[String]) = {
    val path = "C:\\Users\\zhy\\Documents\\study\\602\\CFAlogrithm@Spark\\源码\\sparkML\\src\\main\\trains"
    val filesHere = (new java.io.File(path)).listFiles()
    for {
      file <- filesHere
      if file.isFile
      if file.getName.endsWith(".scala")
    } Console.println("Found" + file)
  }
}

object App4 {
  def main(args: Array[String]) {
    val names = Array("n ae", "l zz", "g o", "m a", "z hy")
    for {
      name <- names
      firstName = name.substring(0, name.indexOf(' '))
    } Console.println("first name is " + firstName)
  }
}

/**
 * nested iteration
 */
object App5 {
  def main(args: Array[String]) {
    val dir = "C:\\Users\\zhy\\Documents\\study\\602\\CFAlogrithm@Spark\\源码\\sparkML\\src\\main\\trains"
    val pattern = ".*object.*"

    grep(pattern, new java.io.File(dir))
  }

  def grep(pattern: String, dir: java.io.File) = {
    val filesHere = dir.listFiles
    for {
      file <- filesHere
      if (file.getName.endsWith(".scala") || file.getName.endsWith(".java"))
      line <- scala.io.Source.fromFile(file).getLines
      if line.trim.matches(pattern)
    } println(line)
  }
}

/**
 * learn about the pattern matching
 */
object App6 {
  def main(args: Array[String]) {
    for (arg <- args)
      arg match {
        case "Java" => println("java is nice...")
        case "Scala" => println("Scala is cool...")
        case "Ruby" => println("Ruby is for wimps...")
        case _ => println("What are you, a VB programmer?")
      }
  }
}

object App7 {
  def main(args: Array[String]) {
    println(describe("true"))
    println(describe(5))
    println(describe(true))
  }

  def describe(x: Any) = x match {
    case 5 => "five"
    case true => "truth"
    case "hello" => "hi"
    case _ => "sth else"
  }
}