package com.meetup.jirastats

import com.meetup.jirastats.printer.FileWriter

object App {

  def main(args: Array[String]): Unit = {
    apply()
  }

  def apply(): Unit = {
    JiraHandler().fold[Unit]({ error =>
      println(error)
    }, { handler =>
      val issuesFile = "issues.json.log"
      val transitionsFile = "transitions.json.log"
      val epicsFile = "epics.json.log"

      val issuesWriter = new FileWriter(issuesFile)
      val transitionsWriter = new FileWriter(transitionsFile)
      val epicsWriter = new FileWriter(epicsFile)

      println(s"Will write issues to $issuesFile")
      println(s"Will write transitions to $transitionsFile")

      new WriteIssues(handler, issuesWriter, transitionsWriter).write()

      println(s"Will write epics to $epicsFile")
      new WriteEpics(handler, epicsWriter).write()

      issuesWriter.close()
      transitionsWriter.close()
      epicsWriter.close()
      handler.close()

    })
  }

}

