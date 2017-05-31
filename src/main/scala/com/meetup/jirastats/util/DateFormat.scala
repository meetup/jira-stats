package com.meetup.jirastats.util

import java.text.SimpleDateFormat
import java.util.Date

import scala.util.{Success, Try}

object DateFormat {
  private val MysqlFormat = "yyyy-MM-dd HH:mm:ss"
  private val BigQueryFormat = "yyyy-MM-dd HH:mm:ss.SSS"
  private val BigQueryDateFormat = "yyyy-MM-dd HH:mm:ss.SSS"

  def mysql(date: Date): Option[String] = {
    val formatter = new SimpleDateFormat(MysqlFormat)
    Try(formatter.format(date)).toOption
  }

  def bigQuery(date: Date): Option[String] = {
    val formatter = new SimpleDateFormat(BigQueryFormat)
    Try(formatter.format(date)).toOption
  }

  def bigQueryDate(date: Date): Option[String] = {
    val formatter = new SimpleDateFormat(BigQueryDateFormat)
    Try(formatter.format(date)).toOption
  }
}
