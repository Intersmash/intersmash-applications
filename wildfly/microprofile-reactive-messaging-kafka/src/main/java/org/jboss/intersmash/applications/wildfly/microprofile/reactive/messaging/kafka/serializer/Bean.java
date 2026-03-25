/*
 * Copyright (C) 2021 Red Hat, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
