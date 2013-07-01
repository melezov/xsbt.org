package org.xsbt.versions

import java.io.File
import java.util.Date

object SBTVersions {
  val versions =
    SBTVersion.values.toSeq

  val exts =
    List("", ".sha1", ".md5", ".url")

  val reps =
    Map(
      ((v: SBTVersion) => v.opted) -> "-opt.jar"
    )

  def getFile(path: String) =
    new File(classOf[SBTVersion].getResource("/").getFile.
      replaceAll("^(.*/)code/.*?$", "$1"+ path))

  def fixTime(path: String, v: SBTVersion) {
    val file = getFile(path)
      .ensuring(_.isFile, "File %s is missing!" format path)

    val mT = v.modTime
    if (file.lastModified != mT) {
      file.setLastModified(mT)
        .ensuring(k => k, "Could not set last modified time for %s!" format path)
      println(file + " -> " + new Date(mT))
    }
  }

  def main(args: Array[String]) = {
    for (v <- versions; e <- exts) {
      fixTime("files/repo/" + v.filename + e, v)
    }

    for (v <- versions; r <- reps if r._1(v)) {
      fixTime("files/repo/" + v.filename.replace(".jar", r._2), v)
    }
  }
}
