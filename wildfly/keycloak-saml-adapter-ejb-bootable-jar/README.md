# Intersmash Applications - WildFly SAML Adapter + Bootable Jar + Keycloak + EJB

A **WildFly/JBoss EAP 8.x application** which is provisioned by the `wildfly-maven-plugin` and that uses the
`keycloak-saml-adapter-galleon-pack` **feature pack** to secure resources by authenticating users through SAML single-sign-on, provided by a 
Keycloak service.

EJB resources are secured and authentication + authorization information is propagated to the EJB subsystem.

The application is packaged as a Bootable Jar;

The WildFly/EAP Maven plugin is configured to build the application and trimmed server, based on required feature packs.

The SAML Client is expected to be pre-configured on Keycloak;

The `keycloak-client-saml-ejb` layer is used instead of the recommended `keycloak-saml`: this is done to check that that layer, which is primarily intended for bare-metal usage, also works in Kubenrnetes/OpenShift.
