package com.meetup.jirastats.util

import java.time.{LocalDateTime, ZoneOffset}
import java.util.Date

import org.scalatest.{FunSpec, Matchers}

class DateFormatTest extends FunSpec with Matchers {

  describe("DateFormat") {
    it("renders mysql format") {
      val input = Date.from(LocalDateTime.of(2016, 11, 18, 15, 41, 19).toInstant(ZoneOffset.ofHours(-5)))
      val expected = "2016-11-18 15:41:19"
      val actual = DateFormat.mysql(input)
      actual shouldBe Some(expected)
    }

    it("renders bigquery format") {
      val input = Date.from(LocalDateTime.of(2016, 11, 18, 15, 41, 19).toInstant(ZoneOffset.ofHours(-5)))
      val expected = "2016-11-18 15:41:19.000 -0500"
      val actual = DateFormat.bigQuery(input)
      actual shouldBe Some(expected)
    }
  }

}
