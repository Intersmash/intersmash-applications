package org.jboss.intersmash.applications.wildfly.distributed.sessions.infinispan;

import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import org.jboss.intersmash.applications.wildfly.distributed.sessions.infinispan.utils.Constants;

/**
 * Logs when a session is created / destroyed
 */
@WebListener
public class LoggingHttpSessionListener implements HttpSessionListener {

	@Override
	public void sessionCreated(HttpSessionEvent se) {
		se.getSession().setMaxInactiveInterval(Constants.SESSION_CONVENTIONAL_EXPIRATION_TIMEOUT_SECONDS);
		System.out.println("\n*****************************************************************");
		System.out.println(String.format(Constants.SESSION_CONVENTIONAL_CREATION_MESSAGE_TEMPLATE, se.getSession().getId(),
				se.getSession().getMaxInactiveInterval()));
		System.out.println("*****************************************************************\n");
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		System.out.println("\n*****************************************************************");
		System.out.println(String.format(Constants.SESSION_CONVENTIONAL_EXPIRATION_MESSAGE_TEMPLATE, se.getSession().getId()));
		System.out.println("*****************************************************************\n");
	}
}
