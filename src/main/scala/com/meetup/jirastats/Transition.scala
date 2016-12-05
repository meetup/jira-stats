package com.meetup.jirastats

import java.util.Date

case class Transition(
  from: String,
  to: String,
  date: Date
)
