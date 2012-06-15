import sbt._
import Keys._

object BuildSettings {
  val coreSets = Defaults.defaultSettings ++ Seq(
    organization  := "Element d.o.o.",

    scalaVersion  := "2.9.2",
    scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "UTF-8", "-optimise", "-Yrepl-sync"),

    unmanagedSourceDirectories in Test := Nil,

    resolvers := Seq("Element Releases" at "http://maven.element.hr/nexus/content/repositories/releases/")
  )

  val bsCore = coreSets ++ Seq(
    name          := "Xsbt.org - Core",
    version       := "0.0.1"
  )

  val bsLift = coreSets ++ Seq(
    name          := "Xsbt.org - Lift",
    version       := "0.0.1",

    unmanagedSourceDirectories in Compile <<= (scalaSource in Compile)( _ :: Nil)
  )
}

object Dependencies {
  val jetty = "org.eclipse.jetty" % "jetty-webapp" % "7.6.0.v20120127" % "container"

  val liftVersion = "2.4"
  val liftweb = Seq(
    "net.liftweb" % "lift-webkit_2.9.1" % liftVersion % "compile"
  )

  val etbLift = "hr.element.etb" % "etb-lift_2.9.1" % "0.0.20"

  val scalaTime = "org.scala-tools.time" % "time_2.9.1" % "0.5"
  val commonsIo = "commons-io" % "commons-io" % "2.3"

  val logback = "ch.qos.logback" % "logback-classic" % "1.0.6" % "compile->default"
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

  val depsCore = Seq()

  val depsLift = liftweb ++ Seq(
    jetty,
    etbLift,
    commonsIo,
    scalaTime,
    logback
  )

  lazy val core = Project(
    "Core",
    file("core"),
    settings = bsCore ++ Seq(
      libraryDependencies := depsCore
    )
  )

  lazy val lift = Project(
    "Lift",
    file("lift"),
    settings = bsLift ++ Seq(
      libraryDependencies := depsLift
    ) ++ webSettings  ++ Seq(
      port in container.Configuration := 8120,
      scanDirectories in Compile := Nil
    ) ++ coffeeSettings ++ Seq(
      resourceManaged in (Compile, coffee) <<= (webappResources in Compile)(_.get.head / "static" / "coffee")
    ) ++ lessSettings ++ Seq(
      mini in (Compile, less) := true,
      resourceManaged in (Compile, less) <<= (webappResources in Compile)(_.get.head / "static" / "less")
    )
  ) dependsOn(core)
}
