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

import jakarta.ejb.EJB;
import jakarta.ejb.EJBAccessException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.HttpConstraint;
import jakarta.servlet.annotation.ServletSecurity;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;

@WebServlet(value = "/secured")
@ServletSecurity(@HttpConstraint(rolesAllowed = { "user-role" }))
public class SecuredServlet extends HttpServlet {

	@EJB
	private SecureEJBLocal secureEJBLocal;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		handleRequest(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		handleRequest(request, response);
	}

	private void handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/plain");
		ServletOutputStream out = response.getOutputStream();

		out.println("Servlet - secured page");
		out.println();

		Principal principal = request.getUserPrincipal();
		if (principal != null) {
			out.println("Servlet.getUserPrincipal()=" + principal + " [" + principal.getName() + "]");
		} else {
			out.println("Servlet.getUserPrincipal()=<null>");
		}
		out.println("Servlet.isCallerInRole(\"user-role\")=" + request.isUserInRole("user-role"));
		out.println();

		out.println("@EJB=" + secureEJBLocal);
		try {
			secureEJBLocal.allowUserMethod();
			out.println("@EJB.allowUserMethod() ALLOWED");
		} catch (EJBAccessException e) {
			out.println("@EJB.allowUserMethod() DENIED");
		}

		try {
			secureEJBLocal.allowManagerMethod();
			out.println("@EJB.allowManagerMethod() ALLOWED");
		} catch (EJBAccessException e) {
			out.println("@EJB.allowManagerMethod() DENIED");
		}
		out.println();
	}
}
