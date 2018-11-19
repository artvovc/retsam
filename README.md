retsam

> java -version
java version "1.8.0_191"
Java(TM) SE Runtime Environment (build 1.8.0_191-b12)
Java HotSpot(TM) 64-Bit Server VM (build 25.191-b12, mixed mode)

> scala -version
Scala code runner version 2.12.7 -- Copyright 2002-2018, LAMP/EPFL and Lightbend, Inc.

> sbt sbtVersion
[info] Loading project definition from /home/noname/workspace/retsam/project
[info] Loading settings for project retsam from build.sbt ...
[info] Set current project to master (in build file:/home/noname/workspace/retsam/)
[info] 1.2.1

//KAFKA daemonized
> /usr/lib/kafka/kafka_2.11-2.0.0/bin/zookeeper-server-start.sh /usr/lib/kafka/kafka_2.11-2.0.0/config/zookeeper.properties
> /usr/lib/kafka/kafka_2.11-2.0.0/bin/kafka-server-start.sh /usr/lib/kafka/kafka_2.11-2.0.0/config/server.properties
//TO KILL use kill process by PID
---
//KAFKA interface daemonized
http://localhost:9000

//RMQ daemonized
> sudo service rabbitmq-server stop
> sudo service rabbitmq-server start
//RMQ interface daemonized
http://localhost:15672

//ManagementFactory.getRuntimeMXBean().getName()
//top -p <PID>