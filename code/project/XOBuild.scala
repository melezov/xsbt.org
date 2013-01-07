import sbt._
import Keys._

object BuildSettings {
  // Eclipse plugin
  import com.typesafe.sbteclipse.plugin.EclipsePlugin._

  val defaultSettings = Defaults.defaultSettings ++ Seq(
    organization  := "hr.element.xsbt"

  , scalaVersion  := "2.9.2"
  , scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "UTF-8", "-optimise", "-Yrepl-sync")

  , resolvers := Seq("Element Nexus" at "http://repo.element.hr/nexus/content/groups/public")

  , externalResolvers <<= resolvers map { r =>
      Resolver.withDefaultResolvers(r, mavenCentral = false)
    }

  , unmanagedSourceDirectories in Test := Nil

  , EclipseKeys.createSrc := EclipseCreateSrc.Default + EclipseCreateSrc.Resource
  )

  val bsCore = defaultSettings ++ Seq(
    name    := "Core"
  , version := "0.0.1"
  )

  val bsLift = defaultSettings ++ Seq(
    name    := "Lift"
  , version := "0.0.1"
  )
}

object Dependencies {
  val jetty = "org.eclipse.jetty" % "jetty-webapp" % "7.6.8.v20121106" % "container"
  val orbit = "org.eclipse.jetty.orbit" % "javax.servlet" % "2.5.0.v201103041518" % "container" artifacts Artifact("javax.servlet", "jar", "jar")

  val liftWebKit = "net.liftweb" %% "lift-webkit" % "2.5-M3"
  val etbLift = "hr.element.etb" %% "etb-lift" % "0.0.23-P0"

  val scalaTime = "org.scalaj" %% "scalaj-time" % "0.6"

  val commonsIo = "commons-io" % "commons-io" % "2.4"

  val logback = "ch.qos.logback" % "logback-classic" % "1.0.9"
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
