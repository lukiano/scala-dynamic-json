import sbt._
import Keys._

object Build extends Build {
  import Settings._
  import Dependencies._

  lazy val root = Project("scala-dynamic-json-spray-example",file("."))
    .settings(basicSettings: _*)
    .settings(libraryDependencies ++=
       compile(sprayCan, sprayRouting, sprayClient, sprayJson, slf4j, logback,
               akkaActor, akkaAgent, akkaSlf4j, json4sJackson, scalaDynamicJson) ++
       test(scalaTest, akkaTestkit, sprayTestkit))

}
