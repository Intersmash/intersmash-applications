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
