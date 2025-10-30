# Intersmash Applications - WildFly SAML Adapter + Keycloak

A WildFly/JBoss EAP 8.x application which is provisioned by the `wildfly-maven-plugin` and that uses the
keycloak-saml-adapter-galleon-pack feature pack to secure resources by authenticating users through SAMP single-sign-on, provided by a 
Keycloak service.

The WildFly/EAP Maven plugin is configured to build the application and trimmed server, based on required feature packs.
