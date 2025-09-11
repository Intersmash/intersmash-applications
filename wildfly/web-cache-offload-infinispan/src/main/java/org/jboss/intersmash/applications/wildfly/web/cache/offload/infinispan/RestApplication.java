package org.jboss.intersmash.applications.wildfly.web.cache.offload.infinispan;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

/**
 * A Jakarta RESTful Web Services application which defines a resource that exposes endpoints to
 * store, delete and get a new value to/from a request {@code HttpSession} instance.
 */
@ApplicationPath("/")
public class RestApplication extends Application {

	@Path("/")
	public static class WebResource {
		private static final Logger log = Logger.getLogger(WebResource.class.getName());

		public static final String KEY = WebResource.class.getName();

		@PUT
		@Produces(MediaType.APPLICATION_JSON)
		public Response doPUT(@Context HttpServletRequest req, @QueryParam("value") String value) {
			HttpSession session = req.getSession(true);
			if (session.isNew()) {
				log.info("New session created: {0} with session data: {1}", new Object[] { session.getId(), value });
				session.setAttribute(KEY, value);
				return Response.status(Response.Status.OK)
						.entity(String.format("{\"added\":\"%s\"}", value))
						.build();
			} else {
				log.info("Session {0} is not new, setting data: {1}", new Object[] { session.getId(), value });
				session.setAttribute(KEY, value);
				return Response.status(Response.Status.OK)
						.entity(String.format("{\"added\":\"%s\"}", value))
						.build();
			}
		}

		@DELETE
		@Produces(MediaType.APPLICATION_JSON)
		public Response doDEL(@Context HttpServletRequest req) {
			HttpSession session = req.getSession(false);

			if (session != null) {
				session.invalidate();
				log.info("Session: " + session.getId() + " has been invalidated");
				return Response.status(Response.Status.OK)
						.entity(String.format("{\"deleted\":\"%s\"}", session.getId()))
						.build();
			} else {
				log.info("No session to invalidate");
				return Response.status(Response.Status.OK)
						.entity("{\"deleted\":\"null\"}")
						.build();
			}
		}

		@GET
		@Consumes(MediaType.APPLICATION_JSON)
		@Produces(MediaType.APPLICATION_JSON)
		public Response doGETJson(@Context HttpServletRequest req) {
			HttpSession session = req.getSession(false);

			if (session != null) {
				log.info("Session {0} data: {1}", new Object[] { session.getId(), session.getAttribute(KEY) });
				return Response.status(Response.Status.OK)
						.entity(String.format("{\"value\":\"%s\"}", session.getAttribute(KEY)))
						.build();
			} else {
				log.info("Session does not exists");
				return Response.status(Response.Status.OK)
						.entity("{}")
						.build();
			}
		}

		@GET
		@Consumes(MediaType.TEXT_PLAIN)
		@Produces(MediaType.TEXT_PLAIN)
		public Response doGETPlain(@Context HttpServletRequest req) {
			HttpSession session = req.getSession(false);

			if (session != null) {
				log.info("Session {0} data: {1}", new Object[] { session.getId(), session.getAttribute(KEY) });
				return Response.status(Response.Status.OK)
						.entity(session.getAttribute(KEY))
						.build();
			} else {
				log.info("Session does not exists");
				return Response.status(Response.Status.OK)
						.entity("Session does not exists")
						.build();
			}
		}
	}
}
