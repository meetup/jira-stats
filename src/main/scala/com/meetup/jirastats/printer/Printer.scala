package com.meetup.jirastats.printer

import com.meetup.jirastats.model.JiraIssues

trait Printer {
  def print(jiraStats: JiraIssues, io: IO): Unit
}
