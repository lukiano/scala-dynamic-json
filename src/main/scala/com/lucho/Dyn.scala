package com.lucho

import scala.language.dynamics
import scala.language.implicitConversions
import scala.reflect.ClassTag

sealed trait Dyn extends scala.Dynamic {

  def updateDynamic(key: String)(value: Dyn)

  def selectDynamic(key: String): Dyn

  def applyDynamic(key: String)(args: Any*): Dyn

}

/*
sealed trait Converter[T] {
  def to(t: T): Dyn
  def from(d: Dyn): T
}
*/

final class DynObject private[lucho] (private[lucho] val map: collection.mutable.Map[String, Dyn]) extends Dyn {

  def this() = this(collection.mutable.Map[String, Dyn]())

  def selectDynamic(key: String): Dyn =
    if (map.contains(key)) map(key) else {
      val value = new DynObject
      map += key -> value
      value
    }

  def updateDynamic(key: String)(value: Dyn) {
    map += key -> value
  }

  def applyDynamic(key: String)(args: Any*): Dyn =
    if (map.contains(key)) {
      map(key).asInstanceOf[DynArray].arr(args(0).asInstanceOf[Int])
    } else {
      throw new NoSuchElementException(s"For field $key at position ${args(0).asInstanceOf[Int]}")
      val value = new DynArray(Array[Dyn]())
      map += key -> value
      value
    }

  override def toString = map.toString()

}

sealed abstract class DynField(field: Any) extends Dyn {

  def selectDynamic(key: String): Dyn = throw new Exception("This field is not an object")

  def updateDynamic(key: String)(value: Dyn) {
    throw new Exception("This field is not an object")
  }

  def applyDynamic(key: String)(args: Any*): Dyn = throw new Exception("This field is not an object")

  final override def toString = field.toString
}

final class DynString(val s: String) extends DynField(s)

final class DynInt(val i: BigInt) extends DynField(i)

final class DynDouble(val d: Double) extends DynField(d)

final class DynDecimal(val d: BigDecimal) extends DynField(d)

final class DynBool(val b: Boolean) extends DynField(b)

final class DynArray(val arr: Array[Dyn]) extends Dyn {

  def selectDynamic(key: String): Dyn = throw new Exception("This field is not an object")

  def updateDynamic(key: String)(value: Dyn) {
    throw new Exception("This field is not an object")
  }

  def applyDynamic(key: String)(args: Any*): Dyn = {
    if (key == "update") {
      val oldElem: Dyn = arr(args(0).asInstanceOf[Int])
      val newElem: Dyn = Dyn.to(args(1))
      arr(args(0).asInstanceOf[Int]) = newElem
      oldElem
    } else {
      throw new NoSuchMethodException(s"No method named $key")
    }
  }

  override def toString = arr.mkString("[", ",", "]")
}

final class DynProduct(val p: Product) extends DynField(p)

object Dyn {

  implicit def string2Dyn(s: String): DynString = new DynString(s)
  implicit def int2Dyn(i: Int): DynInt = new DynInt(i)
  implicit def long2Dyn(l: Long): DynInt = new DynInt(l)
  implicit def bigint2Dyn(i: BigInt): DynInt = new DynInt(i)
  implicit def double2Dyn(d: Double): DynDouble = new DynDouble(d)
  implicit def decimal2Dyn(d: BigDecimal): DynDecimal = new DynDecimal(d)
  implicit def bool2Dyn(b: Boolean): DynBool = new DynBool(b)

  def dyn2String(d: DynString): String = d.s
  def dyn2Int(d: DynInt): Int = d.i.toInt
  def dyn2Long(d: DynInt): Long = d.i.toLong
  def dyn2BigInt(d: DynInt): BigInt = d.i
  def dyn2Decimal(d: DynDecimal): BigDecimal = d.d
  def dyn2Double(d: DynDouble): Double = d.d
  def dyn2Boolean(d: DynBool): Boolean = d.b
  def dyn2Product(d: DynProduct): Product = d.p

  implicit def product2Dyn(p: Product) = new DynProduct(p)

  implicit def array2Dyn[T](ar: Array[T]):DynArray  = new DynArray(ar.map(to))
  implicit def list2Dyn[T](list: List[T]): DynArray = new DynArray(list.map(to).toArray)
  implicit def map2Dyn(map: Map[String, Any]): DynObject = {
    val mutableMap = collection.mutable.Map[String, Dyn]()
    map.foreach {
      case (key: String, value: Any) => mutableMap += key -> to(value)
    }
    new DynObject(mutableMap)
  }

  def dyn2Array[T: ClassTag](d: DynArray): Array[T] = d.arr.map(elem => from(elem).asInstanceOf[T]).toArray
  def dyn2List[T](d: DynArray): List[T] = d.arr.map(elem => from(elem).asInstanceOf[T]).toList
  def dyn2Map(d: DynObject): Map[String, Any] = d.map.map { case (key: String, value: Dyn) => (key, from(value)) }.toMap

  private[lucho] def to(any: Any): Dyn = any match {
    case s: String => string2Dyn(s)
    case i: Int => int2Dyn(i)
    case l: Long => long2Dyn(l)
    case i: BigInt => bigint2Dyn(i)
    case d: Double => double2Dyn(d)
    case d: BigDecimal => decimal2Dyn(d)
    case b: Boolean => bool2Dyn(b)
    case m: Map[_, _] => map2Dyn(m.asInstanceOf[Map[String, Any]])
    case arr: Array[_] => array2Dyn(arr)
    case lis: List[_] => list2Dyn(lis)
    case p: Product => product2Dyn(p)
    case dyn: Dyn => dyn
  }

  private[Dyn] def from(dyn: Dyn): Any = dyn match {
    case s: DynString => dyn2String(s)
    case i: DynInt => dyn2Int(i)
    case d: DynDouble => dyn2Double(d)
    case d: DynDecimal => dyn2Decimal(d)
    case b: DynBool => dyn2Boolean(b)
    case arr: DynArray => dyn2Array(arr)
    case obj: DynObject => dyn2Map(obj)
    case pro: DynProduct => dyn2Product(pro)
  }

  def <> = new DynObject()   //cannot do {} like javascript

}

