package org.jboss.intersmash.deployments.wildfly;

import org.jboss.intersmash.deployments.DeploymentProperties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import uk.org.webcompere.systemstubs.jupiter.SystemStub;
import uk.org.webcompere.systemstubs.jupiter.SystemStubsExtension;
import uk.org.webcompere.systemstubs.properties.SystemProperties;

/**
 * Verifies {@link WildflyApplicationConfiguration} functionality, based on documented WildFly application configuration
 * properties.
 */
@ExtendWith(SystemStubsExtension.class)
public class WildflyApplicationConfigurationTest {

	@SystemStub
	private SystemProperties systemProperties;

	/**
	 * Check that all relevant system properties can be read via {@link WildflyApplicationConfiguration}
	 * @param properties a {@link SystemProperties} instance to set properties which are exposed via
	 *                   {@link WildflyApplicationConfiguration}
	 */
	@Test
	void allPropertiesCanBeReadSuccessfully(SystemProperties properties) {
		// Arrange
		properties.set("wildfly-maven-plugin.groupId", "org.wildfly.plugins");
		properties.set("wildfly-maven-plugin.artifactId", "wildfly-maven-plugin");
		properties.set("wildfly-maven-plugin.version", "4.2.2.Final");
		properties.set("wildfly.ee-feature-pack.location", "org.wildfly:wildfly-ee-galleon-pack:30.0.0.Final");
		properties.set("wildfly.feature-pack.location", "org.wildfly:wildfly-galleon-pack:30.0.0.Final");
		properties.set("wildfly.cloud-feature-pack.location", "org.wildfly.cloud:wildfly-cloud-galleon-pack:5.0.1.Final");
		properties.set("wildfly.datasources-feature-pack.location", "org.wildfly:wildfly-datasources-galleon-pack:6.0.0.Final");
		properties.set("wildfly.keycloak-saml-adapter-feature-pack.version", "22.0.3");
		properties.set("wildfly.ee-channel.groupId", "wildfly.ee-channel.groupId.NONE_FOR_WILDFLY");
		properties.set("wildfly.ee-channel.artifactId", "wildfly.ee-channel.artifactId.NONE_FOR_WILDFLY");
		properties.set("wildfly.ee-channel.version", "wildfly.ee-channel.version.NONE_FOR_WILDFLY");
		properties.set("bom.wildfly-ee.version", "30.0.0.Final");

		// Act
		WildflyApplicationConfiguration app = new WildflyApplicationConfiguration() {
		};

		// Assert
        Assertions.assertEquals(System.getProperty("wildfly-maven-plugin.groupId"), app.wildflyMavenPluginGroupId());
        Assertions.assertEquals(System.getProperty("wildfly-maven-plugin.artifactId"), app.wildflyMavenPluginArtifactId());
        Assertions.assertEquals(System.getProperty("wildfly-maven-plugin.version"), app.wildflyMavenPluginVersion());
        Assertions.assertEquals(System.getProperty("wildfly.ee-feature-pack.location"), app.eeFeaturePackLocation());
        Assertions.assertEquals(System.getProperty("wildfly.feature-pack.location"), app.featurePackLocation());
        Assertions.assertEquals(System.getProperty("wildfly.cloud-feature-pack.location"), app.cloudFeaturePackLocation());
        Assertions.assertEquals(System.getProperty("wildfly.datasources-feature-pack.location"), app.datasourcesFeaturePackLocation());
        Assertions.assertEquals(System.getProperty("wildfly.keycloak-saml-adapter-feature-pack.version"), app.keycloakSamlAdapterFeaturePackVersion());
        Assertions.assertEquals(System.getProperty("wildfly.ee-channel.groupId"), app.eeChannelGroupId());
        Assertions.assertEquals(System.getProperty("wildfly.ee-channel.artifactId"), app.eeChannelArtifactId());
        Assertions.assertEquals(System.getProperty("wildfly.ee-channel.version"), app.eeChannelVersion());
        Assertions.assertEquals(System.getProperty("bom.wildfly-ee.version"), app.bomsEeServerVersion());
	}

	/**
	 * Verify that the value for the Maven additional arguments environment variable (MAVEN_ARGS_APPEND) that
	 * should be passed to a s2i build is generated properly
	 * @param properties a {@link SystemProperties} instance to set properties which are exposed via
	 *                   {@link WildflyApplicationConfiguration}
	 */
	@Test
	void generatedMavenArgsAppendValueIncludesAllSystemProperties(SystemProperties properties) {
		// Arrange
		properties.set("wildfly-maven-plugin.groupId", "org.wildfly.plugins");
		properties.set("wildfly-maven-plugin.artifactId", "wildfly-maven-plugin");
		properties.set("wildfly-maven-plugin.version", "4.2.2.Final");
		properties.set("wildfly.ee-feature-pack.location", "org.wildfly:wildfly-ee-galleon-pack:30.0.0.Final");
		properties.set("wildfly.feature-pack.location", "org.wildfly:wildfly-galleon-pack:30.0.0.Final");
		properties.set("wildfly.cloud-feature-pack.location", "org.wildfly.cloud:wildfly-cloud-galleon-pack:5.0.1.Final");
		properties.set("wildfly.datasources-feature-pack.location", "org.wildfly:wildfly-datasources-galleon-pack:6.0.0.Final");
		properties.set("wildfly.keycloak-saml-adapter-feature-pack.version", "22.0.3");
		properties.set("wildfly.ee-channel.groupId", "wildfly.ee-channel.groupId.NONE_FOR_WILDFLY");
		properties.set("wildfly.ee-channel.artifactId", "wildfly.ee-channel.artifactId.NONE_FOR_WILDFLY");
		properties.set("wildfly.ee-channel.version", "wildfly.ee-channel.version.NONE_FOR_WILDFLY");
		properties.set("bom.wildfly-ee.version", "30.0.0.Final");

		// Act
		WildflyApplicationConfiguration app = new WildflyApplicationConfiguration() {
		};
		final String mavenArgs = app.generateAdditionalMavenArgs();

		// Assert
		Assertions.assertTrue(mavenArgs.contains(String.format(" -Dwildfly-maven-plugin.groupId=%s",
				app.wildflyMavenPluginGroupId())));
		Assertions.assertTrue(mavenArgs.contains(String.format(" -Dwildfly-maven-plugin.artifactId=%s",
				app.wildflyMavenPluginArtifactId())));
		Assertions.assertTrue(mavenArgs.contains(String.format(" -Dwildfly-maven-plugin.version=%s",
				app.wildflyMavenPluginVersion())));
		Assertions.assertTrue(mavenArgs.contains(String.format(" -Dwildfly.ee-feature-pack.location=%s",
				app.eeFeaturePackLocation())));
		Assertions.assertTrue(mavenArgs.contains(String.format(" -Dwildfly.feature-pack.location=%s",
				app.featurePackLocation())));
		Assertions.assertTrue(mavenArgs.contains(String.format(" -Dwildfly.cloud-feature-pack.location=%s",
				app.cloudFeaturePackLocation())));
		Assertions.assertTrue(mavenArgs.contains(String.format(" -Dwildfly.datasources-feature-pack.location=%s",
				app.datasourcesFeaturePackLocation())));
		Assertions.assertTrue(mavenArgs.contains(String.format(" -Dwildfly.keycloak-saml-adapter-feature-pack.version=%s",
				app.keycloakSamlAdapterFeaturePackVersion())));
		Assertions.assertTrue(mavenArgs.contains(String.format(" -Dwildfly.ee-channel.groupId=%s",
				app.eeChannelGroupId())));
		Assertions.assertTrue(mavenArgs.contains(String.format(" -Dwildfly.ee-channel.artifactId=%s",
				app.eeChannelArtifactId())));
		Assertions.assertTrue(mavenArgs.contains(String.format(" -Dwildfly.ee-channel.version=%s",
				app.eeChannelVersion())));
		Assertions.assertTrue(mavenArgs.contains(String.format(" -Dbom.wildfly-ee.version=%s",
				app.bomsEeServerVersion())));
	}

	@Test
	void generatedMavenArgsIncludeValidProfiles() {
		// Arrange not needed because this test is based on compiled DeploymentProperties and included selectively by
		// the Surefire Maven Plugin

		// Act
		WildflyApplicationConfiguration app = new WildflyApplicationConfiguration() {
		};
		final String mavenArgs = app.generateAdditionalMavenArgs();

		// Assert
		Assertions.assertFalse(mavenArgs.contains(" -Pbuild-profile."));
		Assertions.assertFalse(mavenArgs.contains(" -Pbuild-stream."));
	}
}
