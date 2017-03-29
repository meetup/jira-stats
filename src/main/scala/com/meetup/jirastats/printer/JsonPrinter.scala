package com.meetup.jirastats.printer

import com.meetup.jirastats.model.JiraIssues
import com.meetup.jirastats.util.DateFormat
import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods._

object JsonPrinter extends Printer {
  def print(releasedIssues: JiraIssues, io: IO): Unit = {
    for {
      issue <- releasedIssues.issues
    } {
      val json =
        ("key" -> issue.key) ~
          ("type" -> issue.issueType) ~
          ("priority" -> issue.priority) ~
          ("version" -> issue.version.orNull) ~
          ("epic" -> issue.epic.orNull) ~
          ("created" -> DateFormat.bigQuery(issue.created))

      io.outln(compact(render(json)))
    }
  }

  def printTransitions(issues: JiraIssues, io: IO): Unit = {
    for {
      issue <- issues.issues
      transition <- issue.transitions
    } {
      val json =
        ("key" -> issue.key) ~
          ("from" -> transition.from) ~
          ("to" -> transition.to) ~
          ("time" -> DateFormat.bigQuery(transition.date))

      io.outln(compact(render(json)))
    }
  }
}
