/*
 * JBoss, Home of Professional Open Source
 * Copyright 2016, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.intersmash.applications.wildfly.keycloak.saml.adapter;

import jakarta.servlet.http.HttpServletRequest;
import org.keycloak.adapters.saml.SamlDeploymentContext;
import org.keycloak.adapters.saml.SamlPrincipal;
import org.keycloak.common.util.KeycloakUriBuilder;
import org.keycloak.constants.ServiceUrlConstants;

/**
 * Controller simplifies access to the server environment from the JSP.
 *
 * @author Stan Silvert ssilvert@redhat.com (C) 2015 Red Hat Inc.
 */
public class Controller {

	/**
	 * Retrieves the first name (given name) of the currently authenticated user.
	 *
	 * @param req the HTTP servlet request containing the user principal
	 * @return the first name of the authenticated user, or null if not available
	 */
	public String getFirstName(HttpServletRequest req) {
		return getFriendlyAttrib(req, "givenName");
	}

	/**
	 * Retrieves the last name (surname) of the currently authenticated user.
	 *
	 * @param req the HTTP servlet request containing the user principal
	 * @return the last name of the authenticated user, or null if not available
	 */
	public String getLastName(HttpServletRequest req) {
		return getFriendlyAttrib(req, "surname");
	}

	/**
	 * Retrieves the email address of the currently authenticated user.
	 *
	 * @param req the HTTP servlet request containing the user principal
	 * @return the email address of the authenticated user, or null if not available
	 */
	public String getEmail(HttpServletRequest req) {
		return getFriendlyAttrib(req, "email");
	}

	/**
	 * Retrieves the username of the currently authenticated user.
	 *
	 * @param req the HTTP servlet request containing the user principal
	 * @return the username of the authenticated user
	 */
	public String getUsername(HttpServletRequest req) {
		return getAccount(req).getName();
	}

	/**
	 * Retrieves a friendly SAML attribute by name from the authenticated user's principal.
	 *
	 * @param req the HTTP servlet request containing the user principal
	 * @param attribName the name of the attribute to retrieve
	 * @return the value of the requested attribute, or null if not available
	 */
	private String getFriendlyAttrib(HttpServletRequest req, String attribName) {
		SamlPrincipal principal = getAccount(req);
		return principal.getFriendlyAttribute(attribName);
	}

	/**
	 * Retrieves the SAML principal associated with the current request.
	 *
	 * @param req the HTTP servlet request containing the user principal
	 * @return the SAML principal representing the authenticated user
	 */
	private SamlPrincipal getAccount(HttpServletRequest req) {
		SamlPrincipal principal = (SamlPrincipal) req.getUserPrincipal();
		return principal;
	}

	/**
	 * Checks if a user is currently logged in.
	 *
	 * @param req the HTTP servlet request to check for authentication
	 * @return true if a user is logged in, false otherwise
	 */
	public boolean isLoggedIn(HttpServletRequest req) {
		return getAccount(req) != null;
	}

	/**
	 * Constructs the URI for accessing the user's Keycloak account management page.
	 *
	 * @param req the HTTP servlet request containing SAML deployment context
	 * @return the complete URI to the Keycloak account management page with referrer parameter
	 */
	public String getAccountUri(HttpServletRequest req) {
		String serverPath = findKeycloakServerPath(req);
		String realm = findRealmName(req);
		return KeycloakUriBuilder.fromUri(serverPath).path(ServiceUrlConstants.ACCOUNT_SERVICE_PATH)
				.queryParam("referrer", "app-profile-saml").build(realm).toString();
	}

	/**
	 * Extracts the realm name from the Keycloak binding URL.
	 * <p>
	 * Note: This implementation parses the URL specified in the SAML configuration.
	 * The binding URL is expected to follow the format:
	 * http://localhost:8080/auth/realms/master/protocol/saml
	 * </p>
	 *
	 * @param req the HTTP servlet request containing SAML deployment context
	 * @return the realm name extracted from the binding URL
	 */
	private String findRealmName(HttpServletRequest req) {
		String bindingUrl = getBindingUrl(req);
		// bindingUrl looks like http://localhost:8080/auth/realms/master/protocol/saml
		int beginIndex = bindingUrl.indexOf("/realms/") + "/realms/".length();
		return bindingUrl.substring(beginIndex, bindingUrl.indexOf('/', beginIndex));
	}

	/**
	 * Extracts the Keycloak server base path from the binding URL.
	 * <p>
	 * The binding URL is expected to follow the format:
	 * http://localhost:8080/realms/master/protocol/saml
	 * This method returns only the server part: http://localhost:8080
	 * </p>
	 *
	 * @param req the HTTP servlet request containing SAML deployment context
	 * @return the base server path of the Keycloak server
	 */
	private String findKeycloakServerPath(HttpServletRequest req) {
		String bindingUrl = getBindingUrl(req);
		// bindingUrl looks like http://localhost:8080/realms/master/protocol/saml
		return bindingUrl.substring(0, bindingUrl.indexOf("/", bindingUrl.indexOf("//") + 2));
	}

	/**
	 * Retrieves the SAML binding URL from the SAML deployment context.
	 *
	 * @param req the HTTP servlet request from which to extract the binding URL
	 * @return the single sign-on service request binding URL
	 */
	private String getBindingUrl(HttpServletRequest req) {
		SamlDeploymentContext ctx = (SamlDeploymentContext) req.getServletContext()
				.getAttribute(SamlDeploymentContext.class.getName());
		return ctx.resolveDeployment(null).getIDP().getSingleSignOnService().getRequestBindingUrl();
	}

}
