package com.meetup.jirastats.util

import java.util.TimeZone

import org.scalatest.{FunSpec, Matchers}

class DateFormatTest extends FunSpec with Matchers {

  describe("DateFormat") {
    it("renders bigquery format") {
      TimeZone.setDefault(TimeZone.getTimeZone("EST"))
      val inputString = "2016-11-18T15:41:19.000-0500"
      val input = JiraTimeParser.parseDate(inputString)

      assert(input.nonEmpty)
      input.foreach { input =>
        val expected = "2016-11-18 15:41:19.000"
        val actual = DateFormat.bigQuery(input)
        actual shouldBe Some(expected)
      }
    }
  }

}
