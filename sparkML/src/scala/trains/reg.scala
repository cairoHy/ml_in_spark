package scala.trains

import java.io._

import scala.collection.mutable.ArrayBuffer
import scala.io.Source

/**
 * Created by zhy on 2015/8/21 0021.
 */

object readLine {
  def getLine = {
    val sr = Source.fromURL("http://www.baidu.com", "UTF-8")
    val tokens = sr.mkString.split("\\s+")
    val lines = sr.getLines
    for (line <- lines) println(line)
  }

  def travelsal(dir: File): Iterator[File] = {
    val children = dir.listFiles.filter(_.isDirectory)
    children.toIterator ++ children.toIterator.flatMap(travelsal _)
  }

  def main(args: Array[String]) {
    //    this.getLine
    val f: File = new File("C:/")
    scala.tools.nsc.io.Directory
    for (file <- travelsal(f)) println(file)
  }
}

object FileOp {
  //1
  def reverseLine(file: String) = {
    val sr = Source.fromFile(file)
    val lines = sr.getLines.toArray.reverse
    val pw = new PrintWriter(file)

    for (line <- lines)
      pw.write(line + "\n")

    sr.close
    pw.close
  }

  //2
  //此处有Bug，文件会被清空
  def tab2Space(file: String) = {
    val lines = Source.fromFile(file).getLines
    val res = for (line <- lines) yield line.replace("\\t", "    ")
    val pw = new PrintWriter(file)
    res.foreach { line =>
      pw.write(line + "\n")
    }

    pw.close
  }

  //3
  def printWord(file: String) = Source.fromFile(file).mkString.split("\\s+").foreach(a => if (a.length > 12) println(a))

  //4
  def readNum(file: String) = {
    val nums = Source.fromFile(file).getLines.mkString.split("\\s+")
    var sum = 0d
    nums.foreach(sum += _.toDouble)
    val ave = sum / nums.length
    val max = nums.max
    val min = nums.min
    println("average: " + ave + "\n" +
      "sum: " + sum + "\n" +
      "max: " + max + "\n" +
      "min: " + min + "\n")
  }

  //5
  def powWrite(file: String) = {
    val pw = new PrintWriter(file)
    for (n <- 0 to 20) {
      val t = BigDecimal(2).pow(n)
      pw.write(t.toString + "\t\t" + (1 / t).toString + "\n")
    }
    pw.close
  }

  //6
  //正则表达式匹配转义字符
  def regMatch(file: String) = {
    val sr = Source.fromFile(file).mkString
    val pattern = "^\\\"[,\\s\\w\\\\\\\"]*\\\"".r
    pattern.findAllIn(sr).foreach(println(_))
  }

  //7
  //并看不懂啊
  def nonNumMatch(file: String) = {
    val sr = Source.fromFile(file).mkString
    val pattern = """[^((\d+\.]{0,1}\d+^\s+]+""".r
    pattern.findAllIn(sr).foreach(println(_))
  }

  //8
  //...
  def printSrc(url:String) = {
    val sr = Source.fromURL(url).mkString
    val pattern = """<img[^>]+(src\s*=\s*"[^>^"]+")[^>]*>""".r
    for( pattern(str) <- pattern.findAllIn(sr) ) println(str)
  }

  def main(args: Array[String]) {
    val f = "d:\\1.txt"
    val url = "http://www.baidu.com"
    //    this.reverseLine(f)
    //    this.tab2Space(f)
    //    this.printWord(f)
    //    this.readNum(f)
    //    this.powWrite(f)
//    this.regMatch(f)
    this.printSrc(url)
  }
}

//9
class Prob9 {
  def calcNum(path:String) :Int = {
    val dir = new File(path)

    def subdirs(dir:File):Iterator[File]={
      val children = dir.listFiles().filter(_.getName.endsWith("class"))
      children.toIterator ++ dir.listFiles().filter(_.isDirectory).toIterator.flatMap(subdirs _)
    }

    subdirs(dir).length
  }

  def main(args: Array[String]) {
    val path = "d:\\"
    println(this.calcNum(path))
  }

}

//10
class mPerson(val name: String) extends Serializable {
  val friends = new ArrayBuffer[mPerson]()

  def addFriend(him: mPerson) = friends += him

  override def toString(): String = {
    var str = "My name is " + name + ", and my friend' name is "
    friends.foreach(str += _.name + ",")
    str
  }

}

object pro extends App {
  val p1 = new mPerson("ivan")
  val p2 = new mPerson("Peter")
  val p3 = new mPerson("Tom")
  p1.addFriend(p2)
  p1.addFriend(p3)
  println(p1)

  val out = new ObjectOutputStream(new FileOutputStream("test.txt"))
  out.writeObject(p1)
  out.close

  val in = new ObjectInputStream(new FileInputStream("test.txt"))
  val p = in.readObject().asInstanceOf[mPerson]
  println(p)
  in.close
}