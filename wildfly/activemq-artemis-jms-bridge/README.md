# Intersmash Applications - WildFly + ActiveMQ Artemis JMS Bridge

A WildFly/JBoss EAP 8.x application which is provisioned by the `wildfly-maven-plugin` and that uses a JMS Bridge to forward messages from local queues to a remote ActiveMQ Artemis message broker.

The WildFly/EAP Maven plugin is configured to build the application and trimmed server, based on required feature packs.