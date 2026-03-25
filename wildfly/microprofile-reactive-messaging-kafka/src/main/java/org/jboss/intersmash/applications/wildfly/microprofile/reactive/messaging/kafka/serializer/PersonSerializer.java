/*
 * Copyright (C) 2021 Red Hat, Inc.
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
package org.jboss.intersmash.applications.wildfly.microprofile.reactive.messaging.kafka.serializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.kafka.common.serialization.Serializer;

/**
 * Taken from WildFly testsuite, see
 * org.wildfly.test.integration.microprofile.reactive.messaging.kafka.serializer.PersonSerializer.
 *
 * @author <a href="mailto:kabir.khan@jboss.com">Kabir Khan</a>
 */
public class PersonSerializer implements Serializer<Person> {

	private static final Logger LOGGER = Logger.getLogger(PersonSerializer.class.toString());

	@Override
	public byte[] serialize(String topic, Person data) {
		if (data == null) {
			return null;
		}

		try {
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(bout);
			out.writeUTF(data.getName());
			out.writeInt(data.getAge());
			out.close();
			return bout.toByteArray();
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Couldn't serialize data!", e);
			throw new RuntimeException(e);
		}
	}

}
