resolvers := Seq(
  "Element Nexus" at "http://repo.element.hr/nexus/content/groups/public/"
, Resolver.url("Element Nexus (Ivy)",
    url("http://repo.element.hr/nexus/content/groups/public/"))(Resolver.ivyStylePatterns)
)

externalResolvers <<= resolvers map { r =>
  Resolver.withDefaultResolvers(r, mavenCentral = false)
}

// +------------------------------------------------------------------------------------+
// | SBT Eclipse (https://github.com/typesafehub/sbteclipse)                            |
// | Creates .project and .classpath files for easy Eclipse project imports             |
// |                                                                                    |
// | See also: Eclipse downloads (http://www.eclipse.org/downloads/)                    |
// | See also: Scala IDE downloads (http://download.scala-ide.org/)                     |
// +------------------------------------------------------------------------------------+

addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "2.1.2")

// +-------------------------------------------------------------------------------------+
// | XSBT Web plugin (https://github.com/JamesEarlDouglas/xsbt-web-plugin)               |
// | Implements SBT 0.7.x Web project actions: "jetty-run" -> "container:start", etc ... |
// +-------------------------------------------------------------------------------------+

libraryDependencies += "com.github.siasia" %% "xsbt-web-plugin" % "0.12.0-0.2.11.1"

// +------------------------------------------------------------------------------------+
// | CoffeeScripted SBT (https://github.com/softprops/coffeescripted-sbt)               |
// | Automates "compilation" of coffeescript files (/src/main/*.coffee) into javascript |
// |                                                                                    |
// | See also: Coffeescript reference (http://jashkenas.github.com/coffee-script/)      |
// +------------------------------------------------------------------------------------+

addSbtPlugin("me.lessis" % "coffeescripted-sbt" % "0.2.3")

// +------------------------------------------------------------------------------------+
// | Less SBT (https://github.com/softprops/less-sbt)                                   |
// | Automates "compilation" of less files (/src/main/*.less) into css                  |
// |                                                                                    |
// | See also: LESS reference (http://lesscss.org/)                                     |
// +------------------------------------------------------------------------------------+

addSbtPlugin("me.lessis" % "less-sbt" % "0.1.10")
