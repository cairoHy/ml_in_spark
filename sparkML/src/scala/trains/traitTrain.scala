package scala.trains

import java.beans.PropertyChangeEvent

/**
 * Created by zhy on 2015/7/2 0002.
 */
class Person(var firstName: String, var lastName: String, var age: Int)
  extends Object with BoundPropertyBean {

  def setFirstName(newvalue: String) = {
    val oldvalue = firstName
    firstName = newvalue
    firePropertyChange("firstName", oldvalue, newvalue)
  }

  def setLastName(newvalue: String) = {
    val oldvalue = lastName
    lastName = newvalue
    firePropertyChange("lastName", oldvalue, newvalue)
  }

  def setAge(newvalue: Int) = {
    val oldvalue = age
    age = newvalue
    firePropertyChange("age", oldvalue, newvalue)
  }

  override def toString = {
    "[Person: firstName=" + firstName +
      " lastName=" + lastName + " age=" + age + "]"
  }
}

object PCL extends java.beans.PropertyChangeListener {
  override def propertyChange(pce: PropertyChangeEvent): Unit = {
    Console.println("Bean changed its " + pce.getPropertyName() +
      " from " + pce.getOldValue() +
      " to " + pce.getNewValue())
  }
}


trait BoundPropertyBean {

  import java.beans._

  val pcs = new PropertyChangeSupport(this)

  def addPropertyChangeListener(pcl: PropertyChangeListener) =
    pcs.addPropertyChangeListener(pcl)

  def removePropertyChangeListener(pcl: PropertyChangeListener) =
    pcs.removePropertyChangeListener(pcl)

  def firePropertyChange(name: String, oldValue: Any, newValue: Any): Unit =
    pcs.firePropertyChange(new PropertyChangeEvent(this, name, oldValue, newValue))
}

object AppListener {
  def main(args: Array[String]) {
    val p = new Person("ja", "zhy", 28)

    p.addPropertyChangeListener(PCL)

    p.setFirstName("abc")
    p.setAge(15)

    Console.println(p)

  }
}

/**
 *
 * @tparam A
 * learn about the trait
 */
trait Ordered[A] {
  def compare(that: A): Int

  def <(that: A): Boolean = (this compare that) < 0

  def >(that: A): Boolean = (this compare that) > 0

  def <=(that: A): Boolean = (this compare that) <= 0

  def >=(that: A): Boolean = (this compare that) >= 0

  def compareTo(that: A): Int = compare(that)
}