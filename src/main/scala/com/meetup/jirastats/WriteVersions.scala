package com.meetup.jirastats

import com.meetup.jirastats.model.Version
import com.meetup.jirastats.printer.{IO, JsonPrinter}

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.Try

class WriteVersions(handler: JiraHandler, versionWriter: IO) {

  final def write(): Unit = {
    Try(Await.result(handler.versions(), 30.seconds))
      .toOption
      .foreach { results =>
        for {
          (project, versions) <- results
        } {
          println(s"${versions.size} for project $project")
        }

        val versions = Version.fromJiraVersions(results)
        JsonPrinter.printVersions(versions, versionWriter)
      }

  }
}
