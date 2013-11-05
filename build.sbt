organization  := "com.lucho"

name := "scala-dynamic-json"

scalaVersion := "2.10.3"

version := "0.2"

libraryDependencies ++= Seq(
  "org.json4s"               %%  "json4s-native"     % "3.2.5",
  "org.json4s"               %%  "json4s-jackson"    % "3.2.5",
  "org.scalatest"            %   "scalatest_2.10"    % "2.0" % "test"
)

