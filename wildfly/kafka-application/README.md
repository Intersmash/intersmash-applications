# Intersmash Applications - WildFly + Apache Kafka

A WildFly/JBoss EAP 8.x application which is provisioned by the `wildfly-maven-plugin` and that uses a remote Apache Kafka service for Messaging via MicroProfile Reactive Messaging.

The WildFly/EAP Maven plugin is configured to build the application and trimmed server, based on required feature packs.

## Profiles

- `bootable-jar` — Produces a bootable JAR (a self-contained executable JAR that includes both the application and the trimmed WildFly server).
