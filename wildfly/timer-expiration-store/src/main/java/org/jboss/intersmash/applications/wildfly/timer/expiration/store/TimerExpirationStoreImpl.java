package org.jboss.intersmash.applications.wildfly.timer.expiration.store;

import jakarta.ejb.Remote;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.inject.Inject;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import org.jboss.intersmash.applications.wildfly.timer.expiration.store.data.TimerExpirationDAO;
import org.jboss.intersmash.applications.wildfly.timer.expiration.store.data.TimerExpirationEntity;

@Singleton
@Startup
@Remote(TimerExpirationStore.class)
public class TimerExpirationStoreImpl implements TimerExpirationStore<MonitoredTimerExpiration> {

	@Inject
	private TimerExpirationDAO timerExpirationDAO;

	@TransactionAttribute(TransactionAttributeType.MANDATORY)
	@Override
	public MonitoredTimerExpiration create(MonitoredTimerExpiration monitoredTimerExpiration) {
		return new TimerExpiration(
				timerExpirationDAO.create(new TimerExpirationEntity(monitoredTimerExpiration)));
	}

	@Override
	public List<MonitoredTimerExpiration> getAll() {
		return timerExpirationDAO.getAll().stream().map(TimerExpiration::new).collect(Collectors.toList());
	}

	@Override
	public MonitoredTimerExpiration getById(Long id) {
		return new TimerExpiration(timerExpirationDAO.getById(id));
	}

	@Override
	public List<MonitoredTimerExpiration> getInTimeRange(Instant from, Instant to) {
		return timerExpirationDAO.getInTimeRange(from, to).stream().map(TimerExpiration::new)
				.collect(Collectors.toList());
	}

	@Override
	public int deleteAll() {
		return timerExpirationDAO.deleteAll();
	}
}
