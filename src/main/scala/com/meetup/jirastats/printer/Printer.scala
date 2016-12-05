package com.meetup.jirastats.printer

import com.meetup.jirastats.{ReleasedIssues, Stories}

trait Printer {
  def print(jiraStats: ReleasedIssues)
  def print(stories: Stories): Unit
}
