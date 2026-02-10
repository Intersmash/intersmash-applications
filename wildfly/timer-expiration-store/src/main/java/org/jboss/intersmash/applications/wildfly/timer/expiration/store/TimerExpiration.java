package org.jboss.intersmash.applications.wildfly.timer.expiration.store;

import java.time.Instant;
import java.util.Objects;

/**
 * A POJO providing a basic implementation of {@link MonitoredTimerExpiration}.
 */
public class TimerExpiration implements MonitoredTimerExpiration {

	private static final long serialVersionUID = -6807753176066577450L;

	private Long id = null;
	private String executor;
	private String name;
	private String info;
	private Instant timestamp;

	public TimerExpiration() {
		this(null, null, null, null);
	}

	public TimerExpiration(MonitoredTimerExpiration monitoredTimerExpiration) {
		this(monitoredTimerExpiration.getExecutor(), monitoredTimerExpiration.getName(), monitoredTimerExpiration.getInfo(),
				monitoredTimerExpiration.getTimestamp());
	}

	public TimerExpiration(String executor, String name, String info, Instant timestamp) {
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

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		TimerExpiration that = (TimerExpiration) o;
		return executor.equals(that.executor) && name.equals(that.name) && info.equals(that.info)
				&& timestamp.equals(that.timestamp);
	}

	@Override
	public int hashCode() {
		return Objects.hash(executor, name, info, timestamp);
	}

	@Override
	public String toString() {
		return "TimerExpiration{" +
				"id=" + id +
				", executor='" + executor + '\'' +
				", name='" + name + '\'' +
				", info='" + info + '\'' +
				", timestamp=" + timestamp +
				'}';
	}
}
