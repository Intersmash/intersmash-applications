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
