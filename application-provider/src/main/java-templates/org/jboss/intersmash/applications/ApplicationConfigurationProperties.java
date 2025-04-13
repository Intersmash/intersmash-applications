/**
 * Copyright (C) 2023 Red Hat, Inc.
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
package org.jboss.intersmash.applications;

import java.io.IOException;
import java.util.Properties;

/**
 * Loads project and system properties that relate to Intersmash applications, and provides methods to access them.
 */
public class ApplicationConfigurationProperties {

	static String version() {
		return "${project.version}";
	}

	static String groupID() {
		return "${project.groupId}";
	}

	static String getApplicationProviderPath() {
		return "${project.basedir}";
	}
}
