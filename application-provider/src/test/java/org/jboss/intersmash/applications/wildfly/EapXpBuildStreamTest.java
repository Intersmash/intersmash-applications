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
package org.jboss.intersmash.applications.wildfly;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.jboss.intersmash.applications.ApplicationProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EapXpBuildStreamTest {

	@Test
	void validateEapXPBuildProvisioningFile() throws IOException {
		final Path provisionedServerPath = Path
				.of(ApplicationProvider.wildflyMicroprofileReactiveMessagingKafkaProvisionedServerPath() +
						"/.wildfly-maven-plugin-provisioning.xml");
		String provisioningFileContents = Files.readString(provisionedServerPath);

		Assertions.assertTrue(
				provisioningFileContents.contains("<feature-pack location=\"org.jboss.eap.xp:wildfly-galleon-pack"));
	}
}
