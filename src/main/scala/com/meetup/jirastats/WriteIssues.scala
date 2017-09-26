package com.meetup.jirastats

import com.meetup.jirastats.model.JiraIssues
import com.meetup.jirastats.printer.{IO, JsonPrinter}

import scala.annotation.tailrec
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.{Failure, Success, Try}

class WriteIssues(handler: JiraHandler, issuesWriter: IO, transitionsWriter: IO) {

  sealed trait Response
  case object Failed extends Response
  case class Ok(nextOffset: Option[Int]) extends Response

  @tailrec
  final def write(
    offset: Option[Int] = None,
    maxResults: Int = 50,
    retry: Int = 0
  ): Unit = {
    val next = nextOffset(offset, maxResults)

    next match {
      case Ok(nextOffset) if nextOffset.isDefined =>
        Thread.sleep(1000)
        write(nextOffset, maxResults)

      case Failed if retry < 2 =>
        write(offset, maxResults, retry + 1)

      case Failed =>
        println(s"Skipping over failed offset: $offset")
        write(offset.map(_ + maxResults), maxResults)

      case _ =>
      // We're done
    }
  }

  private def nextOffset(offset: Option[Int], maxResults: Int): Response = {
    Try(Await.result(handler.issues(offset), 30.seconds)) match {
      case Success(search) =>
        val start = search.startAt
        val until = start + search.maxResults
        println(s"Writing issues $start to $until of ${search.total}")

        val jiraStats = JiraIssues.fromIssues(search)
        JsonPrinter.print(jiraStats, issuesWriter)
        JsonPrinter.printTransitions(jiraStats, transitionsWriter)

        Ok(Some(search.startAt + search.maxResults)
          .filter(_ < search.total))

      case Failure(e) =>
        println(s"Exception occurred on offset $offset")
        e.printStackTrace()
        Failed
    }
  }
}