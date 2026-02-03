package org.jboss.qa.appsint.eap.bootablejar.keycloak.client.oidc;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.HttpMethodConstraint;
import jakarta.servlet.annotation.ServletSecurity;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.Principal;

@WebServlet("/secured")
@ServletSecurity(httpMethodConstraints = { @HttpMethodConstraint(value = "GET", rolesAllowed = { "user" }) })
public class SecuredServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		Principal user = req.getUserPrincipal();
		String userName = user != null ? user.toString() : "NO AUTHENTICATED USER";
		try (PrintWriter writer = resp.getWriter()) {
			if (user != null) {
				writer.print("The user is authenticated");
			}
			writer.print(String.format("Current Principal: '%s'", userName));
		}
		if (req.getParameter("logout") != null) {
			req.logout();
			resp.sendRedirect(req.getServerName());
		}
	}

}
