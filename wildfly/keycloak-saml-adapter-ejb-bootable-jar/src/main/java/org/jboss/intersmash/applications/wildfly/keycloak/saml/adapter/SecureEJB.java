package org.eap.bootable.saml.ejb;

import jakarta.annotation.Resource;
import jakarta.annotation.security.DeclareRoles;
import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.SessionContext;
import jakarta.ejb.Stateless;

import java.security.Principal;

@Stateless
@DeclareRoles({ "user", "manager", "fake" })
public class SecureEJB implements SecureEJBLocal {
	@Resource
	private SessionContext context;

	public Principal getCallerPrincipal() {
		return context.getCallerPrincipal();
	}

	public boolean isCallerInRole(String role) {
		return context.isCallerInRole(role);
	}

	@RolesAllowed("user")
	public void allowUserMethod() {
	}

	@RolesAllowed("manager")
	public void allowManagerMethod() {
	}

	@RolesAllowed("fake")
	public void allowFakeMethod() {
	}

	@DenyAll
	public void denyAllMethod() {
	}

	public String toString() {
		return "SecureEJB[userName=" + getCallerPrincipal() + "]";
	}
}
