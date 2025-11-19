# Intersmash Applications - WildFly SAML Adapter + Keycloak

A **WildFly/JBoss EAP 8.x application** which is provisioned by the `wildfly-maven-plugin` and that uses the
`keycloak-saml-adapter-galleon-pack` **feature pack** to secure resources by authenticating users through SAML single-sign-on, provided by a 
Keycloak service.

The WildFly/EAP Maven plugin is configured to build the application and trimmed server, based on required feature packs.

The SAML Client is automatically registered into Keycloak by leveraging a capability of the `keycloak-saml` layer called "Automatic SAML Client Registration".
Here is how this feature works:
* first, a user is created in Keycloak: this user must be granted the **create-client** "Client Role"
* this user's credentials are then passed to the **WildFly/JBoss EAP 8.x application** by setting the **SSO_USERNAME** and **SSO_PASSWORD** env variables (see [keycloak.sh](https://github.com/wildfly/wildfly-cekit-modules/blob/main/jboss/container/wildfly/launch/keycloak/2.0/added/keycloak.sh) for the logic and [keycloak/2.0/module.yaml](https://github.com/wildfly/wildfly-cekit-modules/blob/main/jboss/container/wildfly/launch/keycloak/2.0/module.yaml) for the env variables description);
* when the **WildFly/JBoss EAP 8.x application** starts, the `keycloak-saml` layer (more precisely, this feature is nested inside the server startup script) takes care of using the provided credentials to register a new SAML client into Keycloak
