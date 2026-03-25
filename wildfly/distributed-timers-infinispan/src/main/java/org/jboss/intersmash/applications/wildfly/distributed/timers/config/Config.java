package org.jboss.intersmash.applications.wildfly.distributed.timers.config;

/**
 * Exposes the app configuration
 */
public class Config {

	private static final String TIMER_EXPIRATION_API_BASE_URL_PROPERTY = "timer.expiration.api.base.url";
	private static final String TIMER_EXPIRATION_API_BASE_URL_ENV = "TIMER_EXPIRATION_API_BASE_URL";
	private static final String TIMER_EXPIRATION_API_BASE_URL_DEFAULT = "http://localhost:8080";

	private static final String RECURRING_TIMER_EXECUTION_INITIAL_DELAY_PROPERTY = "recurring.timer.execution.initial.delay";
	private static final String RECURRING_TIMER_EXECUTION_INITIAL_DELAY_ENV = "RECURRING_TIMER_EXECUTION_INITIAL_DELAY";
	private static final Long RECURRING_TIMER_EXECUTION_INITIAL_DELAY_DEFAULT = 10000L;

	private static final String RECURRING_TIMER_EXPIRATION_INTERVAL_PROPERTY = "recurring.timer.expiration.interval";
	private static final String RECURRING_TIMER_EXPIRATION_INTERVAL_ENV = "RECURRING_TIMER_EXPIRATION_INTERVAL";
	private static final Long RECURRING_TIMER_EXPIRATION_INTERVAL_DEFAULT = 1000L;

	/**
	 * Looks up the configured value for the timer expiration APIs URL.
	 *
	 * If the {@link Config#TIMER_EXPIRATION_API_BASE_URL_PROPERTY} system property has been set, it will take precedence
	 * over the {@link Config#TIMER_EXPIRATION_API_BASE_URL_ENV} environment variable.
	 * Will default to {@link Config#TIMER_EXPIRATION_API_BASE_URL_DEFAULT} if none of the above has been set.
	 *
	 * @return A string representing the configured value for the timer expiration APIs URL
	 */
	public static String getTimerExpirationApisBaseUrl() {
		final String propertyValue = System.getProperty(TIMER_EXPIRATION_API_BASE_URL_PROPERTY);
		final String configValue = propertyValue == null ? System.getenv(TIMER_EXPIRATION_API_BASE_URL_ENV) : propertyValue;
		// can be just _null_, i.e. not empty or blank
		if (configValue == null) {
			return TIMER_EXPIRATION_API_BASE_URL_DEFAULT;
		}
		return configValue;
	}

	/**
	 * Looks up the configured value for the recurring timer execution initial delay.
	 *
	 * If the {@link Config#RECURRING_TIMER_EXECUTION_INITIAL_DELAY_PROPERTY} system property has been set,
	 * it will take precedence over the {@link Config#RECURRING_TIMER_EXECUTION_INITIAL_DELAY_ENV} environment variable.
	 * Will default to {@link Config#RECURRING_TIMER_EXECUTION_INITIAL_DELAY_DEFAULT} if none of the above has been set.
	 *
	 * @return A Long representing the configured value for the recurring timer execution initial delay
	 */
	public static Long getRecurringTimerExecutionInitialDelay() {
		final String propertyValue = System.getProperty(RECURRING_TIMER_EXECUTION_INITIAL_DELAY_PROPERTY);
		final String envValue = System.getenv(RECURRING_TIMER_EXECUTION_INITIAL_DELAY_ENV);
		final Long actualValue = propertyValue == null
				? envValue == null ? RECURRING_TIMER_EXECUTION_INITIAL_DELAY_DEFAULT : Long.valueOf(envValue)
				: Long.valueOf(propertyValue);
		return actualValue;
	}

	/**
	 * Looks up the configured value for the recurring timer expiration interval.
	 *
	 * If the {@link Config#RECURRING_TIMER_EXPIRATION_INTERVAL_PROPERTY} system property has been set,
	 * it will take precedence over the {@link Config#RECURRING_TIMER_EXPIRATION_INTERVAL_ENV} environment variable.
	 * Will default to {@link Config#RECURRING_TIMER_EXPIRATION_INTERVAL_DEFAULT} if none of the above has been set.
	 *
	 * @return A Long representing the configured value for the recurring timer expiration interval.
	 */
	public static Long getRecurringTimerExpirationInterval() {
		final String propertyValue = System.getProperty(RECURRING_TIMER_EXPIRATION_INTERVAL_PROPERTY);
		final String envValue = System.getenv(RECURRING_TIMER_EXPIRATION_INTERVAL_ENV);
		final Long actualValue = propertyValue == null
				? envValue == null ? RECURRING_TIMER_EXPIRATION_INTERVAL_DEFAULT : Long.valueOf(envValue)
				: Long.valueOf(propertyValue);
		return actualValue;
	}
}
