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

/**
 * JAX-RS resource that exposes a REST API for managing distributed EJB interval timers.
 *
 * <p>Timers are identified by an {@code applicationInfo} string. When no identifier is supplied,
 * the fully-qualified class name of this endpoint is used as the default.
 *
 * <p>Timer state is persisted to a remote Infinispan server (via the {@code hotrod-persistent}
 * cache configured in {@code remote-infinispan.cli}), so timers survive application restarts
 * and are visible across a WildFly cluster. Each timer execution is recorded by the remote
 * {@code timer-expiration-store} service.
 *
 * @see TransactionalRecurringTimerService
 */
@Path("/timer")
public class TimerManagementEndpoint {
	private static final String RECURRING_TIMER_APPLICATION_INFO = TimerManagementEndpoint.class.getName();

	@EJB
	private TransactionalRecurringTimerService transactionalRecurringTimerService;

	/**
	 * Creates a recurring interval timer.
	 *
	 * <p>The timer will first fire after {@code initialDelay} milliseconds and then repeat every
	 * {@code expirationInterval} milliseconds. Both values fall back to the application
	 * configuration (see {@link Config}) when not provided.
	 *
	 * @param initialDelay       milliseconds before the timer first fires (default: 10 000)
	 * @param expirationInterval milliseconds between subsequent executions (default: 1 000)
	 * @param applicationInfo    identifier used to look up or cancel the timer later;
	 *                           defaults to this class's fully-qualified name
	 * @return {@code 200 OK} on success
	 */
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

	/**
	 * Looks up an active timer by its {@code applicationInfo} identifier.
	 *
	 * @param applicationInfo the identifier that was assigned when the timer was created
	 * @return {@code 200 OK} with a message containing the timer info if found,
	 *         or {@code 404 Not Found} if no active timer matches
	 */
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

	/**
	 * Cancels an active timer identified by its {@code applicationInfo}.
	 *
	 * @param applicationInfo the identifier of the timer to cancel
	 * @return {@code 200 OK} with a confirmation message if the timer was found and cancelled,
	 *         or {@code 404 Not Found} if no active timer matches
	 */
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
