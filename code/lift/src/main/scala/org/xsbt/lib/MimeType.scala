package org.xsbt.lib
import net.liftweb.http.InMemoryResponse

import _root_.org.apache.commons.io.FilenameUtils

object MimeType {
  val map = Map(
    "exe" -> "application/octet-stream",
    "jar" -> "application/java-archive",
    "md5" -> "text/plain",
    "sha1" -> "text/plain",
    "url" -> "text/plain"
  )

  def fromFileName(filename: String) =
    map(FilenameUtils.getExtension(filename))
}

object BinaryResponse {
  def apply(body: Array[Byte], filename: String, inline: Boolean) = {
    val disposition = if (inline) {
      "inline"
    }
    else {
      "attachment; filename=\""+ filename +"\""
    }

    val headers = List(
      ("Content-Transfer-Encoding", "binary"),
      ("Content-Length", body.length.toString),
      ("Content-Type", MimeType.fromFileName(filename)),
      ("Content-Disposition", disposition)
    )

    InMemoryResponse(body, headers, Nil, 200)
  }
}

object InlineResponse {
  def apply(body: Array[Byte], filename: String) =
    BinaryResponse(body, filename, true)
}

object AttachmentResponse {
  def apply(body: Array[Byte], filename: String) =
    BinaryResponse(body, filename, false)
}
