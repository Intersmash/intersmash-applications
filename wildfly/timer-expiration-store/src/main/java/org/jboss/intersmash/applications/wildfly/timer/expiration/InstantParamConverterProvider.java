package org.jboss.intersmash.applications.wildfly.timer.expiration;

import jakarta.ws.rs.ext.ParamConverter;
import jakarta.ws.rs.ext.ParamConverterProvider;
import jakarta.ws.rs.ext.Provider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.time.Instant;

/**
 * Provider that the Jakarta Restful Web Services implementation will use in order to obtain an
 * {@link InstantParamConverter} instance.
 */
@Provider
public class InstantParamConverterProvider implements ParamConverterProvider {
	@SuppressWarnings("unchecked")
	@Override
	public <T> ParamConverter<T> getConverter(Class<T> rawType,
			Type genericType,
			Annotation[] annotations) {
		if (rawType.isAssignableFrom(Instant.class)) {
			return (ParamConverter<T>) new InstantParamConverter();
		}
		return null;
	}
}
