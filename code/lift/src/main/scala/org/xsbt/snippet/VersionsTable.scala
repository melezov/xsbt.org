package org.xsbt.snippet

import _root_.org.xsbt.versions._
import _root_.org.scala_tools.time.Imports._

import org.xsbt.lib.URIs

import scala.xml._
import net.liftweb.http.SessionVar
import net.liftweb.util.BindPlus._
import net.liftweb.util.Helpers._
import scala.collection.JavaConversions._
import java.text.NumberFormat
import java.util.Locale
import net.liftweb.util.PassThru
import org.xsbt.versions._
import SBTVersions._

object VersionRow {
  def maxNumFmt(num: Int) =
    "%0"+ num.toString.length +"d"

  val useCaseCount =
    versions.count(!_.legacy)

  val totalCount =
    versions.size

  val ordFormat =
    maxNumFmt(totalCount)

  val sizeFormat =
    maxNumFmt(versions.map(_.filesize).max)

  val bytesFormat =
    NumberFormat.getIntegerInstance(Locale.GERMANY)
}

class VersionRow(val v: SBTVersion) {
  val paddedOrdinal =
    VersionRow.ordFormat.format(v.ordinal)

  val paddedSize =
    VersionRow.sizeFormat.format(v.filesize)

  val formatFilesize =
    VersionRow.bytesFormat.format(v.filesize)

  val kbSize =
    math.round(v.filesize / 1024.).toInt

  val dateTime =
    new DateTime(v.modTime)

  val isoDate =
    dateTime.toString("YYYY-MM-dd")

  val isoTimestamp =
    dateTime.toString("YYYY-MM-dd HH:mm:ss")

  val localJar =
    URIs.HttpStub + v.filename

  val proxyJar =
    URIs.HttpProxy + v.filename

  val ftpJar =
    URIs.FtpStub + v.filename

  val gopherJar =
    URIs.GopherStub + v.filename

  val localMD5 =
    localJar + ".md5"

  val localSHA1 =
    localJar + ".sha1"

  val localURL =
    localJar + ".url"

  val localGruj =
    URIs.HttpStub + "gruj_vs_" + v.filename

  val localOpt =
    URIs.HttpStub + v.filename.replace(".jar", "-opt.jar")

  val formatOptSize =
    VersionRow.bytesFormat.format(v.optSize)

    val localExe =
    URIs.HttpStub + v.filename.replace(".jar", ".exe")
}

object ShowAll extends SessionVar[Boolean](false)

object VersionsTable {
  def formatDate(millis: Int) =
    new DateTime(millis).toString("YYYY-MM-dd")

  def describe(content: Any*) =
    (n: NodeSeq) => Text(n.text.format(content: _*))

  def showGrujed(vR: VersionRow) =
    (n: NodeSeq) =>
      if (vR.v.grujed) {
        n.bind("ver",
          AttrBindParam("local_gruj", vR.localGruj, "href"),
          FuncAttrBindParam("gruj_title", describe(vR.v.filename), "title")
        )
      }
      else {
        NodeSeq.Empty
      }

  def showNoGrujed(vR: VersionRow) =
    (n: NodeSeq) =>
      if (vR.v.grujed) {
        NodeSeq.Empty
      }
      else {
        n.bind("ver",
          FuncAttrBindParam("gruj_na", describe(vR.v.filename), "title")
        )
      }

  def showOpted(vR: VersionRow) =
    (n: NodeSeq) =>
      if (vR.v.opted) {
        n.bind("ver",
          AttrBindParam("local_opt", vR.localOpt, "href"),
          FuncAttrBindParam("opt_title", describe(vR.v.filename, vR.formatOptSize), "title")
        )
      }
      else {
        NodeSeq.Empty
      }

  def showNoOpted(vR: VersionRow) =
    (n: NodeSeq) =>
      if (vR.v.opted) {
        NodeSeq.Empty
      }
      else {
        n.bind("ver",
          FuncAttrBindParam("opt_na", describe(vR.v.filename), "title")
        )
      }

  def showExeed(vR: VersionRow) =
    (n: NodeSeq) =>
      if (vR.v.exeed) {
        n.bind("ver",
          AttrBindParam("local_exe", vR.localExe, "href"),
          FuncAttrBindParam("exe_title", describe(vR.v.filename), "title")
        )
      }
      else {
        NodeSeq.Empty
      }

  def showNoExeed(vR: VersionRow) =
    (n: NodeSeq) =>
      if (vR.v.exeed) {
        NodeSeq.Empty
      }
      else {
        n.bind("ver",
          FuncAttrBindParam("exe_na", describe(vR.v.filename), "title")
        )
      }

  def attrIf(legacy: Boolean) =
    (n: NodeSeq) =>
      if (legacy) {
        Some(n)
      }
      else {
        None
      }

  def renderRow(v: SBTVersion) = {
    val vR = new VersionRow(v)

    (_: NodeSeq).bind("ver",
      FuncAttrOptionBindParam("legacy_class", attrIf(v.legacy), "class"),
      FuncAttrOptionBindParam("legacy_hidden", attrIf(v.legacy && !ShowAll.is), "hidden"),

      "filename_sort" -> vR.paddedOrdinal,
      "filename" -> v.filename,

      "publish_date" -> vR.isoDate,
      FuncAttrBindParam("publish_timestamp", describe(vR.isoTimestamp), "title"),

      "filesize_sort" -> vR.paddedSize,
      FuncAttrBindParam("filesize", describe(vR.formatFilesize), "title"),
      "filesize_kb" -> vR.kbSize,

      AttrBindParam("local_jar", vR.localJar, "href"),
      AttrBindParam("proxy_jar", vR.proxyJar, "href"),
      AttrBindParam("remote_jar", v.url, "href"),

      AttrBindParam("ftp_jar", vR.ftpJar, "href"),
      AttrBindParam("gopher_jar", vR.gopherJar, "href"),

      AttrBindParam("local_md5", vR.localMD5, "href"),
      FuncAttrBindParam("md5", describe(v.md5), "title"),

      AttrBindParam("local_sha1", vR.localSHA1, "href"),
      FuncAttrBindParam("sha1", describe(v.sha1), "title"),

      AttrBindParam("local_url", vR.localURL, "href"),
      "gruj" -> showGrujed(vR),
      "no_gruj" -> showNoGrujed(vR),

      "opt" -> showOpted(vR),
      "no_opt" -> showNoOpted(vR),

      "exe" -> showExeed(vR),
      "no_exe" -> showNoExeed(vR)
    )
  }

  def render(q: NodeSeq) =
    q.bind("ver",
      FuncAttrOptionBindParam("hide_legacy_hidden", attrIf(!ShowAll.is), "hidden"),
      FuncAttrOptionBindParam("show_legacy_hidden", attrIf(ShowAll.is), "hidden"),
      "show_legacy_message" ->
        describe(VersionRow.useCaseCount, VersionRow.totalCount)
    ).bind("ver",
      "row" ->
        ((n: NodeSeq) => versions.reverse.flatMap(renderRow(_)(n))
    ))
}
