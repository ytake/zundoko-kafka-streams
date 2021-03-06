ThisBuild / version := "1.0"
ThisBuild / scalaVersion := "2.13.8"
ThisBuild / organization := "net.jp.ytake"

lazy val commonSettings = Seq(
  resolvers ++= Seq(
    "mvn" at "https://mvnrepository.com/artifact"
  ),
  libraryDependencies ++= Seq(
    "com.fasterxml.jackson.core" % "jackson-databind" % "2.10.5.1",
    "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.10.5",
    "org.apache.kafka" % "kafka-clients" % "2.8.1",
    "org.apache.kafka" %% "kafka-streams-scala" % "2.8.1",
    "org.slf4j" % "slf4j-api" % "1.7.30",
    "io.circe" %% "circe-core" % "0.14.1",
    "io.circe" %% "circe-generic" % "0.14.1",
    "io.circe" %% "circe-parser" % "0.14.1",
    "junit" % "junit" % "4.13" % Test,
    "org.apache.kafka" % "kafka-streams-test-utils" % "2.8.1" % Test
  ),
  assembly / assemblyMergeStrategy := {
    case PathList(ps@_*) if ps.last endsWith ".class" => MergeStrategy.first
    case PathList("org", "apache", xs@_*) => MergeStrategy.first
    case x =>
      val oldStrategy = (assembly / assemblyMergeStrategy).value
      oldStrategy(x)
  },
  assemblyJarName := {
    s"${name.value}-${version.value}.jar"
  }
)

lazy val zundoko = (project in file("zundoko")).
  settings(commonSettings: _*).
  settings(
    name := "zundoko",
    assembly / mainClass := Some(s"${organization.value}.zundoko.MessageProducer")
  )

lazy val kiyoshi = (project in file("kiyoshi")).
  settings(commonSettings: _*).
  settings(
    name := "kiyoshi",
    assembly / mainClass := Some(s"${organization.value}.kiyoshi.StreamProcessor")
  )
