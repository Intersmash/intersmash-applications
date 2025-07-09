/*
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
package org.jboss.intersmash.applications.wildfly;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * This class is executed when building applications for JBoss EAP 8.x, to verify that the specific bits are used.
 */
public class Eap8BuildStreamTest {

	/**
	 * Verifies that JBoss EAP 8.x applications' provisioning file contains the JBoss EAP
	 * {@code wildfly-ee-galleon-pack} declaration.
	 */
	@Test
	void validateEap81BuildProvisioningFile() throws IOException {
		final Stream<Path> provisionedServerPaths = Stream.of(/* no JBoss EAP 8 apps yet... */);

		provisionedServerPaths.forEach((path) -> {
			final Path provisionedServerPath = Path
					.of(path + "/.wildfly-maven-plugin-provisioning.xml");
			String provisioningFileContents = null;
			try {
				provisioningFileContents = Files.readString(provisionedServerPath);
				Assertions.assertTrue(
						provisioningFileContents.contains("<feature-pack location=\"org.jboss.eap:wildfly-ee-galleon-pack"));
			} catch (IOException e) {
				Assertions.fail(e);
			}
		});
	}
}
