package com.lucho

import org.json4s._

object DynJson4s {

  def serialize: PartialFunction[Any, JValue] = {
    case d: Dyn => d match {
      case obj: DynObject => JObject(obj.map.toList.map {case (key, dyn) => JField(key, serialize(dyn)) })
      case field: DynField => field match {
        case str: DynString => JString(str.s)
        case i: DynInt => JInt(i.i)
        case dou: DynDouble => JDouble(dou.d)
        case dec: DynDecimal => JDecimal(dec.d)
        case bol: DynBool => JBool(bol.b)
      }
      case array: DynArray => JArray(array.arr.map(serialize).toList)
    }
    case null => JNull
  }

  def deserialize: PartialFunction[JValue, Dyn] = {
    case JNull => null
    case JString(str) => new DynString(str)
    case JInt(i) => new DynInt(i)
    case JDouble(d) => new DynDouble(d)
    case JDecimal(d) => new DynDecimal(d)
    case JBool(b) => new DynBool(b)
    case JArray(list) => new DynArray(list.map(deserialize).toArray)
    case JObject(fields) =>
      val map = collection.mutable.Map[String, Dyn]()
      for (field <- fields) {
        val key: String = field._1
        val value: JValue = field._2
        map += key -> deserialize(value)
      }
      new DynObject(map)
  }

  object DynSerializer extends CustomSerializer[Dyn](formats => (deserialize, serialize))

  val json4sJacksonFormats = DefaultFormats + DynSerializer

}
