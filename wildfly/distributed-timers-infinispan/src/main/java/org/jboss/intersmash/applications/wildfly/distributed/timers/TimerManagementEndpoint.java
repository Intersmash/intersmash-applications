package org.jboss.intersmash.applications.wildfly.distributed.timers;

import jakarta.ejb.EJB;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.intersmash.applications.wildfly.distributed.timers.config.Config;

@Path("/timer")
public class TimerManagementEndpoint {
	private static final String RECURRING_TIMER_APPLICATION_INFO = TimerManagementEndpoint.class.getName();

	@EJB
	private TransactionalRecurringTimerService transactionalRecurringTimerService;

	@GET
	@Path("/custom-interval")
	@Produces(MediaType.APPLICATION_JSON)
	public Response createIntervalTimer(
			@QueryParam("initialDelay") Long initialDelay,
			@QueryParam("expirationInterval") Long expirationInterval,
			@QueryParam("applicationInfo") String applicationInfo) {
		transactionalRecurringTimerService.createIntervalTimer(
				initialDelay == null ? Config.getRecurringTimerExecutionInitialDelay() : initialDelay,
				expirationInterval == null ? Config.getRecurringTimerExpirationInterval() : expirationInterval,
				applicationInfo == null ? RECURRING_TIMER_APPLICATION_INFO : applicationInfo);
		return Response.ok().build();
	}

	@GET
	@Path("/custom-interval/{applicationInfo}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getIntervalTimer(@PathParam("applicationInfo") String applicationInfo) {
		final String existing = transactionalRecurringTimerService.getIntervalTimer(applicationInfo);
		if (existing != null) {
			return Response
					.ok()
					.entity(String.format("%d - Got timer [applicationInfo=%s]", Response.Status.OK.getStatusCode(),
							existing))
					.type(MediaType.TEXT_PLAIN)
					.build();
		}
		return Response
				.status(Response.Status.NOT_FOUND)
				.entity(String.format("%d - Timer [applicationInfo=%s] not found", Response.Status.NOT_FOUND.getStatusCode(),
						applicationInfo))
				.type(MediaType.TEXT_PLAIN)
				.build();
	}

	@DELETE
	@Path("/custom-interval/{applicationInfo}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteTransactionalIntervalTimer(@PathParam("applicationInfo") String applicationInfo) {
		final String deleted = transactionalRecurringTimerService.cancelIntervalTimer(applicationInfo);
		if (deleted != null) {
			return Response
					.ok()
					.entity(String.format("%d - Timer [applicationInfo=%s] deleted", Response.Status.OK.getStatusCode(),
							deleted))
					.type(MediaType.TEXT_PLAIN)
					.build();
		}
		return Response
				.status(Response.Status.NOT_FOUND)
				.entity(String.format("%d - Timer [applicationInfo=%s] not found", Response.Status.NOT_FOUND.getStatusCode(),
						applicationInfo))
				.type(MediaType.TEXT_PLAIN)
				.build();
	}
}
