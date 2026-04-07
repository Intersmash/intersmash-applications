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

import jakarta.annotation.Resource;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import org.jgroups.JChannel;

@SuppressWarnings("serial")
@WebServlet("/members")
public class MembersServlet extends HttpServlet {
	@Resource(lookup = "java:jboss/jgroups/channel/default")
	private JChannel channel;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		StringBuilder info = new StringBuilder();

		// Get current cache nodes
		info.append("Address: ").append(channel.getAddress()).append(System.lineSeparator());
		info.append("Coordinator: ").append(channel.getView().getCoord()).append(System.lineSeparator());
		info.append("Members: ").append(channel.getView().getMembers()).append(System.lineSeparator());

		resp.setContentType("text/html");
		PrintWriter writer = resp.getWriter();
		writer.print(info.toString());
		writer.close();
	}
}
