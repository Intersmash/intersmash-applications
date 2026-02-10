package org.jboss.intersmash.applications.wildfly.timer.expiration.store.data;

import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.time.Instant;
import java.util.Objects;
import org.jboss.intersmash.applications.wildfly.timer.expiration.store.MonitoredTimerExpiration;

/**
 * A JPA based implementation of {@link MonitoredTimerExpiration}, representing a timer expiration persisted to
 * a JPA storage.
 */
@Entity
public class TimerExpirationEntity implements MonitoredTimerExpiration {

	private static final long serialVersionUID = 4410988609879455828L;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;
	private String executor;
	private String name;
	private String info;
	private Instant timestamp;

	public TimerExpirationEntity() {
		this(null, null, null, null);
	}

	public TimerExpirationEntity(MonitoredTimerExpiration monitoredTimerExpiration) {
		this(monitoredTimerExpiration.getExecutor(), monitoredTimerExpiration.getName(), monitoredTimerExpiration.getInfo(),
				monitoredTimerExpiration.getTimestamp());
	}

	public TimerExpirationEntity(String executor, String name, String info, Instant timestamp) {
		super();
		this.executor = executor;
		this.name = name;
		this.info = info;
		this.timestamp = timestamp;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String getExecutor() {
		return executor;
	}

	@Override
	public void setExecutor(String executor) {
		this.executor = executor;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getInfo() {
		return info;
	}

	@Override
	public void setInfo(String info) {
		this.info = info;
	}

	@Override
	public Instant getTimestamp() {
		return timestamp;
	}

	@Override
	public void setTimestamp(Instant timestamp) {
		this.timestamp = timestamp;
	}

	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		TimerExpirationEntity that = (TimerExpirationEntity) o;
		return executor.equals(that.executor) && name.equals(that.name) && info.equals(that.info)
				&& timestamp.equals(that.timestamp);
	}

	public int hashCode() {
		return Objects.hash(executor, name, info, timestamp);
	}

	public String toString() {
		return "TimerExpirationEntity{" +
				"id=" + id +
				", executor='" + executor + '\'' +
				", name='" + name + '\'' +
				", info='" + info + '\'' +
				", timestamp=" + timestamp +
				'}';
	}
}
