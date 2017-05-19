package com.meetup.jirastats

import com.meetup.jirastats.model.Epic
import com.meetup.jirastats.printer.{IO, JsonPrinter}

import scala.annotation.tailrec
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.Try

class WriteEpics(handler: JiraHandler, epicWriter: IO) {

  @tailrec
  final def write(offset: Option[Int] = None): Unit = {
    val nextOffset =
      Try(Await.result(handler.epics(offset), 30.seconds))
        .toOption
        .flatMap { search =>
          val start = search.startAt
          val until = start + search.maxResults
          println(s"Recording $start to $until of ${search.total}")

          val epics = Epic.fromIssues(search)
          JsonPrinter.printEpics(epics, epicWriter)

          Some(search.startAt + search.maxResults)
            .filter(_ < search.total)
        }

    if (nextOffset.isDefined) {
      Thread.sleep(1000)
      write(nextOffset)
    }
  }
}
