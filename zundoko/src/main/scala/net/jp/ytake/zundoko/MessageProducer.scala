package net.jp.ytake.zundoko

import java.util.Properties
import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper}
import com.fasterxml.jackson.module.scala.{DefaultScalaModule, ScalaObjectMapper}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}

object MessageProducer extends App {

  if (args.length < 1) {
    throw new IllegalArgumentException(
      "This program takes one argument: the path to an environment configuration file.")
  }

  val prop = new Properties
  prop.load(new java.io.FileInputStream(args(0)))

  val bootstrapServers: String = prop.getProperty("bootstrap_servers")
  val topic: String = prop.getProperty("topic")
  prop.clear()
  prop.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers)
  prop.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
  prop.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")

  val producer = new KafkaProducer[String, String](prop)
  val m = new MessageGenerator()

  for (_ <- 0 to 1000) {
    producer.send(
      new ProducerRecord(topic, "key", toJson(Map("message" -> m.make())))
    )
  }
  producer.close()

  def toJson(value: Map[String, Seq[String]]): String = {
    jsonMapper().writeValueAsString(value)
  }

  private def jsonMapper(): ObjectMapper = {
    val mapper = new ObjectMapper() with ScalaObjectMapper
    mapper.registerModule(DefaultScalaModule)
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
  }
}
