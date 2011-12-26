import sbt._
import Keys._

object BuildSettings {
  import Formatting._

  val coreSets = Defaults.defaultSettings ++ Seq(
    organization  := "Element d.o.o.",
    scalaVersion  := "2.9.1",
    scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "UTF-8", "-optimise") // , "-Yrepl-sync"
  ) ++ formatSettings

  val bsCore = coreSets ++ Seq(
    name          := "Xsbt.org - Core",
    version       := "0.0.1"
  )

  val bsLift = coreSets ++ Seq(
    name          := "Xsbt.org - Lift",
    version       := "0.0.1"
  )
}

object Dependencies {
  val jetty  = "org.eclipse.jetty" % "jetty-webapp" % "8.1.0.RC2" % "container"

  val liftVersion = "2.4-RC1"
  val liftweb = Seq(
    "net.liftweb" %% "lift-webkit" % liftVersion % "compile"
  )

  val scalatime = "org.scala-tools.time" %% "time" % "0.5"
  val commonsIo = "commons-io" % "commons-io" % "2.1"

  val logback = "ch.qos.logback" % "logback-classic" % "1.0.0" % "compile->default"

  val scalatest = "org.scalatest" %% "scalatest" % "1.6.1" % "test"
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

  val depsCore = Seq(
    scalatest
  )

  val depsLift = liftweb ++ Seq(
    jetty,
    commonsIo,
    scalatime,
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

object Formatting {
  // Scalariform plugin
  import com.typesafe.sbtscalariform._
  import ScalariformPlugin._
  import scalariform.formatter.preferences._

  ScalariformKeys.preferences := FormattingPreferences()
    .setPreference(AlignParameters, false)
    .setPreference(AlignSingleLineCaseStatements, false)
    .setPreference(AlignSingleLineCaseStatements.MaxArrowIndent, 40)
    .setPreference(CompactControlReadability, true)
    .setPreference(CompactStringConcatenation, false)
    .setPreference(DoubleIndentClassDeclaration, true)
    .setPreference(FormatXml, false)
    .setPreference(IndentLocalDefs, false)
    .setPreference(IndentPackageBlocks, false)
    .setPreference(IndentSpaces, 2)
    .setPreference(IndentWithTabs, false)
    .setPreference(MultilineScaladocCommentsStartOnFirstLine, true)
    .setPreference(PlaceScaladocAsterisksBeneathSecondAsterisk, true)
    .setPreference(PreserveDanglingCloseParenthesis, false)
    .setPreference(PreserveSpaceBeforeArguments, false)
    .setPreference(RewriteArrowSymbols, false)
    .setPreference(SpaceBeforeColon, false)
    .setPreference(SpaceInsideBrackets, false)
    .setPreference(SpaceInsideParentheses, false)
    .setPreference(SpacesWithinPatternBinders, true)

  lazy val formatSettings =
    ScalariformPlugin.defaultScalariformSettings
}
