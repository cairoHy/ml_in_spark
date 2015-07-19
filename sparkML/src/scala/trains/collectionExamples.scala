package scala.trains

import org.junit.Test


/**
 * Created by zhy on 2015/7/11 0011.
 */
object collectionExamples {
  @Test def simpleOptionTest = {
    val footballTeamsAFCEast =
      Map(
        "New England" -> "Patriots",
        "New York" -> "Jets",
        "Buffalo" -> "Bills",
        "Miami" -> "Dolphins",
        "Los Angeles" -> null
      )

    assert(footballTeamsAFCEast.get("Miami") == Some("Dolphins"))
    assert(footballTeamsAFCEast.get("Miami").get == "Dolphins")
    assert(footballTeamsAFCEast.get("Los Angeles") == Some(null))
    assert(footballTeamsAFCEast.get("sss") == None)
  }

  @Test def optionWithPM = {
    val footballTeamsAFCEast =
      Map(
        "New England" -> "Patriots",
        "New York" -> "Jets",
        "Buffalo" -> "Bills",
        "Miami" -> "Dolphins"
      )

    def show(value: Option[String]) = {
      value match {
        case Some(x) => x
        case None => "no team found"
      }
    }

    assert(show(footballTeamsAFCEast.get("Miami")) == "Dolphins")

  }

  def main(args: Array[String]): Unit = {
    Console.println("simpleOptionTest----------")
    simpleOptionTest;
    Console.println("optinWithPM----------")
    optionWithPM;

  }
}

class TupleTest {

  import java.util.Date

  import org.junit._
  import Assert._

  @Test def simpleTuples() = {
    val tedsStartingDateWithScala = Date.parse("3/7/2006")

    val tuple = ("Ted", "Scala", tedsStartingDateWithScala)

    assertEquals(tuple._1, "Ted")
    assertEquals(tuple._2, "Scala")
    assertEquals(tuple._3, tedsStartingDateWithScala)
  }
}
