/*
 * Copyright The WildFly Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.jboss.intersmash.applications.amqp.connector;

import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

@ApplicationScoped
public class ProducingBean {
	public static final int HIGH = 64;
	private static final int TICK = 100;

	private ScheduledExecutorService delayedExecutor = Executors
			.newSingleThreadScheduledExecutor(Executors.defaultThreadFactory());
	private volatile int value = 1;
	private long last = -1;

	@Outgoing("source")
	public CompletableFuture<Integer> generate() {
		System.out.println("----> Calling generate!!!!");
		synchronized (this) {
			CompletableFuture<Integer> cf = new CompletableFuture<>();
			if (last == -1) {
				last = System.currentTimeMillis();
			}
			long next = TICK + last;
			long delay = next - last;
			last = next;
			Next nextTask = new Next(cf, value);
			System.out.println("=====> Creating Next with " + value);
			if (value < HIGH) {
				value *= 2;
			}
			delayedExecutor.schedule(nextTask, delay, TimeUnit.MILLISECONDS);
			return cf;
		}
	}

	@PreDestroy
	public void stop() {
		delayedExecutor.shutdown();
	}

	private class Next implements Runnable {
		private final CompletableFuture<Integer> cf;
		private final int value;

		public Next(CompletableFuture<Integer> cf, int value) {
			this.cf = cf;
			this.value = value;
		}

		@Override
		public void run() {
			System.out.println("---> Sending " + value);
			cf.complete(value);
		}
	}

}
