# Intersmash Applications - WildFly Timer Expiration Store

A WildFly/JBoss EAP 8.x application which is provisioned by the `wildfly-maven-plugin`, providing a service 
to store timer expirations to a remote persistence layer, abstracted by the 
[persistence.xml](./src/main/resources/META-INF/persistence.xml) definition.

The persistence.xml definition configures a `primary` persistence unit, held by a `java:jboss/datasources/PostgreSQLDS` 
datasource, which must be backed by a PostgreSQL service. 

The application provides a remote EJB service to record timer expirations, and exposes an endpoint to get or delete
persisted timer expirations.

The WildFly/JBoss EAP Maven plugin is configured to build the application and the trimmed server, based on required 
feature packs, and to include user credentials provided by a local application-users.properties file to secure client 
access to the timer expiration storage service.
