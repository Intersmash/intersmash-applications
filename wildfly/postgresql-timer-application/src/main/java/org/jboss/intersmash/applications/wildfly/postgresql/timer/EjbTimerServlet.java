/**
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
package org.jboss.intersmash.applications.wildfly.postgresql.timer;

import jakarta.annotation.Resource;
import jakarta.ejb.EJB;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import javax.sql.DataSource;

@WebServlet(name = "EjbTimerServlet", urlPatterns = { "/EjbTimerServlet" })
public class EjbTimerServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public static final String DATASOURCE_NAME = "PostgreSQLDS";

	@EJB
	private EjbTimerBean ejbTimerBean;

	@Resource(lookup = "java:jboss/datasources/" + DATASOURCE_NAME)
	private DataSource ds;

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		resp.setContentType("text/plain");
		final PrintWriter writer = resp.getWriter();
		String request = req.getParameter("request");

		switch (request) {
			case "CREATE_TIMER":
				ejbTimerBean.createTimer();

				try (Connection connection = ds.getConnection();
						Statement stmt = connection.createStatement();
						ResultSet rs = stmt.executeQuery("select next_date from jboss_ejb_timer")) {
					Timestamp nextDate = null;
					while (rs.next()) {
						nextDate = rs.getTimestamp("next_date");
					}
					writer.print("next_date:" + nextDate);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
				break;
			case "CREATE_TIMER_FAILS":
				try {
					ejbTimerBean.createTimer();
				} catch (Exception e) {
					resp.setStatus(500);
					String responseBody = String.format("An error occurred while processing the client request: %s - %s",
							e.getClass().getSimpleName(), e.getMessage());
					writer.write(responseBody);
				}
				break;
			default:
				throw new IllegalArgumentException("Unrecognised request parameter " + request);
		}
	}
}
