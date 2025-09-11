package org.jboss.intersmash.applications.wildfly.distributed.sessions.infinispan.utils;

public class Constants {
	public final static String SESSION_CONVENTIONAL_CREATION_MESSAGE_TEMPLATE = "Session %s created, will expire in %d seconds";
	public final static String SESSION_CONVENTIONAL_EXPIRATION_MESSAGE_TEMPLATE = "Session %s destroyed";
	public final static Integer SESSION_CONVENTIONAL_EXPIRATION_TIMEOUT_SECONDS = 120;
	public final static String INVALIDATE = "invalidate";
}
