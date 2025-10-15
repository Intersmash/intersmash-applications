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
package org.jboss.intersmash.applications;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.maven.settings.building.SettingsBuildingException;
import org.eclipse.aether.resolution.ArtifactResolutionException;
import org.jboss.intersmash.applications.maven.ArtifactProvider;

/**
 * A class which is expected to provide access to applications. Archive based
 * deployments (e.g.: WAR, JAR) must be installed in local repository.
 */
public class ApplicationProvider {
	static final String WILDFLY_MICROPROFILE_REACTIVE_MESSAGING_KAFKA_DEPLOYMENT = "wildfly-microprofile-reactive-messaging-kafka";
	static final String WILDFLY_ELYTRON_OIDC_CLIENT_KEYCLOAK_DEPLOYMENT = "wildfly-elytron-oidc-client-keycloak";
	static final String WILDFLY_WEB_CACHE_OFFLOAD_INFINISPAN_DEPLOYMENT = "wildfly-web-cache-offload-infinispan";
	static final String WILDFLY_DISTRIBUTED_SESSIONS_INFINISPAN_DEPLOYMENT = "wildfly-distributed-sessions-infinispan";
	static final String WILDFLY_AMQ_BROKER_SSL_DEPLOYMENT = "wildfly-amq-broker-ssl";
	static final String WILDFLY_DEPLOYMENT_ARTIFACT_PACKAGING_WAR = "war";

	/**
	 * Provides access to a filesystem directory containing a server provisioned by
	 * the WildFly/JBoss EAP Maven plugin, which is generated from the
	 * {@code wildfly-microprofile-reactive-messaging-kafka} application
	 *
	 * @return {@link Path} instance that identifies the directory containing the
	 *         WildFly/JBoss EAP provisioned server.
	 */
	public static Path wildflyMicroprofileReactiveMessagingKafkaProvisionedServerPath() {
		return findApplicationDirectory("wildfly", "microprofile-reactive-messaging-kafka", "target", "server");
	}

	/**
	 * Provides access to a filesystem directory containing a server provisioned by
	 * the WildFly/JBoss EAP 8.x Maven plugin, which is generated from the
	 * {@code wildfly-distributed-sessions-infinispan} application
	 *
	 * @return {@link Path} instance that identifies the directory containing the
	 *         WildFly/JBoss EAP 8.x provisioned server.
	 */
	public static Path wildflyDistributedSessionsInfinispanProvisionedServerPath() {
		return findApplicationDirectory("wildfly", "distributed-sessions-infinispan", "target", "server");
	}

	/**
	 * Provides access to a filesystem directory containing a server provisioned by
	 * the WildFly/JBoss EAP 8.x Maven plugin, which is generated from the
	 * {@code wildfly-amq-broker-ssl} application
	 *
	 * @return {@link Path} instance that identifies the directory containing the
	 *         WildFly/JBoss EAP 8.x provisioned server.
	 */
	public static Path wildflyActiveMQArtemisProvisionedServerPath() {
		return findApplicationDirectory("wildfly", "amq-broker-ssl", "target", "server");
	}

	/**
	 * Provides access to a filesystem directory containing a server provisioned by
	 * the WildFly/JBoss EAP 8.x Maven plugin, which is generated from the
	 * {@code wildfly-distributed-sessions-infinispan} application
	 *
	 * @return {@link Path} instance that identifies the directory containing the
	 *         WildFly/JBoss EAP 8.x provisioned server.
	 */
	public static Path wildflyElytronOidcClientKeycloakProvisionedServerPath() {
		return findApplicationDirectory("wildfly", "elytron-oidc-client-keycloak", "target", "server");
	}

	/**
	 * Provides access to a filesystem directory containing a server provisioned by
	 * the WildFly/JBoss EAP 8.x Maven plugin, which is generated from the
	 * {@code wildfly-web-cache-offload-infinispan} application
	 *
	 * @return {@link Path} instance that identifies the directory containing the
	 *         WildFly/JBoss EAP 8.x provisioned server.
	 */
	public static Path wildflyWebCacheOffloadInfinispanProvisionedServerPath() {
		return findApplicationDirectory("wildfly", "web-cache-offload-infinispan", "target", "server");
	}

	/**
	 * Provides access to a WAR deployment containing the
	 * {@code wildfly-microprofile-reactive-messaging-kafka} application
	 *
	 * @return {@link Path} instance that identifies the WAR artifact containing the
	 *         {@code wildfly-microprofile-reactive-messaging-kafka} application.
	 */
	public static Path wildflyMicroprofileReactiveMessagingKafkaDeploymentPath() {
		Path file = null;
		try {
			file = ArtifactProvider.resolveArtifact(ApplicationConfigurationProperties.groupID(),
					WILDFLY_MICROPROFILE_REACTIVE_MESSAGING_KAFKA_DEPLOYMENT,
					ApplicationConfigurationProperties.version(), WILDFLY_DEPLOYMENT_ARTIFACT_PACKAGING_WAR, null)
					.toPath();
		} catch (SettingsBuildingException | ArtifactResolutionException e) {
			throw new RuntimeException("Can not get artifact", e);
		}
		return file;
	}

	/**
	 * Provides access to a WAR deployment containing the
	 * {@code wildfly-elytron-oidc-client-keycloak} application
	 *
	 * @return {@link Path} instance that identifies the WAR artifact containing the
	 *         {@code wildfly-elytron-oidc-client-keycloak} application.
	 */
	public static Path wildflyElytronOidcClientKeycloakDeploymentPath() {
		Path file = null;
		try {
			file = ArtifactProvider.resolveArtifact(ApplicationConfigurationProperties.groupID(),
					WILDFLY_ELYTRON_OIDC_CLIENT_KEYCLOAK_DEPLOYMENT,
					ApplicationConfigurationProperties.version(), WILDFLY_DEPLOYMENT_ARTIFACT_PACKAGING_WAR, null)
					.toPath();
		} catch (SettingsBuildingException | ArtifactResolutionException e) {
			throw new RuntimeException("Can not get artifact", e);
		}
		return file;
	}

	/**
	 * Provides access to a WAR deployment containing the
	 * {@code wildfly-web-cache-offload-infinispan} application
	 *
	 * @return {@link Path} instance that identifies the WAR artifact containing the
	 *         {@code wildfly-web-cache-offload-infinispan} application.
	 */
	public static Path wildflyWebCacheOffloadInfinispanDeploymentPath() {
		Path file = null;
		try {
			file = ArtifactProvider.resolveArtifact(ApplicationConfigurationProperties.groupID(),
					WILDFLY_WEB_CACHE_OFFLOAD_INFINISPAN_DEPLOYMENT,
					ApplicationConfigurationProperties.version(), WILDFLY_DEPLOYMENT_ARTIFACT_PACKAGING_WAR, null)
					.toPath();
		} catch (SettingsBuildingException | ArtifactResolutionException e) {
			throw new RuntimeException("Can not get artifact", e);
		}
		return file;
	}

	/**
	 * Provides access to a WAR deployment containing the
	 * {@code wildfly-distributed-sessions-infinispan} application
	 *
	 * @return {@link Path} instance that identifies the WAR artifact containing the
	 *         {@code wildfly-distributed-sessions-infinispan} application.
	 */
	public static Path wildflyDistributedSessionsInfinispanDeploymentPath() {
		Path file = null;
		try {
			file = ArtifactProvider.resolveArtifact(ApplicationConfigurationProperties.groupID(),
					WILDFLY_DISTRIBUTED_SESSIONS_INFINISPAN_DEPLOYMENT,
					ApplicationConfigurationProperties.version(), WILDFLY_DEPLOYMENT_ARTIFACT_PACKAGING_WAR, null)
					.toPath();
		} catch (SettingsBuildingException | ArtifactResolutionException e) {
			throw new RuntimeException("Can not get artifact", e);
		}
		return file;
	}

	/**
	 * Provides access to a WAR deployment containing the
	 * {@code wildfly-amq-broker-ssl} application
	 *
	 * @return {@link Path} instance that identifies the WAR artifact containing the
	 *         {@code wildfly-amq-broker-ssl} application.
	 */
	public static Path wildflyActiveMQArtemisDeploymentPath() {
		Path file = null;
		try {
			file = ArtifactProvider.resolveArtifact(ApplicationConfigurationProperties.groupID(),
					WILDFLY_AMQ_BROKER_SSL_DEPLOYMENT,
					ApplicationConfigurationProperties.version(), WILDFLY_DEPLOYMENT_ARTIFACT_PACKAGING_WAR, null)
					.toPath();
		} catch (SettingsBuildingException | ArtifactResolutionException e) {
			throw new RuntimeException("Can not get artifact", e);
		}
		return file;
	}

	/**
	 * Provides access the provided applications base path.
	 *
	 * @return A {@link Path} instance that represents the base path of the provided
	 *         applications.
	 */
	public static Path getApplicationsBasePath() {
		return Path.of(ApplicationConfigurationProperties.getApplicationProviderPath()).getParent();
	}

	private static Path findApplicationDirectory(String... appPathTokens) {
		File applicationsBasedir = getApplicationsBasePath().toFile();
		Path path = Paths.get(applicationsBasedir.getAbsolutePath(), appPathTokens);
		if (path.toFile().exists() && path.toFile().isDirectory()) {
			return path;
		}
		throw new RuntimeException(
				"Cannot find the provisioned WildFly/JBoss EAP server directory: " + path.toFile().getAbsolutePath());
	}
}
