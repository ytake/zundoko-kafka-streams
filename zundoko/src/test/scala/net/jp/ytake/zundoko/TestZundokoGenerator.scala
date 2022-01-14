package net.jp.ytake.zundoko

import org.junit.Test
import org.junit.Assert._

class TestZundokoGenerator {
  
  @Test
  def should5CharactersAreOutputAtRandom(): Unit = {
    val d = new MessageGenerator()
    assertEquals(5, d.make().length)
    val m = new MessageGenerator(Seq("a", "b", "c"))
    assertEquals(5, m.make().length)
  }
}
