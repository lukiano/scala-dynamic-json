resolvers ++= Seq(
    "Nexus Public Repository" at "http://nexus.despegar.it:8080/nexus/content/groups/public",
    "Nexus Snapshots Repository" at "http://nexus.despegar.it:8080/nexus/content/repositories/snapshots",
    "Nexus Proxies Repository" at "http://nexus.despegar.it:8080/nexus/content/groups/proxies",
    "sonatype-releases" at "https://oss.sonatype.org/content/repositories/releases/",
    "Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/"
  )

addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.4.0")

addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.8.8")

addSbtPlugin("io.spray" % "sbt-revolver" % "0.6.2")

addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.7.3")

addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "0.2.0")

addSbtPlugin("com.despegar.sbt" %% "sbt-cloudia-plugin" % "0.2.1")

libraryDependencies += "de.djini" %% "xsbt-reflect" % "0.0.3" //needed this way to be read from Nexus
