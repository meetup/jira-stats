package com.meetup.jirastats

import java.util.Date

import com.meetup.jira.client.Issues
import com.meetup.jirastats.util.JiraTimeParser

object Stories {
  def fromIssues(issuesResult: Issues): Stories = {

    val stories: List[Story] = issuesResult.issues.flatMap { issue =>
      (for {
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

        Story(
          issue.key,
          transitions,
          created
        )
      }).toList
    }

    new Stories(stories)
  }
}

case class Stories(issues: List[Story])

case class Story(
  key: String,
  transitions: List[Transition],
  created: Date
)