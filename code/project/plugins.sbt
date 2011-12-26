// +------------------------------------------------------------------------------------+
// | SBT Eclipse (https://github.com/typesafehub/sbteclipse)                            |
// | Creates .project and .classpath files for easy Eclipse project imports             |
// |                                                                                    |
// | See also: Eclipse downloads (http://www.eclipse.org/downloads/)                    |
// | See also: Scala IDE downloads (http://download.scala-ide.org/)                     |
// +------------------------------------------------------------------------------------+

resolvers += Classpaths.typesafeResolver

addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "2.0.0-M2")

// +-------------------------------------------------------------------------------------+
// | XSBT Web plugin (https://github.com/siasia/xsbt-web-plugin)                         |
// | Implements SBT 0.7.x Web project actions: "jetty-run" -> "container:start", etc ... |
// +-------------------------------------------------------------------------------------+

libraryDependencies <+= sbtVersion(v => "com.github.siasia" %% "xsbt-web-plugin" % (v+"-0.2.10"))

// +------------------------------------------------------------------------------------+
// | CoffeeScripted SBT (https://github.com/softprops/coffeescripted-sbt)               |
// | Automates "compilation" of coffeescript files (/src/main/*.coffee) into javascript |
// |                                                                                    |
// | See also: Coffeescript reference (http://jashkenas.github.com/coffee-script/)      |
// +------------------------------------------------------------------------------------+

resolvers += "less is" at "http://repo.lessis.me"

addSbtPlugin("me.lessis" % "coffeescripted-sbt" % "0.2.0")

// +------------------------------------------------------------------------------------+
// | Less SBT (https://github.com/softprops/less-sbt)                                   |
// | Automates "compilation" of less files (/src/main/*.less) into css                  |
// |                                                                                    |
// | See also: LESS reference (http://lesscss.org/)                                     |
// +------------------------------------------------------------------------------------+

resolvers += "less is" at "http://repo.lessis.me"

addSbtPlugin("me.lessis" % "less-sbt" % "0.1.3")

// +------------------------------------------------------------------------------------+
// | SBT Scalariform (https://github.com/typesafehub/sbt-scalariform)                   |
// | Performs source code formatting                                                    |
// |                                                                                    |
// | See also: Scalariform reference (http://mdr.github.com/scalariform/)               |
// +------------------------------------------------------------------------------------+

resolvers += Classpaths.typesafeResolver

addSbtPlugin("com.typesafe.sbtscalariform" % "sbtscalariform" % "0.3.0")
