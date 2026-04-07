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
package org.jboss.intersmash.applications.wildfly.microprofile.reactive.messaging.kafka.metadata;

import io.smallrye.reactive.messaging.kafka.api.IncomingKafkaRecordMetadata;
import jakarta.inject.Inject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;

/**
 * Test servlet which can be used to invoke common JMS tasks in test classes.
 */
@WebServlet("/metadata")
public class MetadataTestServlet extends HttpServlet {

	private static final Logger LOGGER = Logger.getLogger(MetadataTestServlet.class.toString());

	@Inject
	BasicMetadataBean basicMetadataBean;

	private static final long TIMEOUT = 15000;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/plain");
		PrintWriter pw = resp.getWriter();
		pw.println("Starting BasicMetadataBean metadata servlet; waiting for data...");

		// Let's wait until bean receives all expected data.
		try {
			if (basicMetadataBean.getLatch().await(TIMEOUT, TimeUnit.MILLISECONDS)) {
				Map<Integer, IncomingKafkaRecordMetadata<String, Integer>> map2 = basicMetadataBean
						.getTesting2Metadatas();
				Map<Integer, IncomingKafkaRecordMetadata<String, Integer>> map3 = basicMetadataBean
						.getTesting3Metadatas();

				pw.println("Map 2 contains '" + map2.size() + "' items");
				pw.println("Map 3 contains '" + map3.size() + "' items");

				// Print the data, so we can check the content is correct.
				for (int i = 1; i <= 2; i++) {
					IncomingKafkaRecordMetadata metadata = map2.get(i);
					if (metadata != null) {
						pw.println("Map 2, data '" + i + "', topic '" + metadata.getTopic() + "', key '"
								+ metadata.getKey() + "', header '" + headersToString(metadata.getHeaders())
								+ "', timestamp '" + metadata.getTimestamp() + "'");
					}
				}
				for (int i = 3; i <= 4; i++) {
					IncomingKafkaRecordMetadata metadata = map3.get(i);
					if (metadata != null) {
						pw.println("Map 3, data '" + i + "', topic '" + metadata.getTopic() + "', key '"
								+ metadata.getKey() + "', header '" + headersToString(metadata.getHeaders())
								+ "', timestamp '" + metadata.getTimestamp() + "'");
					}
				}
			} else {
				pw.println("Timed out. BasicMetadataBean hasn't received all expected messages in time.");
			}
		} catch (InterruptedException e) {
			LOGGER.log(Level.SEVERE, "Wait interrupted...", e);
			pw.println(e.getMessage());
		}
		pw.close();
	}

	private String headersToString(Headers headers) {
		String result = "";

		if (headers != null) {
			for (Header header : headers) {
				result += header.key() + "=" + Arrays.toString(header.value());
			}
			return result;
		}

		return "null";
	}
}
