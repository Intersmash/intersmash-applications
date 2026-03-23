/*
 * Copyright (C) 2026 Red Hat, Inc.
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
package org.jboss.intersmash.applications.wildfly.keycloak.saml.adapter;

import jakarta.ejb.Local;

/**
 * Local business interface for the stateless EJB that provides access to security principal information.
 * <p>
 * This interface defines the contract for retrieving user information from the EJB
 * in a local context (same JVM).
 * </p>
 */
@Local
public interface StatelessEJBLocal {
	/**
	 * Retrieves the username of the caller principal.
	 *
	 * @return the name of the caller principal, or null if no principal is available
	 */
	public String getUsername();
}
