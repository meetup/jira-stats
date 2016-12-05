package com.meetup.jirastats

import com.meetup.jira.client.{Issues, JiraClient, Search}

import dispatch.Http
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

trait JiraHandler {
  def releasedClassicIssues(startAt: Option[Int] = None): Future[Issues]
  def classicStories(startAt: Option[Int] = None): Future[Issues]
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
  def releasedClassicIssues(startAt: Option[Int] = None): Future[Issues] = {
    client.search(
      Search(
        jql = "project = MUP AND status = Closed AND resolution = Done and fixVersion is not EMPTY",
        startAt = startAt,
        expand = Some(List("changelog"))
      ))
  }

  def classicStories(startAt: Option[Int] = None): Future[Issues] = {
    client.search(
      Search(
        jql = "project = MUP AND status = Closed AND resolution = Done AND issuetype = Story ORDER BY resolved DESC",
        startAt = startAt,
        expand = Some(List("changelog"))
      )
    )
  }

  def close(): Unit = {
    // quirk in dispatch http requires you to shutdown the default instance
    // as well as the instance you're using. otherwise the jvm may not shut down
    // cleanly
    Http.shutdown()
    client.close()
  }
}