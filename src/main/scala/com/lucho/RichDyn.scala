package com.lucho

import scala.reflect.ClassTag

class RichDyn(val d: Dyn) extends AnyVal {

  def as[T]: T = Dyn.as[T](d)

  def asArray[T: ClassTag]: Array[T] = Dyn.asArray[T](d)

}
