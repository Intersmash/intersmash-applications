# Intersmash Applications - WildFly MicroProfile Reactive Messaging + Kafka

A WildFly/JBoss EAP XP MicroProfile Reactive Messaging application, which is configured to use a remote Kafka/Streams for 
Apache Kafka service.

The WildFly/EAP Maven plugin is configured to build the application and trimmed server, based on required feature packs.

This application sends messages to a Kafka topic, and at the same time listens to another topic
instance to read data from there. Connections are performed both as not secured (plaintext) and secured via SSL with 
SSLContext too.
