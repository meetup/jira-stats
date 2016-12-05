package com.meetup.jirastats

import com.meetup.jirastats.printer.{MysqlPrinter, StdOutPrinter}

import scala.annotation.tailrec
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.Try

object PrintReleasedIssuesApp {

  def main(args: Array[String]): Unit = {
    apply()
  }

  def apply(): Unit = {
    JiraHandler().right.map { handler =>
      printIssues(handler)
    }
  }

  @tailrec
  private def printIssues(handler: JiraHandler, offset: Option[Int] = None): Unit = {
    val nextOffset =
      Try(Await.result(handler.releasedClassicIssues(offset), 30.seconds))
        .toOption
        .flatMap { search =>
          val jiraStats = ReleasedIssues.fromIssues(search)
          MysqlPrinter.print(jiraStats)

          Some(search.startAt + search.maxResults)
            .filter(_ < search.total)
        }

    if (nextOffset.isDefined) {
      Thread.sleep(1000)
      printIssues(handler, nextOffset)
    }
  }
}
