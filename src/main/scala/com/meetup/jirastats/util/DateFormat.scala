package com.meetup.jirastats.util

import java.text.SimpleDateFormat
import java.util.Date

import scala.util.{Success, Try}

object DateFormat {
  private val MysqlFormat = "yyyy-MM-dd HH:mm:ss"

  def mysql(date: Date): Option[String] = {
    val formatter = new SimpleDateFormat(MysqlFormat)
    Try(formatter.format(date)).toOption
  }
}
