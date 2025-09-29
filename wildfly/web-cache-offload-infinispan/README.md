# Intersmash Applications - WildFly Web cache offload + Infinispan

A WildFly/JBoss EAP 8.x application which is provisioned by the `wildfly-maven-plugin` and that is configured to 
offload a web cache to a remote Infinispan service.

The application exposes endpoint to store, delete and get a new value to/from the request HttpSession instance.

The WildFly/EAP Maven plugin is configured to build the application and trimmed server, based on required feature packs, 
and to execute a [CLI script](scripts/script.cli) that applies the required server configuration.
