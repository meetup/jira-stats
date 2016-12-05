package com.meetup.jirastats

import com.meetup.jirastats.printer.MysqlPrinter

import scala.annotation.tailrec
import scala.concurrent.Await
import scala.util.Try
import scala.concurrent.duration._

object PrintStoriesApp {

  def main(args: Array[String]): Unit = {
    apply()
  }

  def apply(): Unit = {
    JiraHandler().right.map { handler =>
      printStories(handler)
    }
  }

  @tailrec
  private def printStories(handler: JiraHandler, offset: Option[Int] = None): Unit = {
    val nextOffset =
      Try(Await.result(handler.classicStories(offset), 30.seconds))
        .toOption
        .flatMap { search =>
          val jiraStats = Stories.fromIssues(search)
          MysqlPrinter.print(jiraStats)

          Some(search.startAt + search.maxResults)
            .filter(_ < search.total)
        }

    if (nextOffset.isDefined) {
      Thread.sleep(1000)
      printStories(handler, nextOffset)
    }
  }

}
