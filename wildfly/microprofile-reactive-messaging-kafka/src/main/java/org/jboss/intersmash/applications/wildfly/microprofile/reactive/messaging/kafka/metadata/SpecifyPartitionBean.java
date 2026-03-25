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
package org.jboss.intersmash.applications.wildfly.microprofile.reactive.messaging.kafka.metadata;

import io.smallrye.reactive.messaging.kafka.api.IncomingKafkaRecordMetadata;
import io.smallrye.reactive.messaging.kafka.api.KafkaMetadataUtil;
import io.smallrye.reactive.messaging.kafka.api.OutgoingKafkaRecordMetadata;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.CountDownLatch;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.eclipse.microprofile.reactive.streams.operators.ReactiveStreams;
import org.reactivestreams.Publisher;

/**
 * Taken from WildFly testsuite, see
 * org.wildfly.test.integration.microprofile.reactive.messaging.kafka.api.SpecifyPartitionBean.
 * <p/>
 * This generates some data and sends them to an AMQ-Stream instance to
 * 'testing' topic via 'serializer-to-kafka' outgoing interface. At the same
 * time it reads from AMQ-Streams instance from 'testing' topic via
 * 'serializer-from-kafka' incoming interface (see
 * 'microprofile-config.properties' for more context).
 *
 * @author <a href="mailto:kabir.khan@jboss.com">Kabir Khan</a>
 */
@ApplicationScoped
public class SpecifyPartitionBean {

	private final CountDownLatch latch = new CountDownLatch(40);
	private final Map<Integer, IncomingKafkaRecordMetadata<String, Integer>> noPartitionSpecifiedMetadata4 = Collections
			.synchronizedMap(new HashMap<>());
	private final Map<Integer, IncomingKafkaRecordMetadata<String, Integer>> partitionSpecifiedMetadata4 = Collections
			.synchronizedMap(new HashMap<>());
	private final Map<Integer, IncomingKafkaRecordMetadata<String, Integer>> noPartitionSpecifiedMetadata5 = Collections
			.synchronizedMap(new HashMap<>());
	private final Map<Integer, IncomingKafkaRecordMetadata<String, Integer>> partitionSpecifiedMetadata5 = Collections
			.synchronizedMap(new HashMap<>());

	public CountDownLatch getLatch() {
		return latch;
	}

	public Map<Integer, IncomingKafkaRecordMetadata<String, Integer>> getNoPartitionSpecifiedMetadata4() {
		return noPartitionSpecifiedMetadata4;
	}

	public Map<Integer, IncomingKafkaRecordMetadata<String, Integer>> getPartitionSpecifiedMetadata4() {
		return partitionSpecifiedMetadata4;
	}

	public Map<Integer, IncomingKafkaRecordMetadata<String, Integer>> getNoPartitionSpecifiedMetadata5() {
		return noPartitionSpecifiedMetadata5;
	}

	public Map<Integer, IncomingKafkaRecordMetadata<String, Integer>> getPartitionSpecifiedMetadata5() {
		return partitionSpecifiedMetadata5;
	}

	@Outgoing("invm2")
	public Publisher<Integer> source4() {
		return ReactiveStreams.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20).buildRs();
	}

	@Incoming("invm2")
	@Outgoing("to-kafka4")
	public Message<Integer> sendToKafka4(Integer i) {
		Message<Integer> msg = Message.of(i);

		OutgoingKafkaRecordMetadata.OutgoingKafkaRecordMetadataBuilder<String> mb = OutgoingKafkaRecordMetadata
				.<String> builder().withKey("KEY-" + i);

		if (i > 10) {
			mb.withPartition(1);
		}

		msg = KafkaMetadataUtil.writeOutgoingKafkaMetadata(msg, mb.build());
		return msg;
	}

	@Incoming("from-kafka4")
	public CompletionStage<Void> receiveFromKafka4(Message<Integer> msg) {
		IncomingKafkaRecordMetadata<String, Integer> metadata = KafkaMetadataUtil.readIncomingKafkaMetadata(msg).get();

		if (msg.getPayload() <= 10) {
			noPartitionSpecifiedMetadata4.put(msg.getPayload(), metadata);
		} else {
			partitionSpecifiedMetadata4.put(msg.getPayload(), metadata);
		}
		latch.countDown();
		return msg.ack();
	}

	@Outgoing("invm3")
	public Publisher<Integer> source5() {
		return ReactiveStreams.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20).buildRs();
	}

	@Incoming("invm3")
	@Outgoing("to-kafka5")
	public Message<Integer> sendToKafka5(Integer i) {
		Message<Integer> msg = Message.of(i);

		OutgoingKafkaRecordMetadata.OutgoingKafkaRecordMetadataBuilder<String> mb = OutgoingKafkaRecordMetadata
				.<String> builder().withKey("KEY-" + i);
		if (i > 10) {
			mb.withPartition(0);
		}

		msg = KafkaMetadataUtil.writeOutgoingKafkaMetadata(msg, mb.build());
		return msg;
	}

	@Incoming("from-kafka5")
	public CompletionStage<Void> receiveFromKafka5(Message<Integer> msg) {
		IncomingKafkaRecordMetadata<String, Integer> metadata = KafkaMetadataUtil.readIncomingKafkaMetadata(msg).get();

		if (msg.getPayload() <= 10) {
			noPartitionSpecifiedMetadata5.put(msg.getPayload(), metadata);
		} else {
			partitionSpecifiedMetadata5.put(msg.getPayload(), metadata);
		}
		latch.countDown();
		return msg.ack();
	}
}
