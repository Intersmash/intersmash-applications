# Intersmash Applications - WildFly Distributed Timers with Infinispan

A WildFly/JBoss EAP 8.x application which is provisioned by the `wildfly-maven-plugin`, implementing a service 
that provides methods to start and stop distributed timers' execution.

The timer service is configured to consume a remote persistence service to store timer expirations. 

The WildFly/JBoss EAP Maven plugin is configured to build the application and the trimmed server, based on required 
feature packs, and to include user credentials provided by a local application-users.properties file to secure client 
access to the timer expiration storage service.
