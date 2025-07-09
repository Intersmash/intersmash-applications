# Intersmash Applications - WildFly Elytron OIDC client + Keycloak

A WildFly/JBoss EAP 8.x application which is provisioned by the `wildfly-maven-plugin` and that uses the 
elytron-oidc-client layer to secure resources by authenticating users through OIDC single-sign-on, provided by a 
Keycloak service.

The WildFly/EAP Maven plugin is configured to build the application and trimmed server, based on required feature packs.
