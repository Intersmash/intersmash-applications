package org.jboss.intersmash.applications.wildfly.distributed.timers;

import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import jakarta.ejb.Stateless;
import jakarta.ejb.Timeout;
import jakarta.ejb.Timer;
import jakarta.ejb.TimerConfig;
import jakarta.ejb.TimerService;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Instant;
import java.util.Collection;
import java.util.Properties;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.jboss.intersmash.applications.wildfly.distributed.timers.config.Config;
import org.jboss.intersmash.applications.wildfly.distributed.timers.expiration.MonitoredTimerExpiration;
import org.jboss.intersmash.applications.wildfly.distributed.timers.expiration.TimerExpiration;
import org.jboss.intersmash.applications.wildfly.distributed.timers.expiration.TimerExpirationStore;

@Stateless
public class TransactionalRecurringTimerService {
	private static final Logger LOGGER = Logger.getLogger(TransactionalRecurringTimerService.class.getName());
	public static final String TIMER_NAME = TransactionalRecurringTimerService.class.getName();
	private static final TimerExpirationStore timerExpirationStoreProxy;
	private static final InitialContext timerExpirationStoreContext;

	static {
		try {
			timerExpirationStoreContext = new InitialContext(getCtxProperties());
			timerExpirationStoreProxy = getTimerExpirationStoreProxy(timerExpirationStoreContext);
		} catch (NamingException e) {
			throw new RuntimeException(e);
		}
	}

	@PreDestroy
	void tearTimeExpirationContextDown() {
		try {
			timerExpirationStoreContext.close();
		} catch (NamingException e) {
			throw new RuntimeException(e);
		}
	}

	@Resource
	protected TimerService timerService;

	public void createIntervalTimer(
			final Long initialDelay,
			final Long expirationInterval,
			final String applicationInfo) {
		LOGGER.info(
				String.format(
						"A recurring timer will be created with the following parameters: initialDelay=%d, expirationInterval=%d, applicationInfo=%s",
						initialDelay, expirationInterval, applicationInfo));
		this.timerService.createIntervalTimer(
				initialDelay,
				expirationInterval,
				new TimerConfig(applicationInfo, Boolean.TRUE));
		LOGGER.info("Recurring timer successfully created.");
	}

	public String cancelIntervalTimer(final String applicationInfo) {
		LOGGER.info("cancelIntervalTimer");
		String deleted = null;
		Collection<Timer> timers = timerService.getTimers();
		for (Timer timer : timers) {
			final String timerInfo = (String) timer.getInfo();
			LOGGER.info(String.format("Found timer: %s", timerInfo));
			if (applicationInfo.equals(timerInfo)) {
				LOGGER.info(
						String.format(
								"A recurring timer identified by its info (%s) will be cancelled",
								timerInfo));
				deleted = timerInfo;
				timer.cancel();
				LOGGER.info(String.format("Recurring timer %s successfully cancelled.", applicationInfo));
				break;
			}
		}
		return deleted;
	}

	public String getIntervalTimer(final String applicationInfo) {
		LOGGER.info("getIntervalTimer");
		String existing = null;
		Collection<Timer> timers = timerService.getTimers();
		for (Timer timer : timers) {
			final String timerInfo = (String) timer.getInfo();
			LOGGER.info(String.format("Found timer: %s", timerInfo));
			if (applicationInfo.equals(timerInfo)) {
				LOGGER.info(
						String.format(
								"A recurring transactional timer was identified by its info (%s)",
								timerInfo));
				existing = timerInfo;
				break;
			}
		}
		return existing;
	}

	@Timeout
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void doExecute(Timer t) {
		Instant now = Instant.now();
		LOGGER.info("Executing timer at: " + now);
		// save the timer expiration
		try {
			final String localHostId = InetAddress.getLocalHost().toString();
			// create data
			MonitoredTimerExpiration timerExpiration = new TimerExpiration(
					localHostId,
					TIMER_NAME,
					t.getInfo().toString(),
					now);
			// save the new timer expiration data
			LOGGER.info("--->>> Sending timer expiration data to service: " + timerExpiration);
			MonitoredTimerExpiration created = timerExpirationStoreProxy.createTimerExpiration(timerExpiration);
			// read the newly created timer expiration data to output some log messages
			LOGGER.info("<<<--- Got timer expiration back from service: " + created.toString());
		} catch (UnknownHostException e) {
			throw new IllegalStateException("An error occurred while trying to get the local host name", e);
		}
	}

	private static TimerExpirationStore getTimerExpirationStoreProxy(InitialContext context) throws NamingException {
		String lookupName = "ejb:/ROOT/TimerExpirationStoreImpl!org.jboss.qa.appsint.tests.deployments.ejb.timer.expiration.TimerExpirationStore";
		return (TimerExpirationStore) context.lookup(lookupName);
	}

	private static Properties getCtxProperties() {
		Properties props = new Properties();
		props.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
		props.put(Context.PROVIDER_URL, "remote+" + Config.getTimerExpirationApisBaseUrl());
		props.put(Context.SECURITY_PRINCIPAL, "user1");
		props.put(Context.SECURITY_CREDENTIALS, "password123");
		return props;
	}
}
