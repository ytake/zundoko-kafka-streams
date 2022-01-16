# zundoko-kafka
zundoko!

`ずん` と `どこ` をランダムで5文字組み合わせてKafkaへ送信し、  
`Seq("ずん","ずん","ずん","ずん","どこ")` の場合に `きよし` を付与したJSONを  
他Topicへ転送するKafka Streamsサンプル

to 

```bash
{"message":["ずん","ずん","ずん","ずん","どこ"],"call":"きよし"}
```

## Producer 

```bash
$ sbt zundoko/assembly
```

```bash
$ java -jar /path/to/zundoko-1.0.jar config/zundoko.properties
```

## Kafka Streams

```bash
$ sbt kiyoshi/assembly
```

```bash
$ java -jar /path/to/kiyoshi-1.0.jar config/kiyoshi.properties
```

## Kafka Command(bitnami/kafka)

### Console Consumer

```bash
$ kafka-console-consumer.sh --bootstrap-server=127.0.0.1:29092 --topic=call-kiyoshi --from-beginning
```
