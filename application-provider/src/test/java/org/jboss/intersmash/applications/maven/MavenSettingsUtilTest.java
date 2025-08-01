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
package org.jboss.intersmash.applications.maven;

import org.apache.maven.settings.Settings;
import org.apache.maven.settings.building.SettingsBuildingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MavenSettingsUtilTest {

	@Test
	void settingsAreLoadedSuccessfully() {
		// Act
		Settings settings = null;
		try {
			settings = MavenSettingsUtil.loadSettings();
		} catch (SettingsBuildingException e) {
			Assertions.fail("An error occurred while loading the settings", e);
		}

		// Assert
		Assertions.assertNotNull(settings, "Loaded settings should not be null");
	}
}
