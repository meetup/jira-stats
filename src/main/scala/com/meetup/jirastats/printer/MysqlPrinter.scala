package com.meetup.jirastats.printer

import com.meetup.jirastats.{ReleasedIssues, Stories, Transition}
import com.meetup.jirastats.util.DateFormat

object MysqlPrinter extends Printer {

  private def printTransitions(key: String, transitions: List[Transition]): Unit = {
    println(s"REPLACE INTO transition (`key`, `from`, `to`, `time`) VALUES ")
    val values: List[String] = for {
      transition <- transitions
      transDate <- DateFormat.mysql(transition.date)
    } yield {
      s" ('$key','${transition.from}', '${transition.to}', '$transDate')"
    }
    println(s"${values.mkString(",\n")};")
  }

  override def print(releasedIssues: ReleasedIssues): Unit = {
    for {
      issue <- releasedIssues.issues
      created <- DateFormat.mysql(issue.created)
    } {
      println(s"REPLACE INTO issue (`key`, `type`, `version`, `created`) VALUES ('${issue.key}', '${issue.issueType}', '${issue.version}', '$created');")
      println

      printTransitions(issue.key, issue.transitions)
      println
    }
  }

  override def print(stories: Stories): Unit = {
    for {
      story <- stories.issues
      created <- DateFormat.mysql(story.created)
    } {
      println(s"REPLACE INTO story (`key`, `created`) VALUES ('${story.key}', '$created');")
      println

      printTransitions(story.key, story.transitions)
      println
    }

  }
}
