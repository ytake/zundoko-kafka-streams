package net.jp.ytake.kiyoshi

import net.jp.ytake.kiyoshi.StreamProcessor.{Cheers, Message, toJson}
import org.junit.Test
import org.junit.Assert._

class TestStreamProcessor {
  @Test
  def shouldReturnSameMessage(): Unit = {
    val c = StreamProcessor.fromJson[Message](raw"""{"message":["どこ","どこ","ずん","ずん","どこ"]}""")
    assertEquals(Message(Seq("どこ", "どこ", "ずん", "ずん", "どこ")), c)
  }

  @Test
  def shouldTransformMessage(): Unit = {
    val c = StreamProcessor.fromJson[Message](raw"""{"message":["どこ","どこ","ずん","ずん","どこ"]}""")
    assertEquals(raw"""{"message":["どこ","どこ","ずん","ずん","どこ"],"call":"きよし"}""", toJson(Cheers(c.message, "きよし")))
  }
}
