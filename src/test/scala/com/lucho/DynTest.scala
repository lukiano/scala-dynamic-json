package com.lucho

import org.scalatest.FunSuite
import Dyn._

class DynTest extends FunSuite {

  test("Dynamic building") {
    val d = new DynObject()
    d.name = "Bruce"
    d.name = 3
    assert(3 === d.name.as[Int])

    d.place.latitude = 35
    d.place.longitude = 35
    d.firstArray = Array[String]("a", "b", "c")
    d.secondArray = List[Int](1, 2, 3)
    assert(3 === d.secondArray(2).as[Int])
    d.secondArray(2) = 4
    assert(4 === d.secondArray(2).as[Int])

    assert(Array(1, 2, 4) === d.secondArray.asArray[Int])

  }

  test("Serialization") {
    val d = new DynObject()
    d.name = "Bruce"
    d.name = 3
    d.place.latitude = 35
    d.place.longitude = 35

    val serialized = DynJson4s.serialize(d)
    val e: Dyn = DynJson4s.deserialize(serialized)
    assert(dyn2Map(d) === e.as[Map[String, Any]])

  }

}
