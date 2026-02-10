package org.jboss.intersmash.applications.wildfly.timer.expiration.store;

import jakarta.ejb.EJB;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.time.Instant;
import java.util.List;

/**
 * A Jakarta RESTful Web Services resource class, that exposes the EJB Timer Expiration Store REST APIs.
 */
@Path("/timer")
public class TimerExpirationStoreResource {

	@EJB
	TimerExpirationStore<MonitoredTimerExpiration> timerExpirationStore;

	@GET
	@Path("/id/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTimerExpiration(@PathParam("id") Long id) {
		final MonitoredTimerExpiration timerExpiration = timerExpirationStore.getById(id);
		return Response.ok(timerExpiration).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTimerExpirations() {
		final List<MonitoredTimerExpiration> timerExpirations = timerExpirationStore.getAll();
		return Response.ok(timerExpirations).build();
	}

	@GET
	@Path("/range")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTimerExpirations(@QueryParam("from") Instant from, @QueryParam("to") Instant to) {
		final List<MonitoredTimerExpiration> timerExpirations = timerExpirationStore.getInTimeRange(from, to);
		return Response.ok(timerExpirations).build();
	}

	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteTimerExpirations() {
		final int affectedTimerExpirations = timerExpirationStore.deleteAll();
		return Response.ok(affectedTimerExpirations).build();
	}
}
