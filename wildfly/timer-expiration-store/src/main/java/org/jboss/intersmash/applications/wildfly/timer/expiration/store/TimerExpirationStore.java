package org.jboss.intersmash.applications.wildfly.timer.expiration.store;

import jakarta.ejb.Remote;
import java.time.Instant;
import java.util.List;

/**
 * Defines the contract to implement a remote timer expiration store service.
 * Concrete implementation can provide different means for storing the data.
 * <<p>
 *     The service uses the {@link MonitoredTimerExpiration} interface to represent timer expirations
 *     and provides methods to create, retrieve and delete timer expirations.
 * </p>
 */
@Remote
public interface TimerExpirationStore<T extends MonitoredTimerExpiration> {

	/**
	 * Stores a new timer expiration.
	 * @param monitoredTimerExpiration A {@link MonitoredTimerExpiration} instance, representing the timer expiration to
	 *                                 be stored.
	 * @return A {@link MonitoredTimerExpiration} instance, representing the actual timer expiration, after it has been
	 * stored. E.g.: for a JPA based store, the ID could be generated automatically.
	 */
	T create(T monitoredTimerExpiration);

	/**
	 * Retrieves all the timer expirations.
	 * @return A collection of {@link MonitoredTimerExpiration}, representing all the stored timer expirations.
	 */
	List<T> getAll();

	/**
	 * Retrieves a certain timer expiration, identified by its {@link MonitoredTimerExpiration#getId()}.
	 * @param id The timer expiration identifier.
	 * @return A {@link MonitoredTimerExpiration} instance.
	 */
	T getById(Long id);

	/**
	 * Retrieves all the timer expirations in a time range.
	 * @param from The {@link Instant} representing the search range lower limit
	 * @param to The {@link Instant} representing the search range upper limit
	 * @return A collection of {@link MonitoredTimerExpiration}, representing all the stored timer expirations in
	 * the desired time range.
	 */
	List<T> getInTimeRange(Instant from, Instant to);

	/**
	 * Deletes all the timer expirations.
	 * @return The number of deleted timer expirations.
	 */
	int deleteAll();
}
