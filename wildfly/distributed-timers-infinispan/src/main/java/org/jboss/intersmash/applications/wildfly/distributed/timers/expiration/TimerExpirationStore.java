package org.jboss.intersmash.applications.wildfly.distributed.timers.expiration;

import jakarta.ejb.Remote;
import java.time.Instant;
import java.util.List;

@Remote
public interface TimerExpirationStore {
	MonitoredTimerExpiration createTimerExpiration(MonitoredTimerExpiration monitoredTimerExpiration);

	List<MonitoredTimerExpiration> getAll();

	MonitoredTimerExpiration getTimerExpiration(Long id);

	List<MonitoredTimerExpiration> getTimerExpirations(Instant from, Instant to);

	int deleteAll();
}
