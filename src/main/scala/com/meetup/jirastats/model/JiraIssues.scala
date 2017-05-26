package com.meetup.jirastats.model

import java.util.Date

import com.meetup.jira.client.Issues
import com.meetup.jirastats.util.JiraTimeParser

object JiraIssues {
  def fromIssues(issuesResult: Issues): JiraIssues = {

    val issues: List[JiraIssue] = issuesResult.issues.flatMap { issue =>
      (for {
        issueType <- issue.issueType
        createdJira <- issue.created
        created <- JiraTimeParser.parseDate(createdJira)
        priority <- issue.priority
      } yield {
        val items = for {
          changeLog <- issue.changeLog.toList
          history <- changeLog.histories
          item <- history.items if item.field == "status"
        } yield (history.created, item)

        val transitions = items.flatMap {
          case (historyCreated, item) =>
            (for {
              from <- item.fromStr
              to <- item.toStr
              date <- JiraTimeParser.parseDate(historyCreated)
            } yield {
              Transition(from, to, date)
            }).toList
        }

        JiraIssue(
          issue.key,
          issue.fixVersions.headOption,
          issueType.name,
          priority.name,
          issue.epic,
          transitions,
          created
        )
      }).orElse {
        println(s"Failed to convert issue: ${issue.key} - ${issue.summary}")
        None
      }.toList
    }

    new JiraIssues(issues)
  }
}

case class JiraIssues(issues: List[JiraIssue])

case class JiraIssue(
  key: String,
  version: Option[String],
  issueType: String,
  priority: String,
  epic: Option[String],
  transitions: List[Transition],
  created: Date
)

