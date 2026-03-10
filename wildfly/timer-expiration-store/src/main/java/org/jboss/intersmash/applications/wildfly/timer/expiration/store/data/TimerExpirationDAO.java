package org.jboss.intersmash.applications.wildfly.timer.expiration.store.data;

import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceContextType;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import java.time.Instant;
import java.util.List;
import org.jboss.intersmash.applications.wildfly.timer.expiration.store.TimerExpirationStore;

/**
 * Implements {@link TimerExpirationStore} to provide functionality to manipulate timer expirations within a given
 * persistence context.
 */
@RequestScoped
public class TimerExpirationDAO implements TimerExpirationStore<TimerExpirationEntity> {

	@PersistenceContext(type = PersistenceContextType.EXTENDED, unitName = "primary")
	private EntityManager em;

	EntityManager getEM() {
		return em;
	}

	@Override
	public TimerExpirationEntity create(TimerExpirationEntity timerExpiration) {
		if (!getEM().contains(timerExpiration)) {
			timerExpiration = getEM().merge(timerExpiration);
		}
		getEM().persist(timerExpiration);

		return timerExpiration;
	}

	@Override
	public List<TimerExpirationEntity> getAll() {
		final TypedQuery<TimerExpirationEntity> query = querySelectAllTimerExpirations();
		return query.getResultList();
	}

	@Override
	public TimerExpirationEntity getById(Long id) {
		final TypedQuery<TimerExpirationEntity> query = querySelectTimerExpiration(id);
		return query.getSingleResult();
	}

	@Override
	public List<TimerExpirationEntity> getInTimeRange(Instant from, Instant to) {
		final TypedQuery<TimerExpirationEntity> query = querySelectTimerExpirations(from, to);
		return query.getResultList();
	}

	@Override
	public int deleteAll() {
		final Query query = queryDeleteAllTimerExpirations();
		return query.executeUpdate();
	}

	private TypedQuery<TimerExpirationEntity> querySelectAllTimerExpirations() {
		return getEM().createQuery(String.format("SELECT t FROM %s t", TimerExpirationEntity.class.getSimpleName()),
				TimerExpirationEntity.class);
	}

	private TypedQuery<TimerExpirationEntity> querySelectTimerExpiration(Long id) {
		return getEM().createQuery(
				String.format("SELECT t FROM %s t WHERE t.id = ?1", TimerExpirationEntity.class.getSimpleName()),
				TimerExpirationEntity.class)
				.setParameter(1, id);
	}

	private TypedQuery<TimerExpirationEntity> querySelectTimerExpirations(Instant from, Instant to) {
		return getEM().createQuery(
				String.format("SELECT t FROM %s t WHERE t.timestamp >= ?1 and t.timestamp <= ?2",
						TimerExpirationEntity.class.getSimpleName()),
				TimerExpirationEntity.class)
				.setParameter(1, from)
				.setParameter(2, to);
	}

	private Query queryDeleteAllTimerExpirations() {
		return getEM().createQuery(String.format("DELETE FROM %s", TimerExpirationEntity.class.getSimpleName()));
	}
}
