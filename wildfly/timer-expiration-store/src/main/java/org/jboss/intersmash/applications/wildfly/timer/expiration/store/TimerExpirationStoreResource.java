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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A Jakarta RESTful Web Services resource class, that exposes the EJB Timer Expiration Store REST APIs.
 */
@Path("/timer")
public class TimerExpirationStoreResource {

	protected static final Logger log = Logger.getLogger(TimerExpirationStoreResource.class.getName());

	@EJB
	TimerExpirationStore<MonitoredTimerExpiration> timerExpirationStore;

	@GET
	@Path("/id/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTimerExpiration(@PathParam("id") Long id) {
		final MonitoredTimerExpiration timerExpiration = timerExpirationStore.getById(id);
		log.log(Level.INFO, "MonitoredTimerExpiration: {0}", timerExpiration.toString());
		return Response.ok(timerExpiration).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTimerExpirations() {
		final List<MonitoredTimerExpiration> timerExpirations = timerExpirationStore.getAll();
		log.log(Level.INFO, "List<MonitoredTimerExpiration>: {0}", timerExpirations.toArray());
		return Response.ok(timerExpirations).build();
	}

	@GET
	@Path("/range")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTimerExpirations(@QueryParam("from") Instant from, @QueryParam("to") Instant to) {
		final List<MonitoredTimerExpiration> timerExpirations = timerExpirationStore.getInTimeRange(from, to);
		log.log(Level.INFO, "List<MonitoredTimerExpiration>: {0}", timerExpirations.toArray());
		return Response.ok(timerExpirations).build();
	}

	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteTimerExpirations() {
		final int affectedTimerExpirations = timerExpirationStore.deleteAll();
		log.log(Level.INFO, "Deleted Timer Expirations: {0}", affectedTimerExpirations);
		return Response.ok(affectedTimerExpirations).build();
	}
}
