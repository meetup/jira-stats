package com.meetup.jirastats.model

import java.util.Date

case class Transition(
  from: String,
  to: String,
  date: Date
)
