import sbt._
import Keys._

object BuildSettings {
  val coreSets = Defaults.defaultSettings ++ Seq(
    organization  := "Element d.o.o."

  , scalaVersion  := "2.9.2"
  , scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "UTF-8", "-optimise", "-Yrepl-sync")

  , resolvers += "Element Releases" at "http://maven.element.hr/nexus/content/repositories/releases/"

  , unmanagedSourceDirectories in Test := Nil
  )

  val bsCore = coreSets ++ Seq(
    name          := "Xsbt.org - Core"
  , version       := "0.0.1"
  )

  val bsLift = coreSets ++ Seq(
    name          := "Xsbt.org - Lift"
  , version       := "0.0.1"
  )
}

object Dependencies {
  val jetty = "org.eclipse.jetty" % "jetty-webapp" % "7.6.5.v20120716" % "container"
  val orbit = "org.eclipse.jetty.orbit" % "javax.servlet" % "2.5.0.v201103041518" % "container" artifacts Artifact("javax.servlet", "jar", "jar")

  val liftWebKit = "net.liftweb" % "lift-webkit_2.9.1" % "2.4"
  val etbLift = "hr.element.etb" %% "etb-lift" % "0.0.21"

  val scalaTime = "org.scala-tools.time" % "time_2.9.1" % "0.5"
  val commonsIo = "commons-io" % "commons-io" % "2.3"

  val logback = "ch.qos.logback" % "logback-classic" % "1.0.6"
}

object XOBuild extends Build {
  import BuildSettings._
  import Dependencies._

  // Web plugin
  import com.github.siasia.WebPlugin._
  import com.github.siasia.PluginKeys._

  // Coffeescript plugin
  import coffeescript.Plugin._
  import CoffeeKeys._

  // Less plugin
  import less.Plugin._
  import LessKeys._

  val depsLift = Seq(
    jetty
  , orbit
  , liftWebKit
  , etbLift
  , commonsIo
  , scalaTime
  , logback
  )

  lazy val core = Project(
    "Core"
  , file("core")
  , settings = bsCore
  )

  lazy val lift = Project(
    "Lift",
    file("lift"),
    settings = bsLift ++ Seq(
        libraryDependencies := depsLift
      ) ++ webSettings  ++ Seq(
        port in container.Configuration := 8120
      , scanDirectories in Compile := Nil
      ) ++ coffeeSettings ++ Seq(
        resourceManaged in (Compile, coffee) <<= (webappResources in Compile)(_.get.head / "static" / "coffee")
      ) ++ lessSettings ++ Seq(
        mini in (Compile, less) := true
      , resourceManaged in (Compile, less) <<= (webappResources in Compile)(_.get.head / "static" / "less")
      )
  ) dependsOn(core)
}
