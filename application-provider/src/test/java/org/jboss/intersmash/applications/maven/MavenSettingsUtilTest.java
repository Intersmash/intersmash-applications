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
