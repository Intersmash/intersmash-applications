package org.jboss.intersmash.applications.maven;

import java.io.File;
import java.nio.file.Files;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ArtifactProviderTest {

	@Test
	void builtArtifactIsResolvedSuccessfully() {
		// Arrange
		final String groupId = "org.jboss.intersmash.applications";
		final String artifactId = "intersmash-applications";
		final String version = "0.0.1-SNAPSHOT";
		final String type = "pom";
		final String classifier = "";

		// Act
		File artifactFile = null;
		try {
			artifactFile = ArtifactProvider.resolveArtifact(groupId, artifactId, version, type, classifier);
		} catch (Throwable e) {
			Assertions.fail("Artifact resolution failed.", e);
		}

		// Assert
		Assertions.assertNotNull(artifactFile);
		Assertions.assertTrue(Files.exists(artifactFile.toPath()));
	}
}