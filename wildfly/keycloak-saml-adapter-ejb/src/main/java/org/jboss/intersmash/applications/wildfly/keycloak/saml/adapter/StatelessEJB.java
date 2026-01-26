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
