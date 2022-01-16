package net.jp.ytake.kiyoshi

import com.fasterxml.jackson.databind.{DeserializationFeature, JsonNode, ObjectMapper}
import com.fasterxml.jackson.module.scala.{DefaultScalaModule, ScalaObjectMapper}
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.{Deserializer, Serde, Serdes, Serializer}
import org.apache.kafka.connect.json.{JsonDeserializer, JsonSerializer}
import org.apache.kafka.streams.{KafkaStreams,StreamsConfig}
import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.scala.kstream.KStream
import org.apache.kafka.streams.scala.ImplicitConversions._
import org.apache.kafka.streams.scala.serialization.Serdes.stringSerde
import java.util.Properties
import java.time.Duration
import scala.reflect.{ClassTag, classTag}

object StreamProcessor extends App {

  if (args.length < 1) {
    throw new IllegalArgumentException(
      "This program takes one argument: the path to an environment configuration file.")
  }
  val prop = new Properties
  prop.load(new java.io.FileInputStream(args(0)))

  val bootstrapServers: String = prop.getProperty("bootstrap_servers")
  val topic: String = prop.getProperty("topic")
  val to: String = prop.getProperty("to")
  val appId: String = prop.getProperty("app_id")
  val expect: Seq[String] = Seq("ずん", "ずん", "ずん", "ずん", "どこ")

  prop.clear()
  prop.put(StreamsConfig.APPLICATION_ID_CONFIG, appId)
  prop.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers)
  prop.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String.getClass)
  prop.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String.getClass)
  prop.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
  prop.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, "0")

  val deserializer: Deserializer[JsonNode] = new JsonDeserializer()
  val serializer: Serializer[JsonNode] = new JsonSerializer
  implicit val json: Serde[JsonNode] = Serdes.serdeFrom(serializer, deserializer)

  val builder = new StreamsBuilder()
  val messages: KStream[String, JsonNode] = builder.stream[String, JsonNode](topic)
  messages.mapValues(_.toPrettyString)
    .filter((_, v) => fromJson[Message](v).message.equals(expect))
    .mapValues((_, v) => toJson(Cheers(fromJson[Message](v).message, "きよし")))
    .to(to)

  val stream = new KafkaStreams(builder.build(), prop)
  stream.cleanUp()
  stream.start()
  sys.ShutdownHookThread {
    stream.close(Duration.ofSeconds(10))
  }

  def fromJson[T: ClassTag](json: String): T = {
    jsonMapper().readValue(json, classTag[T].runtimeClass).asInstanceOf[T]
  }

  def toJson(value: Cheers): String = {
    jsonMapper().writeValueAsString(value)
  }

  private def jsonMapper(): ObjectMapper = {
    val mapper = new ObjectMapper() with ScalaObjectMapper
    mapper.registerModule(DefaultScalaModule)
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
  }

  case class Message(message: Seq[String])

  case class Cheers(message: Seq[String], call: String)
}
