package com.meetup.jirastats

import com.meetup.jirastats.model.JiraIssues
import com.meetup.jirastats.printer.{FileWriter, IO, JsonPrinter}

import scala.annotation.tailrec
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.Try

object PrintIssuesApp {

  def main(args: Array[String]): Unit = {
    apply()
  }

  def apply(): Unit = {
    JiraHandler().fold[Unit]({ error =>
      println(error)
    }, { handler =>
      val issuesWriter = new FileWriter("issues.json.log")
      val transitionsWriter = new FileWriter("transitions.json.log")

      new PrintIssues(handler, issuesWriter, transitionsWriter).printIssues()

      issuesWriter.close()
      transitionsWriter.close()
      handler.close()

    })
  }

}

class PrintIssues(handler: JiraHandler, issuesWriter: IO, transitionsWriter: IO) {

  @tailrec
  final def printIssues(offset: Option[Int] = None): Unit = {
    val nextOffset =
      Try(Await.result(handler.issues(offset), 30.seconds))
        .toOption
        .flatMap { search =>
          val start = search.startAt
          val until = start + search.maxResults
          println(s"Recording $start to $until of ${search.total}")

          val jiraStats = JiraIssues.fromIssues(search)
          JsonPrinter.print(jiraStats, issuesWriter)
          JsonPrinter.printTransitions(jiraStats, transitionsWriter)

          Some(search.startAt + search.maxResults)
            .filter(_ < search.total)
        }

    if (nextOffset.isDefined) {
      Thread.sleep(1000)
      printIssues(nextOffset)
    }
  }
}
