package com.meetup.jirastats.util

import java.text.SimpleDateFormat
import java.util.Date

import scala.util.Try

/**
 * Parses dates in the format of 2016-11-18T15:41:19.000-0500
 */
object JiraTimeParser {
  val TimeFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZZZZ"
  val Format = new SimpleDateFormat(TimeFormat)

  def parseDate(dateTime: String): Option[Date] = {
    Try(Format.parse(dateTime)).toOption.orElse {
      println(s"Failed to parse: $dateTime")
      None
    }
  }
}
