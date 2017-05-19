package com.meetup.jirastats.model

import com.meetup.jira.client.{Issue, Issues}

case class Epic(key: String, name: String, prefix: String)

object Epic {

  def fromIssues(issues: Issues): List[Epic] = {
    for {
      epic <- issues.issues
      name <- epicName(epic)
      prefix <- epicPrefix(name)
    } yield {
      Epic(
        key = epic.key,
        name = name,
        prefix = prefix
      )
    }
  }

  private def epicName(issue: Issue): Option[String] = {
    for {
      fields <- issue.customs.find(_._1 == "customfield_10004")
      field <- fields._2.headOption
    } yield {
      field.value
    }
  }

  private val PrefixPattern = "(^[a-zA-Z0-9]+)\\s?-\\s?.*".r
  def epicPrefix(epicName: String): Option[String] = {
    (epicName.trim match {
      case PrefixPattern(prefix) =>
        Some(prefix)
      case _ => None
    }).orElse {
      println(s"Failed to parse prefix from epic name: $epicName")
      None
    }
  }

}
