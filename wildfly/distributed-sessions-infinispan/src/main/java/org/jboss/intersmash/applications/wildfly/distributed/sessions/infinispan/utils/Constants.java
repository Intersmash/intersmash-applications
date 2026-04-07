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
package org.jboss.intersmash.applications.wildfly.distributed.sessions.infinispan.utils;

public class Constants {
	public final static String SESSION_CONVENTIONAL_CREATION_MESSAGE_TEMPLATE = "Session %s created, will expire in %d seconds";
	public final static String SESSION_CONVENTIONAL_EXPIRATION_MESSAGE_TEMPLATE = "Session %s destroyed";
	public final static Integer SESSION_CONVENTIONAL_EXPIRATION_TIMEOUT_SECONDS = 120;
	public final static String INVALIDATE = "invalidate";
}
