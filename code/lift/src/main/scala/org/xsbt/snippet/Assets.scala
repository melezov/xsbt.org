package org.xsbt
package snippet

import hr.element.etb.lift.{ snippet => etbs }

object SharedJS extends etbs.StaticJS {
  override val root = "shared"

  override val defaultVersions = Map(
    "jquery"             -> "1.9.0"
  , "jquery.tablesorter" -> "2.0.5b"
  , "jquery.scrollTo"    -> "1.4.3.1"
  )
}

object SharedCSS extends etbs.StaticCSS {
  override val root = "shared"
}

object GeneratedJS extends etbs.GeneratedJS

object GeneratedCSS extends etbs.GeneratedCSS
