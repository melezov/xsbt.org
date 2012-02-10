package org.xsbt.snippet

import hr.element.etb.lift.snippet.StaticScripts

object Scripts extends StaticScripts {
  override val root = "static"

  override val defaultVersions = Map(
    "jquery"             -> "1.7.1",
    "jquery.tablesorter" -> "2.0.5b",
    "jquery.scrollTo"    -> "1.4.2"
  )
}
