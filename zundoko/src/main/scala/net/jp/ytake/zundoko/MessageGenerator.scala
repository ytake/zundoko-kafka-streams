package net.jp.ytake.zundoko

import scala.util.Random

class MessageGenerator(val x: Seq[String] = Seq("ずん", "どこ")) {

  def make(): Seq[String] = for (_ <- 0 to 4) yield {
    x.apply(Random.nextInt(x.size))
  }
}
