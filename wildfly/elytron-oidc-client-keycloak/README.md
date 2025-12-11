# Intersmash Applications - WildFly Elytron OIDC client + Keycloak

A WildFly/JBoss EAP 8.x application which is provisioned by the `wildfly-maven-plugin` and that uses the 
elytron-oidc-client layer to secure resources by authenticating users through OIDC single-sign-on, provided by a 
Keycloak service.

The WildFly/EAP Maven plugin is configured to build the application and trimmed server, based on required feature packs.

The `no-oidc-json` profile can be used to exclude the [oidc.json](src/main/webapp/WEB-INF/oidc.json) file from packaging: this is useful when testing with dynamic OIDC client registration feature (see function `oidc_configure_remote_client` in [oidc.sh](https://github.com/wildfly/wildfly-cekit-modules/blob/main/jboss/container/wildfly/launch/oidc/added/oidc.sh) for the details)
