package org.jboss.intersmash.applications.wildfly.timer.expiration;

import jakarta.ws.rs.ext.ParamConverter;
import jakarta.ws.rs.ext.Provider;
import java.time.Instant;
import java.time.format.DateTimeParseException;

/**
 * A {@link ParamConverter} implementation that provides the logic to convert {@link Instant} values to
 * {@link String}, and vice versa.
 * This is used by {@link InstantParamConverterProvider} in order to provide the Jakarta Restful Web Services
 * implementation with a component that can handle the conversion for the exposed REST APIs.
 */
@Provider
public class InstantParamConverter implements ParamConverter<Instant> {

	public Instant fromString(String value) {
		try {
			return Instant.parse(value);
		} catch (DateTimeParseException e) {
			throw new IllegalArgumentException(
					String.format("Cannot convert String \"%s\" to Instant: %s", value, e.getMessage()));
		}
	}

	public String toString(Instant value) {
		return value.toString();
	}
}
