package org.jboss.intersmash.applications.wildfly.timer.expiration.store;

import java.io.Serializable;
import java.time.Instant;

/**
 * Defines the contract to implement timer expirations that can be monitored.
 */
public interface MonitoredTimerExpiration extends Serializable {
	/**
	 * The timer expiration identifier.
	 * @return A {@link Long} representing the timer expiration identifier.
	 */
	Long getId();

	/**
	 * Sets the timer expiration identifier.
	 * @param id A {@link Long} representing the timer expiration identifier.
	 */
	void setId(Long id);

	/**
	 * The timer executor.
	 * @return A {@link String} representing the timer executor, typically a host name or IP.
	 */
	String getExecutor();

	/**
	 * Sets the timer executor.
	 * @param executor A {@link String} representing the timer executor, typically a host name or IP.
	 */
	void setExecutor(String executor);

	/**
	 * The timer name.
	 * @return A {@link String} representing the name of the monitored timer.
	 */
	String getName();

	/**
	 * Sets the timer name.
	 * @param name A {@link String} representing the name of the monitored timer.
	 */
	void setName(String name);

	/**
	 * The timer detailed information.
	 * @return A {@link String} containing information about the monitored timer.
	 */
	String getInfo();

	/**
	 * Sets the timer detailed information.
	 * @param info A {@link String} containing information about the monitored timer.
	 */
	void setInfo(String info);

	/**
	 * The timer expiration timestamp.
	 * @return A {@link Instant} storing the instant in which the timer expired.
	 */
	Instant getTimestamp();

	/**
	 * Sets the timer expiration timestamp.
	 * @param timestamp A {@link Instant} storing the instant in which the timer expired.
	 */
	void setTimestamp(Instant timestamp);
}
