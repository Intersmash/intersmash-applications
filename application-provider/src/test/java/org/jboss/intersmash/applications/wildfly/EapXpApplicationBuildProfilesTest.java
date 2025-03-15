package org.jboss.intersmash.applications.wildfly;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Verifies {@link WildflyApplicationConfiguration} functionality, based on
 * documented WildFly application build profiles.
 */
public class EapXpApplicationBuildProfilesTest {

	@Test
	void generatedMavenArgsIncludeValidProfiles() {
		// Arrange not needed because this test is based on compiled
		// ApplicationConfigurationProperties and included selectively by
		// the Surefire Maven Plugin...

		// Act
		WildflyApplicationConfiguration app = new WildflyApplicationConfiguration() {
		};
		final String mavenArgs = app.generateAdditionalMavenArgs();

		// Assert
		Assertions.assertTrue(mavenArgs.contains(" -Pbuild-distribution.eap"));
		Assertions.assertTrue(mavenArgs.contains(" -Pbuild-stream.eapxp")); // + ("5"|"6")
	}
}
