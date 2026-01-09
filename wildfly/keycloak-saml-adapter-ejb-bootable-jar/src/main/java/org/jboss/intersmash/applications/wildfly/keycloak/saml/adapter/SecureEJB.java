package org.jboss.intersmash.applications.wildfly.keycloak.saml.adapter;

import jakarta.annotation.Resource;
import jakarta.annotation.security.DeclareRoles;
import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.SessionContext;
import jakarta.ejb.Stateless;

import java.security.Principal;

@Stateless
@DeclareRoles({ "user-role", "manager-role", "fake-role" })
public class SecureEJB implements SecureEJBLocal {
	@Resource
	private SessionContext context;

	public Principal getCallerPrincipal() {
		return context.getCallerPrincipal();
	}

	public boolean isCallerInRole(String role) {
		return context.isCallerInRole(role);
	}

	@RolesAllowed("user-role")
	public void allowUserMethod() {
	}

	@RolesAllowed("manager-role")
	public void allowManagerMethod() {
	}

	@RolesAllowed("fake-role")
	public void allowFakeMethod() {
	}

	@DenyAll
	public void denyAllMethod() {
	}

	public String toString() {
		return "SecureEJB[userName=" + getCallerPrincipal() + "]";
	}
}
