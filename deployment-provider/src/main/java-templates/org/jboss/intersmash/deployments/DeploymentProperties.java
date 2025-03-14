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
package org.jboss.intersmash.deployments;

import java.io.IOException;
import java.util.Properties;

/**
 * Loads project and system properties that relate to Intersmash applications, and provides methods to access them.
 */
public class DeploymentProperties {
	public static final String WILDFLY_DEPLOYMENTS_BUILD_PROFILE_VALUE_EAP = "eap";
	public static final String WILDFLY_DEPLOYMENTS_BUILD_PROFILE_VALUE_EAP_XP = "eapxp";
	public static final String WILDFLY_DEPLOYMENTS_BUILD_STREAM_VALUE_COMMUNITY = "community";
	public static final String WILDFLY_DEPLOYMENTS_BUILD_STREAM_VALUE_EAP_80 = "eap80";
	public static final String WILDFLY_DEPLOYMENTS_BUILD_STREAM_VALUE_EAP_81 = "eap81";
	public static final String WILDFLY_DEPLOYMENTS_BUILD_STREAM_VALUE_EAP_XP5 = "eapxp5";
	public static final String WILDFLY_DEPLOYMENTS_BUILD_STREAM_VALUE_EAP_XP6 = "eapxp6";

	static String version() {
		return "${project.version}";
	}

	static String groupID() {
		return "${project.groupId}";
	}

	static String getDeploymentsProviderPath() {
		return "${project.basedir}";
	}

	/**
	 * Get the configured WildFly/EAP 8.x application <b>build profile</b>, i.e. whether the application is built out of
	 * either community (WildFly) bits, or based on productised (JBoss EAP and JBoss EAP XP) bits.
	 * @return A string representing the application <b>build profile</b>, i.e. {@code eap} or {@code eapxp} for
	 * JBoss EAP and EAP XP respectively, or an empty string for a WildFly (community bits) based application.
	 */
	public static String getWildflyDeploymentsBuildProfile() {
		return "${intersmash.applications.wildfly.build.profile}";
	}

	/**
	 * Get the configured WildFly/EAP 8.x application <b>build stream</b>, i.e. a given version stream that uniquely
	 * identifies a supported version. For WildFly only one stream is supported, while {@code 8.0.z} and {@code 8.1.x}
	 * JBoss EAP major version streams are supported. Similarly, JBoss EAP XP {@code 5.z} and {@code 6.z} are supported.
	 * @return A string representing the application <b>build stream</b>, i.e. either {@code community},
	 * {@code eap80}, {@code eap81}, {@code eapxp5} or {@code eapxp6}
	 */
	public static String getWildflyDeploymentsBuildStream() {
		return "${intersmash.applications.wildfly.build.stream}";
	}

	public static Boolean isWildFlyDeploymentsBuildProfileEnabled() {
		return "".equals(getWildflyDeploymentsBuildProfile());
	}

	public static Boolean isCommunityDeploymentsBuildStreamEnabled() {
		return WILDFLY_DEPLOYMENTS_BUILD_STREAM_VALUE_COMMUNITY.equals(getWildflyDeploymentsBuildStream());
	}

	public static Boolean isEapDeploymentsBuildProfileEnabled() {
		return WILDFLY_DEPLOYMENTS_BUILD_PROFILE_VALUE_EAP.equals(getWildflyDeploymentsBuildProfile());
	}

	public static Boolean isEap80DeploymentsBuildStreamEnabled() {
		return WILDFLY_DEPLOYMENTS_BUILD_STREAM_VALUE_EAP_80.equals(getWildflyDeploymentsBuildStream());
	}

	public static Boolean isEap81DeploymentsBuildStreamEnabled() {
		return WILDFLY_DEPLOYMENTS_BUILD_STREAM_VALUE_EAP_81.equals(getWildflyDeploymentsBuildStream());
	}

	public static Boolean isEapXpDeploymentsBuildProfileEnabled() {
		return WILDFLY_DEPLOYMENTS_BUILD_PROFILE_VALUE_EAP_XP.equals(getWildflyDeploymentsBuildProfile());
	}

	public static Boolean isEapXp5DeploymentsBuildStreamEnabled() {
		return WILDFLY_DEPLOYMENTS_BUILD_STREAM_VALUE_EAP_XP5.equals(getWildflyDeploymentsBuildStream());
	}

	public static Boolean isEapXp6DeploymentsBuildStreamEnabled() {
		return WILDFLY_DEPLOYMENTS_BUILD_STREAM_VALUE_EAP_XP6.equals(getWildflyDeploymentsBuildStream());
	}
}
