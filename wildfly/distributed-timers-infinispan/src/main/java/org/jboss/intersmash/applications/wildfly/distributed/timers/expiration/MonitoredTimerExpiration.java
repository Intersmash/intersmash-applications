package org.jboss.intersmash.applications.wildfly.distributed.timers.expiration;

import java.io.Serializable;
import java.time.Instant;

public interface MonitoredTimerExpiration extends Serializable {
	Long getId();

	void setId(Long id);

	String getExecutor();

	void setExecutor(String executor);

	String getName();

	void setName(String name);

	String getInfo();

	void setInfo(String info);

	Instant getTimestamp();

	void setTimestamp(Instant timestamp);
}
