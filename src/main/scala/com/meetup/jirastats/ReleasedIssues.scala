package com.meetup.jirastats

import java.util.Date

import com.meetup.jira.client.Issues
import com.meetup.jirastats.util.JiraTimeParser

object ReleasedIssues {
  def fromIssues(issuesResult: Issues): ReleasedIssues = {

    val issues: List[ReleasedIssue] = issuesResult.issues.flatMap { issue =>
      (for {
        fixVersion <- issue.fixVersions.headOption
        issueType <- issue.issueType
        createdJira <- issue.created
        created <- JiraTimeParser.parseDate(createdJira)
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

        ReleasedIssue(
          issue.key,
          fixVersion,
          issueType.name,
          transitions,
          created
        )
      }).toList
    }

    new ReleasedIssues(issues)
  }
}

case class ReleasedIssues(issues: List[ReleasedIssue])

case class ReleasedIssue(
  key: String,
  version: String,
  issueType: String,
  transitions: List[Transition],
  created: Date
)

