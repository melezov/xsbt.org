package org.xsbt
package snippet

import lib.URIs

import net.liftweb.util.PassThru
import hr.element.etb.lift.lib.SinkHole

class HideOnHttps {
  val render =
    if (URIs.isHttps()) {
      SinkHole
    }
    else {
      PassThru
    }
}
