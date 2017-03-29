package com.meetup.jirastats.printer

trait IO {
  def outln(output: String)
  def close()
}
