package com.meetup.jirastats.printer

import com.meetup.jirastats.{ReleasedIssues, Stories}

object StdOutPrinter extends Printer {
  override def print(releasedIssues: ReleasedIssues): Unit = {
    releasedIssues.issues.foreach { issue =>
      println(s"Issue: ${issue.key} (${issue.issueType}) - (${issue.version})")

      println(s"  Opened on ${issue.created}")
      issue.transitions.foreach { transition =>
        println(s"  ${transition.from} -> ${transition.to} on ${transition.date}")
      }
      println
    }
  }

  def print(stories: Stories): Unit = {
    stories.issues.foreach { issue =>
      println(s"Story: ${issue.key}")
      println(s"  Opened on ${issue.created}")

      issue.transitions.foreach { transition =>
        println(s"  ${transition.from} -> ${transition.to} on ${transition.date}")
      }
      println
    }
  }
}
