/**
* Copyright (C) 2025 Red Hat, Inc.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*         http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
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