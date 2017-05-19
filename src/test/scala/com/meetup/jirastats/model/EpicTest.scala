package com.meetup.jirastats.model

import org.scalatest.{FunSpec, Matchers}

class EpicTest extends FunSpec with Matchers {

  describe("Epic") {

    it("parses prefixes from epicName") {
      val input = "WP - Port of mup-web"
      val actual = Epic.epicPrefix(input)
      actual shouldBe Some("WP")
    }

    it("parses prefixes from epicNames without spacing") {
      val input = "WP-Port of mup-web"
      val actual = Epic.epicPrefix(input)
      actual shouldBe Some("WP")
    }

    it("parses prefixes with lower case names") {
      val input = "Android - Event Create and Edit Flow"
      val actual = Epic.epicPrefix(input)
      actual shouldBe Some("Android")
    }
  }
}
