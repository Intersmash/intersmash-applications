package org.jboss.intersmash.applications.wildfly;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import uk.org.webcompere.systemstubs.jupiter.SystemStubsExtension;

/**
 * Verifies {@link WildflyApplicationConfiguration} functionality, based on documented WildFly application build profiles.
 */
@ExtendWith(SystemStubsExtension.class)
public class WildflyApplicationBuildProfilesTest {

	@Test
	void generatedMavenArgsIncludeValidProfiles() {
		// Arrange not needed because this test is based on compiled ApplicationConfigurationProperties and included selectively by
		// the Surefire Maven Plugin

		// Act
		WildflyApplicationConfiguration app = new WildflyApplicationConfiguration() {
		};
		final String mavenArgs = app.generateAdditionalMavenArgs();

		// Assert
		Assertions.assertFalse(mavenArgs.contains(" -Pbuild-distribution."));
		Assertions.assertFalse(mavenArgs.contains(" -Pbuild-stream."));
	}
}
