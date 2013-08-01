organization  := "com.lucho"

name := "scala-dynamic-json"

scalaVersion := "2.10.2"

version := "0.1"

libraryDependencies ++= Seq(
  "org.json4s"               %%  "json4s-native"     % "3.2.4",
  "org.json4s"               %%  "json4s-jackson"    % "3.2.4",
  "org.scalatest"            %   "scalatest_2.10"    % "2.0.M5b" % "test"
)

