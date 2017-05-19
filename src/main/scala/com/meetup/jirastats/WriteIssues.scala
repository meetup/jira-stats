package com.meetup.jirastats

import com.meetup.jirastats.model.JiraIssues
import com.meetup.jirastats.printer.{IO, JsonPrinter}

import scala.annotation.tailrec
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.Try

class WriteIssues(handler: JiraHandler, issuesWriter: IO, transitionsWriter: IO) {

  @tailrec
  final def write(offset: Option[Int] = None): Unit = {
    val nextOffset =
      Try(Await.result(handler.issues(offset), 30.seconds))
        .toOption
        .flatMap { search =>
          val start = search.startAt
          val until = start + search.maxResults
          println(s"Writing issues $start to $until of ${search.total}")

          val jiraStats = JiraIssues.fromIssues(search)
          JsonPrinter.print(jiraStats, issuesWriter)
          JsonPrinter.printTransitions(jiraStats, transitionsWriter)

          Some(search.startAt + search.maxResults)
            .filter(_ < search.total)
        }

    if (nextOffset.isDefined) {
      Thread.sleep(1000)
      write(nextOffset)
    }
  }
}