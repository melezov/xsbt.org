package bootstrap.liftweb

import net.liftweb._
import sitemap._
import http._
import common._

import net.liftweb._
import util._
import Helpers._
import common._
import http._
import sitemap._

import org.xsbt.lib.URIs

class Boot {
  LiftRules.addToPackages("org.xsbt")

  LiftRules.setSiteMap(SiteMap(
    Menu.i("Index") / "index"
  ))

  LiftRules.stripComments.default.set(() => false)

  LiftRules.htmlProperties.default.set((r: Req) => {
    new XHtmlInHtml5OutProperties(r.userAgent)
  })

  LiftRules.early.append(_.setCharacterEncoding("UTF-8"))

  LiftRules.statelessDispatchTable.append(URIs)
}
