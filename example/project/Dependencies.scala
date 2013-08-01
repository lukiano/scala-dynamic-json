import sbt._

object Dependencies extends Build {

  val sprayVersion = "1.2-20130712"

  val akkaVersion = "2.2.0"

  val json4sVersion = "3.2.4"

  val resolutionRepos = Seq(
      "Nexus Public Repository"        at "http://nexus.despegar.it:8080/nexus/content/groups/public",
      "Nexus Snapshots Repository"     at "http://nexus.despegar.it:8080/nexus/content/repositories/snapshots",
      "Nexus Proxies Repository"       at "http://nexus.despegar.it:8080/nexus/content/groups/proxies",
      "Typesafe repository"            at "http://repo.typesafe.com/typesafe/releases/",
      "spray repo"                     at "http://repo.spray.io/",
      "spray nightlies repo"           at "http://nightlies.spray.io/",
      "Mark Schaake"                   at "http://markschaake.github.com/snapshots",
      "Sourceforge Releases"           at "https://oss.sonatype.org/content/repositories/sourceforge-releases"
  )

  val sprayCan      =     "io.spray"                 %   "spray-can"         % sprayVersion
  val sprayRouting  =     "io.spray"                 %   "spray-routing"     % sprayVersion
  val sprayTestkit  =     "io.spray"                 %   "spray-testkit"     % sprayVersion
  val sprayClient   =     "io.spray"                 %   "spray-client"      % sprayVersion

  val slf4j         =     "org.slf4j"                %   "slf4j-api"         % "1.7.5"
  val logback       =     "ch.qos.logback"           %   "logback-classic"   % "1.0.13"

  val akkaActor     =     "com.typesafe.akka"        %%  "akka-actor"        % akkaVersion
  val akkaAgent     =     "com.typesafe.akka"        %%  "akka-agent"        % akkaVersion
  val akkaSlf4j     =     "com.typesafe.akka"        %%  "akka-slf4j"        % akkaVersion
  val akkaTestkit   =     "com.typesafe.akka"        %%  "akka-testkit"      % akkaVersion
  val scalaTest     =     "org.scalatest"            %   "scalatest_2.10"    % "2.0.M5b"
  val scalaMock     =     "org.scalamock" %% "scalamock-scalatest-support"   % "3.0.1"

  val sprayJson     =     "io.spray"                 %%  "spray-json"        % "1.2.4"
  val json4sNative  =     "org.json4s"               %%  "json4s-native"     % json4sVersion
  val json4sJackson =     "org.json4s"               %%  "json4s-jackson"    % json4sVersion

  val scalaDynamicJson =  "com.lucho"                %%  "scala-dynamic-json"% "0.1"

  def compile   (deps: ModuleID*): Seq[ModuleID] = deps map (_ % "compile")
  def provided  (deps: ModuleID*): Seq[ModuleID] = deps map (_ % "provided")
  def test      (deps: ModuleID*): Seq[ModuleID] = deps map (_ % "test")
  def runtime   (deps: ModuleID*): Seq[ModuleID] = deps map (_ % "runtime")
  def container (deps: ModuleID*): Seq[ModuleID] = deps map (_ % "container")


}
