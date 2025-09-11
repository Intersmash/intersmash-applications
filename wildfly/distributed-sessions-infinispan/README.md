# Intersmash Applications - WildFly Distributed Cache + Infinispan Server

A WildFly/JBoss EAP 8.x application which is provisioned by the `wildfly-maven-plugin` and that uses the
web-clustering layer to store session data in a distributed cache which is backed by an Infinispan Server service.

The WildFly/EAP Maven plugin is configured to build the application and trimmed server, based on required feature packs.
