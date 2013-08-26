import sbt._
import Keys._

object BuildSettings {
  // Eclipse plugin
  import com.typesafe.sbteclipse.plugin.EclipsePlugin._

  val defaultSettings = Defaults.defaultSettings ++ Seq(
    organization  := "hr.element.xsbt"

  , scalaVersion  := "2.10.2"
  , scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "UTF-8", "-optimise", "-Yrepl-sync")

  , resolvers := Seq("Element Nexus" at "http://repo.element.hr/nexus/content/groups/public")
  , externalResolvers := Resolver.withDefaultResolvers(resolvers.value, mavenCentral = false)

  , unmanagedSourceDirectories in Test := Nil
  , EclipseKeys.createSrc := EclipseCreateSrc.Default + EclipseCreateSrc.Resource
  )

  val bsCore = defaultSettings ++ Seq(
    name    := "Core"
  , version := "0.0.0-SNAPSHOT"
  )

  val bsLift = defaultSettings ++ Seq(
    name    := "Lift"
  , version := "0.0.0-SNAPSHOT"
  )
}

object Dependencies {
  val jetty = "org.eclipse.jetty" % "jetty-webapp" % "8.1.12.v20130726" % "container"
  val orbit = "org.eclipse.jetty.orbit" % "javax.servlet" % "3.0.0.v201112011016" % "container" artifacts Artifact("javax.servlet", "jar", "jar")

  val liftWebKit = "net.liftweb" %% "lift-webkit" % "2.5.1"
  val etbLift = "hr.element.etb" %% "etb-lift" % "0.1.4"

  val scalaTime = "com.github.nscala-time" %% "nscala-time" % "0.6.0"

  val commonsIo = "commons-io" % "commons-io" % "2.4"

  val logback = "ch.qos.logback" % "logback-classic" % "1.0.13"
}

object XOBuild extends Build {
  import BuildSettings._
  import Dependencies._

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

  // Web plugin
  import com.earldouglas.xsbtwebplugin.WebPlugin._
  import com.earldouglas.xsbtwebplugin.PluginKeys._

  // Coffeescript plugin
  import coffeescript.Plugin._
  import CoffeeKeys._

  // Less plugin
  import less.Plugin._
  import LessKeys._

  lazy val lift = Project(
    "Lift",
    file("lift"),
    settings = bsLift ++ Seq(
        libraryDependencies := depsLift
      ) ++ webSettings  ++ Seq(
        port in container.Configuration := 8120
      , scanDirectories in Compile := Nil
      ) ++ coffeeSettings ++ Seq(
        resourceManaged in (Compile, coffee) := (webappResources in Compile).value.get.head / "static" / "coffee-js"
      ) ++ lessSettings ++ Seq(
        mini in (Compile, less) := true
      , resourceManaged in (Compile, less) := (webappResources in Compile).value.get.head / "static" / "less-css"
      )
  ) dependsOn(core)
}
