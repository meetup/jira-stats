package com.meetup.jirastats

import com.meetup.jira.client.{Issues, JiraClient, Search, Version => JiraVersion}

import dispatch.Http
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

trait JiraHandler {
  def issues(startAt: Option[Int] = None): Future[Issues]
  def epics(startAt: Option[Int] = None): Future[Issues]
  def versions(): Future[Map[String, List[JiraVersion]]]
  def close(): Unit
}

object JiraHandler {
  def apply(env: Map[String, String] = sys.env): Either[String, JiraHandler] = {
    val handler = for {
      uri <- env.get("JIRA_URI")
      user <- env.get("JIRA_USER")
      pass <- env.get("JIRA_PASSWORD")
    } yield new JiraHandlerImpl(JiraClient(uri, user, pass))
    handler.toRight(
      "missing required env variables JIRA_URI, JIRA_USER, JIRA_PASSWORD"
    )
  }
}

class JiraHandlerImpl(client: JiraClient) extends JiraHandler {
  def issues(startAt: Option[Int] = None): Future[Issues] = {
    client.search(
      Search(
        jql = "status = Closed AND resolution = Done AND createdDate > startOfYear()",
        startAt = startAt,
        expand = Some(List("changelog"))
      ))
  }

  def epics(startAt: Option[Int] = None): Future[Issues] = {
    client.search(
      Search(
        jql = "type = Epic",
        startAt = startAt
      ))
  }

  def versions(): Future[Map[String, List[JiraVersion]]] = {
    Future.sequence(
      for {
        project <- List("MUP", "AND", "IOS")
      } yield {
        client.versions(project)
          .map { versions =>
            (project -> versions)
          }
      }
    ).map(_.toMap)
  }

  def close(): Unit = {
    // quirk in dispatch http requires you to shutdown the default instance
    // as well as the instance you're using. otherwise the jvm may not shut down
    // cleanly
    Http.shutdown()
    client.close()
  }
}
