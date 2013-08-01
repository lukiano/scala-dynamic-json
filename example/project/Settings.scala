import sbt._
import Keys._

object Settings {

  lazy val basicSettings = seq(
    organization  := "com.lucho",
    name := "scala-dynamic-json-spray-example",
    scalaVersion  := "2.10.2",
    resolvers    ++= Dependencies.resolutionRepos,
    scalacOptions := Seq(
      "-encoding",
      "utf8",
      "-g:vars",
      "-feature",
      "-unchecked",
      "-deprecation",
      "-target:jvm-1.7",
      "-language:postfixOps",
      "-language:implicitConversions",
      "-Xlog-reflective-calls"
    )
  )
}
