package org.eap.bootable.saml.ejb;

import jakarta.ejb.Local;

import java.security.Principal;

@Local
public interface SecureEJBLocal {
	Principal getCallerPrincipal();

	boolean isCallerInRole(String role);

	void allowUserMethod();

	void allowManagerMethod();

	void allowFakeMethod();

	void denyAllMethod();
}
