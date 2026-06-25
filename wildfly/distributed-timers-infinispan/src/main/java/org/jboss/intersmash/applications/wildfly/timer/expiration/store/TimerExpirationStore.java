package org.jboss.intersmash.applications.wildfly.timer.expiration.store;

import jakarta.ejb.Remote;
import java.time.Instant;
import java.util.List;

@Remote
public interface TimerExpirationStore {
	MonitoredTimerExpiration create(MonitoredTimerExpiration monitoredTimerExpiration);

	List<MonitoredTimerExpiration> getAll();

	MonitoredTimerExpiration getById(Long id);

	List<MonitoredTimerExpiration> getInTimeRange(Instant from, Instant to);

	int deleteAll();
}
