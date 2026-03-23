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

import jakarta.annotation.Resource;
import jakarta.annotation.security.DeclareRoles;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.SessionContext;
import jakarta.ejb.Stateless;

/**
 * Stateless session bean that provides access to security principal information.
 * <p>
 * This EJB is secured with role-based access control and requires the caller
 * to have the "user-role" role to invoke its methods.
 * </p>
 */
@Stateless
@DeclareRoles({ "user-role" })
public class StatelessEJB implements StatelessEJBLocal {

	/**
	 * Session context that provides access to the runtime context of a session bean.
	 */
	@Resource
	private SessionContext context;

	/**
	 * Retrieves the username of the caller principal.
	 * <p>
	 * This method is restricted to users with the "user-role" role.
	 * </p>
	 *
	 * @return the name of the caller principal, or null if no principal is available
	 */
	@RolesAllowed("user-role")
	public String getUsername() {
		if (context.getCallerPrincipal() != null) {
			return context.getCallerPrincipal().getName();
		}
		return null;
	}
}
