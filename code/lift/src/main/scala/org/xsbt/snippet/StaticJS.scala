package org.xsbt
package snippet

object StaticJS extends hr.element.etb.lift.snippet.StaticJS {
  override val root = "static"

  override val defaultVersions = Map(
    "jquery"             -> "1.7.2"
  , "jquery.tablesorter" -> "2.0.5b"
  , "jquery.scrollTo"    -> "1.4.2"
  )
}
