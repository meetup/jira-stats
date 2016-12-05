package com.meetup.jirastats.util

import java.time.{LocalDateTime, ZoneOffset}
import java.util.Date

import org.scalatest.{FunSpec, Matchers}

class JiraTimeParserTest extends FunSpec with Matchers {

  describe("JiraTimeParser") {
    it("gracefully returns None on failure") {
      val actual = JiraTimeParser.parseDate("asdf")
      actual shouldBe None
    }

    it("parses jira date formats") {
      val input = "2016-11-18T15:41:19.000-0500"
      val actual = JiraTimeParser.parseDate(input)
      val expected = Date.from(LocalDateTime.of(2016, 11, 18, 15, 41, 19).toInstant(ZoneOffset.ofHours(-5)))
      actual shouldBe Some(expected)
    }
  }
}
