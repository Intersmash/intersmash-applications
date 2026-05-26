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
package org.jboss.intersmash.applications.wildfly.kafka.sasl;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("")
public class ReactiveMessagingWithSaslEndpoint {
	@Inject
	ReactiveMessagingWithSaslBean bean;

	@POST
	@Path("{value}")
	@Consumes(MediaType.TEXT_PLAIN)
	public Response send(@PathParam("value") String value) {
		bean.send(value);
		return Response.ok().build();
	}

	@GET
	@Produces(APPLICATION_JSON)
	public List<String> getReceived() {
		return bean.getReceived();
	}
}
