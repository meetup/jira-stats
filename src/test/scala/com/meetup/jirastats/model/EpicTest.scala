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

    it("parses prefixes where it's the prefix only") {
      val input = "EE"
      val actual = Epic.epicPrefix(input)
      actual shouldBe Some("EE")
    }

    it("parses prefixes with spaces and all sorts") {
      val input = "QA Automation - Genesis"
      val actual = Epic.epicPrefix(input)
      actual shouldBe Some("QA Automation")
    }

    it("parses other forms of delimiters (:)") {
      val input = "ENGA Replatform: event page"
      val actual = Epic.epicPrefix(input)
      actual shouldBe Some("ENGA Replatform")
    }

    it("parses other forms of delimiters (|)") {
      val input = "CX | Policy Documentation "
      val actual = Epic.epicPrefix(input)
      actual shouldBe Some("CX")
    }
  }
}
