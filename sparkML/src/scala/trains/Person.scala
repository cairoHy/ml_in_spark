package scala.trains

/**
 * Created by zhy on 2015/7/1 0001.
 */
class Person1(fn: String, ln: String, a: Int) {

  //  @scala.reflect.BeanProperty
  var firstName = fn

  //  @scala.reflect.BeanProperty
  var lastName = ln

  //  @scala.reflect.BeanProperty
  var age = a

  override def toString =
    "[Person firstName:" + firstName + " lastName:" + lastName +
      " age:" + age + " ]"
}
