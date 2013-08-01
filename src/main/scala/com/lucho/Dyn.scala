package com.lucho

import scala.language.dynamics
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

  /*
  def update(i: Int, x: Dyn) {
    arr.update(i, x)
  }
  */
  //def apply(i: Int): Dyn = arr(i)

  override def toString = arr.mkString("[", ",", "]")
}

object Dyn {

  implicit def string2Dyn(s: String): DynString = new DynString(s)
  implicit def int2Dyn(i: Int): DynInt = new DynInt(i)
  implicit def long2Dyn(l: Long): DynInt = new DynInt(l)
  implicit def bigint2Dyn(i: BigInt): DynInt = new DynInt(i)
  implicit def double2Dyn(d: Double): DynDouble = new DynDouble(d)
  implicit def decimal2Dyn(d: BigDecimal): DynDecimal = new DynDecimal(d)
  implicit def bool2Dyn(b: Boolean): DynBool = new DynBool(b)

  def dyn2String(d: Dyn): String = d.asInstanceOf[DynString].s
  def dyn2Int(d: Dyn): Int = d.asInstanceOf[DynInt].i.toInt
  def dyn2Long(d: Dyn): Long = d.asInstanceOf[DynInt].i.toLong
  def dyn2BigInt(d: Dyn): BigInt = d.asInstanceOf[DynInt].i
  def dyn2Decimal(d: Dyn): BigDecimal = d.asInstanceOf[DynDecimal].d
  def dyn2Double(d: Dyn): Double = d.asInstanceOf[DynDouble].d
  def dyn2Boolean(d: Dyn): Boolean = d.asInstanceOf[DynBool].b

  /*
  implicit object String2Dyn extends Converter[String, DynString] { def to(s: String) = string2Dyn(s); def from(d: DynString) = dyn2String(d) }
  implicit object Int2Dyn extends Converter[Int, DynInt] { def to(i: Int) = int2Dyn(i); def from(d: DynInt) = dyn2Int(d) }
  implicit object Long2Dyn extends Converter[Long, DynInt] { def to(l: Long) = long2Dyn(l); def from(d: DynInt) = dyn2Long(d) }
  implicit object BigInt2Dyn extends Converter[BigInt, DynInt] { def to(i: BigInt) = bigint2Dyn(i); def from(d: DynInt) = dyn2BigInt(d) }
  implicit object Double2Dyn extends Converter[Double, DynDouble] { def to(d: Double) = double2Dyn(d); def from(d: DynDouble) = dyn2Double(d) }
  implicit object Decimal2Dyn extends Converter[BigDecimal, DynDecimal] { def to(d: BigDecimal) = decimal2Dyn(d); def from(d: DynDecimal) = dyn2Decimal(d) }
  implicit object Bool2Dyn extends Converter[Boolean, DynBool] { def to(b: Boolean) = bool2Dyn(b); def from(d: DynBool) = dyn2Boolean(d) }
  */

  /*
  implicit object String2Dyn extends Converter[String] { def to(s: String) = string2Dyn(s); def from(d: Dyn) = dyn2String(d) }
  implicit object Int2Dyn extends Converter[Int] { def to(i: Int) = int2Dyn(i); def from(d: Dyn) = dyn2Int(d) }
  implicit object Long2Dyn extends Converter[Long] { def to(l: Long) = long2Dyn(l); def from(d: Dyn) = dyn2Long(d) }
  implicit object BigInt2Dyn extends Converter[BigInt] { def to(i: BigInt) = bigint2Dyn(i); def from(d: Dyn) = dyn2BigInt(d) }
  implicit object Double2Dyn extends Converter[Double] { def to(d: Double) = double2Dyn(d); def from(d: Dyn) = dyn2Double(d) }
  implicit object Decimal2Dyn extends Converter[BigDecimal] { def to(d: BigDecimal) = decimal2Dyn(d); def from(d: Dyn) = dyn2Decimal(d) }
  implicit object Bool2Dyn extends Converter[Boolean] { def to(b: Boolean) = bool2Dyn(b); def from(d: Dyn) = dyn2Boolean(d) }
  */

  /*
  implicit def array2Dyn[T](ar: Array[T])(implicit converter: Converter[T]):DynArray  = new DynArray(ar.map(converter.to))
  implicit def list2Dyn[T](list: List[T])(implicit converter: Converter[T]): DynArray = new DynArray(list.map(converter.to).toArray)

  def dyn2Array[T: ClassTag](d: Dyn)(implicit converter: Converter[T]): Array[T] = d.asInstanceOf[DynArray].arr.map(converter.from).toArray
  def dyn2List[T](d: Dyn)(implicit converter: Converter[T]): List[T] = d.asInstanceOf[DynArray].arr.map(converter.from).toList
  */

  implicit def array2Dyn[T](ar: Array[T]):DynArray  = new DynArray(ar.map(to))
  implicit def list2Dyn[T](list: List[T]): DynArray = new DynArray(list.map(to).toArray)
  implicit def map2Dyn(map: Map[String, Any]): DynObject = {
    val mutableMap = collection.mutable.Map[String, Dyn]()
    map.foreach {
      case (key: String, value: Any) => mutableMap += key -> to(value)
    }
    new DynObject(mutableMap)
  }

  def dyn2Array[T: ClassTag](d: Dyn): Array[T] = d.asInstanceOf[DynArray].arr.map(elem => from(elem).asInstanceOf[T]).toArray
  def dyn2List[T](d: Dyn): List[T] = d.asInstanceOf[DynArray].arr.map(elem => from(elem).asInstanceOf[T]).toList
  def dyn2Map(d: Dyn): Map[String, Any] = d.asInstanceOf[DynObject].map.map { case (key: String, value: Dyn) => (key, from(value)) }.toMap

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
  }

  private[Dyn] def from(dyn: Dyn): Any = dyn match {
    case s: DynString => dyn2String(s)
    case i: DynInt => dyn2Int(i)
    case d: DynDouble => dyn2Double(d)
    case d: DynDecimal => dyn2Decimal(d)
    case b: DynBool => dyn2Boolean(b)
    case arr: DynArray => dyn2Array(arr)
    case obj: DynObject => dyn2Map(obj)
  }

}

