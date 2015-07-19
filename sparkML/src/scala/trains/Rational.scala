package scala.trains

/**
 * Created by zhy on 2015/6/30 0030.
 */
class Rational(n: Int, d: Int) {
  def this(d: Int) = {
    this(0, d)
  }

  private def gcd(x: Int, y: Int): Int = {
    if (x == 0) y
    else if (x < 0) gcd(-x, y)
    else if (y < 0) gcd(x, -y)
    else gcd(y % x, x)
  }

  private val g = gcd(n, d)

  val number: Int = n / g
  val denom: Int = d / g

  def +(that: Rational) =
    new Rational(number * that.denom + that.number * denom, denom * that.denom)

  def -(that: Rational) =
    new Rational(number * that.denom - that.denom * number, denom * that.denom)

  def *(that: Rational) =
    new Rational(number * that.number, denom * that.denom)

  def /(that: Rational) =
    new Rational(number * that.denom, denom * that.number)

  def unary_~(): Rational =
    new Rational(denom, number)

  override def toString() =
    "Rational:[" + number + "/" + denom + "]"
}

object runRational extends App {
  val r1 = new Rational(1, 3)
  val r2 = new Rational(2, 5)
  val r3 = r1 - r2
  val r4 = r1 + r2
  val r5 = ~r4
  Console.println("r1 = " + r1)
  Console.println("r2 = " + r2)
  Console.println("r3 = r1 - r2 = " + r3)
  Console.println("r4 = r1 + r2 = " + r4)
  Console.println("r5 = " + r5)
}
