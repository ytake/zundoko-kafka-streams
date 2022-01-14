package net.jp.ytake.zundoko

import org.junit.Test
import org.junit.Assert._

class TestMessageProducer {

  @Test
  def shouldBeToJsonString(): Unit = {
    val p = MessageProducer.toJson(Map("message" -> Seq("ずん")))
    assertEquals(raw"""{"message":["ずん"]}""", p)
  }
}
