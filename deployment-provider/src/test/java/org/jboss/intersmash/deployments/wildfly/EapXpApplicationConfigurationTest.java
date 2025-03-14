package org.jboss.intersmash.deployments.wildfly;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Verifies {@link WildflyApplicationConfiguration} functionality, based on documented WildFly application configuration
 * properties.
 */
public class EapXpApplicationConfigurationTest {

	@Test
	void generatedMavenArgsIncludeValidProfiles() {
		// Arrange not needed because this test is based on compiled DeploymentProperties and included selectively by
		// the Surefire Maven Plugin

		// Act
		WildflyApplicationConfiguration app = new WildflyApplicationConfiguration() {
		};
		final String mavenArgs = app.generateAdditionalMavenArgs();

		// Assert
		Assertions.assertTrue(mavenArgs.contains(" -Pbuild-profile.eap"));
		Assertions.assertTrue(mavenArgs.contains(" -Pbuild-stream.eapxp")); // + ("5"|"6")
	}
}
