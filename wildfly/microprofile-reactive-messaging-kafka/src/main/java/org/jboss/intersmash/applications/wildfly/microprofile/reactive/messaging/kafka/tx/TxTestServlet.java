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
package org.jboss.intersmash.applications.wildfly.microprofile.reactive.messaging.kafka.tx;

import jakarta.inject.Inject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Test servlet which can be used to invoke common JMS tasks in test classes.
 */
@WebServlet("/tx")
public class TxTestServlet extends HttpServlet {

	private static final Logger LOGGER = Logger.getLogger(TxTestServlet.class.toString());

	@Inject
	Bean bean;

	@Inject
	TransactionalBean txBean;

	private static final long TIMEOUT = 15000;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/plain");
		PrintWriter pw = resp.getWriter();
		pw.println("Starting tx servlet; waiting for data...");

		// Let's wait until bean receives all expected data.
		try {
			if (bean.getLatch().await(TIMEOUT, TimeUnit.MILLISECONDS)) {
				// Storing of data to the database may change order of the send data.
				List<String> list = bean.getWords();

				// Print items we've got to the final 'sink' method
				pw.println("items: " + list.size());
				for (String item : list) {
					pw.println("item - " + item);
				}

				// Print items that have been pushed to the database
				pw.println("database records: " + txBean.getCount());
				for (String item : txBean.getDbRecords()) {
					pw.println("db record - " + item);
				}
			} else {
				pw.println("Timed out. Bean hasn't received all expected messages in time.");
			}
		} catch (InterruptedException e) {
			LOGGER.log(Level.SEVERE, "Wait interrupted...", e);
			pw.println(e.getMessage());
		}
		pw.close();
	}
}
