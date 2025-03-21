/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2021, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.intersmash.applications.wildfly.microprofile.reactive.messaging.kafka.serializer;

import io.smallrye.reactive.messaging.kafka.api.KafkaMetadataUtil;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.eclipse.microprofile.reactive.streams.operators.PublisherBuilder;
import org.eclipse.microprofile.reactive.streams.operators.ReactiveStreams;

/**
 * Taken from WildFly testsuite, see
 * org.wildfly.test.integration.microprofile.reactive.messaging.kafka.serializer.Bean.
 * <p/>
 * This generates some data and sends them to an AMQ-Stream instance to
 * 'testing' topic via 'ssl-serializer-to-kafka' outgoing interface. At the same
 * time it reads from AMQ-Streams instance from 'testing' topic via
 * 'ssl-serializer-from-kafka' incoming interface (see
 * 'microprofile-config.properties' for more context).
 *
 * This test also serves as a test for the SSL connection with Kafka/AMQ
 * Streams.
 *
 * @author <a href="mailto:kabir.khan@jboss.com">Kabir Khan</a>
 */
@ApplicationScoped
public class Bean {
	private final CountDownLatch latch = new CountDownLatch(10);
	private List<Person> received = new ArrayList<>();
	private List<Integer> partitionReceived = new ArrayList<>();

	private ExecutorService executorService = Executors.newSingleThreadExecutor();

	@PreDestroy
	public void stop() {
		executorService.shutdown();
	}

	public CountDownLatch getLatch() {
		return latch;
	}

	@Outgoing("sslto")
	public PublisherBuilder<Person> source() {
		// We need to set the following in microprofile-config.properties for this
		// approach to work
		// mp.messaging.incoming.from-kafka.auto.offset.reset=earliest
		System.out.println("Sending to sslto");
		return ReactiveStreams.of(new Person("Kabir", 101), new Person("Bob", 18), new Person("Roger", 21),
				new Person("Franta", 11), new Person("Pepa", 12), new Person("Karel", 13), new Person("Jaromir", 14),
				new Person("Vita", 15), new Person("Suzie", 16), new Person("Paja", 17));
	}

	@Incoming("sslfrom")
	public CompletionStage<Void> sink(Message<Person> msg) {
		System.out.println("We received from sslfrom");
		received.add(msg.getPayload());
		partitionReceived.add(KafkaMetadataUtil.readIncomingKafkaMetadata(msg).get().getPartition());
		latch.countDown();
		return msg.ack();
	}

	public List<Person> getReceived() {
		return received;
	}

	public List<Integer> getPartitionReceived() {
		return partitionReceived;
	}
}
