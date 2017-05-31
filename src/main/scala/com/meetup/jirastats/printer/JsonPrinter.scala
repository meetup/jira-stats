package com.meetup.jirastats.printer

import com.meetup.jirastats.model.{Epic, JiraIssues, Version}
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

  def printEpics(epics: List[Epic], io: IO): Unit = {
    epics.foreach { epic =>
      val json =
        ("key" -> epic.key) ~
          ("name" -> epic.name) ~
          ("prefix" -> epic.prefix)

      io.outln(compact(render(json)))
    }
  }

  def printVersions(versions: List[Version], io: IO): Unit = {
    versions.foreach { version =>
      val json =
        ("project" -> version.project) ~
          ("name" -> version.name) ~
          ("release_date" -> DateFormat.bigQueryDate(version.releaseDate))

      io.outln(compact(render(json)))
    }
  }
}
