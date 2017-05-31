package com.meetup.jirastats.model

import java.util.Date
import com.meetup.jira.client.{Version => JiraVersion}

case class Version(
  project: String,
  name: String,
  releaseDate: Date
)

object Version {
  def fromJiraVersions(
    versionsByProject: Map[String, List[JiraVersion]]
  ): List[Version] = {
    (for {
      (project, versions) <- versionsByProject
      version <- versions
      releaseDate <- version.releaseDate
    } yield {
      Version(project, version.name, releaseDate)
    }).toList
  }
}
