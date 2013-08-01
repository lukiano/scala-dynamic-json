package com.lucho

import spray.httpx.Json4sJacksonSupport

object Json4sJacksonSupport extends Json4sJacksonSupport {

  implicit val json4sJacksonFormats = DynJson4s.json4sJacksonFormats

}
