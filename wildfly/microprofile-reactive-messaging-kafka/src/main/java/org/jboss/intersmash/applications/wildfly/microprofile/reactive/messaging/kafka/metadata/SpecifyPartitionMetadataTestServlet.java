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
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Test servlet which can be used to invoke common JMS tasks in test classes.
 */
@WebServlet("/partitionsMetadata")
public class SpecifyPartitionMetadataTestServlet extends HttpServlet {

	private static final Logger LOGGER = Logger.getLogger(SpecifyPartitionMetadataTestServlet.class.toString());

	@Inject
	SpecifyPartitionBean specifyPartitionBean;

	private static final long TIMEOUT = 15000;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/plain");
		PrintWriter pw = resp.getWriter();
		pw.println("Starting SpecifyPartitionBean metadata servlet; waiting for data...");

		// Let's wait until bean receives all expected data.
		try {
			/*
			 * The first 10 messages are assigned the partition by the partitioner - the
			 * last 10 specify it in the metadata. There are two sets of each - the first
			 * specifies 1 as the partition for the specified ones, the second does 2.
			 */
			if (specifyPartitionBean.getLatch().await(TIMEOUT, TimeUnit.MILLISECONDS)) {
				Map<Integer, IncomingKafkaRecordMetadata<String, Integer>> noPartition4 = specifyPartitionBean
						.getNoPartitionSpecifiedMetadata4();
				Map<Integer, IncomingKafkaRecordMetadata<String, Integer>> definedPartition4 = specifyPartitionBean
						.getPartitionSpecifiedMetadata4();

				pw.println("Metadata4 unspecified partition: " + noPartition4.size());
				pw.println("Metadata4 specified partition: " + definedPartition4.size());

				for (int i = 1; i <= 10; i++) {
					pw.println("Metadata4, item " + i + " partition " + noPartition4.get(i).getPartition());
				}
				for (int i = 11; i <= 20; i++) {
					pw.println("Metadata4, item " + i + " partition " + definedPartition4.get(i).getPartition());
				}

				Map<Integer, IncomingKafkaRecordMetadata<String, Integer>> noPartition5 = specifyPartitionBean
						.getNoPartitionSpecifiedMetadata5();
				Map<Integer, IncomingKafkaRecordMetadata<String, Integer>> definedPartition5 = specifyPartitionBean
						.getPartitionSpecifiedMetadata5();

				pw.println("Metadata5 unspecified partition: " + noPartition5.size());
				pw.println("Metadata5 specified partition: " + definedPartition5.size());

				for (int i = 1; i <= 10; i++) {
					pw.println("Metadata5, item " + i + " partition " + noPartition5.get(i).getPartition());
				}
				for (int i = 11; i <= 20; i++) {
					pw.println("Metadata5, item " + i + " partition " + definedPartition5.get(i).getPartition());
				}
			} else {
				pw.println("Timed out. SpecifyPartitionBean hasn't received all expected messages in time.");
			}
		} catch (InterruptedException e) {
			LOGGER.log(Level.SEVERE, "Wait interrupted...", e);
			pw.println(e.getMessage());
		}
		pw.close();
	}
}
