package org.xsbt.lib

import java.io.File
import net.liftweb.http.LiftRules
import net.liftweb.http.Req
import net.liftweb.common._
import scala.collection.mutable.LinkedHashMap
import _root_.org.xsbt.versions._
import SBTVersions._
import _root_.org.apache.commons.io.{FileUtils, FilenameUtils}
import net.liftweb.http._
import net.liftweb.util.Helpers._
import net.liftweb.util.Props

object URIs extends LiftRules.DispatchPF {

  val Host = Props.get("hostname", "xsbt.org")
  val Stub = Host + "/"
  val Proxy = "proxy." + Stub

  val HttpProxy = "http://" + Proxy

  val HttpStub = "http://" + Stub
  val FtpStub = "ftp://" + Stub
  val GopherStub = "gopher://" + Stub

//  ----------------------------------------

  def createResponse(path: String) = {
    val name = FilenameUtils.getName(path)
    val ext = FilenameUtils.getExtension(path)
    val body = FileUtils.readFileToByteArray(
      SBTVersions.getFile(path))

    ext match {
      case "jar" | "exe" =>
        AttachmentResponse(body, name)
      case _ =>
        InlineResponse(body, name)
    }
  }

  lazy val localCache = {
    val files = {
      (for(v <- versions; e <- exts) yield {
        val f = v.filename + e
        (HttpStub + f) -> (v -> createResponse("files/repo/"+ f))
      })
    }

    val news = {
      (for(v <- versions; r <- reps if r._1(v)) yield {
        val f = v.filename.replace(".jar", r._2)
        (HttpStub + f) -> (v -> createResponse("files/repo/"+ f))
      })
    }

    val gruj =
      (for(v <- versions if v.grujed) yield {
        val f = "gruj_vs_"+ v.filename
        (HttpStub + f) -> (v -> createResponse("files/gruj/"+ f))
      })

    (files ++ news ++ gruj).toMap
  }

  lazy val proxyCache =
    (for(v <- versions if v.grujed) yield
      (HttpProxy + v.filename) -> RedirectResponse(v.url)
    ).toMap

  def isDefinedAt(r: Req) = {
    val fullUrl = r.hostAndPath + r.uri
    localCache.isDefinedAt(fullUrl) ||
    proxyCache.isDefinedAt(fullUrl)
  }

  def apply(r: Req) = {
    val fullUrl = r.hostAndPath + r.uri
    () => (
      localCache.get(fullUrl).map{ case(v, res) =>
        new SBTResponse(r, v, res).serveResponse()
      } orElse

      proxyCache.get(fullUrl)
    )
  }

// -----------------------------------------------------------------------------

  val ExpiryInterval: Long = 1.day
}

class SBTResponse(r: Req, v: SBTVersion, res: InMemoryResponse) {
  val modDate = v.modTime
  val nowDate = millis
  val expDate = nowDate + URIs.ExpiryInterval

  def getExpHeaders = List(
    "Last-Modified" -> toInternetDate(modDate),
    "Expires" -> toInternetDate(expDate),
    "Date" -> toInternetDate(nowDate),
    "Pragma" -> "",
    "Cache-Control" -> ""
  )

  def serveResponse() =
    r.testFor304(modDate) openOr {
      res.copy(headers = res.headers ::: getExpHeaders)
    }
}
