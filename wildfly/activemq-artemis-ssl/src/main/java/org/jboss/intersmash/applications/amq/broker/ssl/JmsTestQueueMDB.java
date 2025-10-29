/**
 * Copyright (C) 2025 Red Hat, Inc.
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
package org.jboss.intersmash.applications.amq.broker.ssl;

import jakarta.annotation.Resource;
import jakarta.ejb.ActivationConfigProperty;
import jakarta.ejb.MessageDriven;
import jakarta.inject.Inject;
import jakarta.jms.JMSContext;
import jakarta.jms.JMSException;
import jakarta.jms.JMSProducer;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.Queue;
import jakarta.jms.TextMessage;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Test message-driven bean that consumes text messages from the inQueue.
 * <p>
 * This MDB listens to the inQueue, processes incoming messages by simulating business logic,
 * and sends reply messages to the outQueue. It also includes a test mechanism to kill the
 * server after processing 100 messages to test failure scenarios.
 * </p>
 * <p>
 * The bean uses auto-acknowledge mode and is configured to consume messages from the
 * {@code queue/inQueue} destination.
 * </p>
 */
@MessageDriven(name = "testQueueMDB", activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "queue/" + JmsTestConstants.IN_QUEUE),
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "jakarta.jms.Queue"),
		@ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge")
})
public class JmsTestQueueMDB implements MessageListener {

	/**
	 * Counter tracking the total number of messages processed by this MDB.
	 * This counter is used to determine when to trigger server shutdown (every 100th message).
	 */
	public static AtomicInteger numberOfProcessedMessages = new AtomicInteger();

	/**
	 * Standard reply message text sent to the outQueue for each processed message.
	 */
	public static final String QUEUE_MDB_TEXT_REPLY_MESSAGE = "Hello MDB - reply message!";

	/**
	 * Logger instance for this message-driven bean.
	 */
	private static final Logger LOGGER = Logger.getLogger(JmsTestQueueMDB.class.toString());

	/**
	 * JMS context used for creating messages and producers.
	 */
	@Inject()
	private JMSContext context;

	/**
	 * Output queue where reply messages are sent after processing incoming messages.
	 */
	@Resource(lookup = "java:/queue/" + JmsTestConstants.OUT_QUEUE)
	private Queue outQueue;

	/**
	 * Processes incoming JMS messages from the inQueue.
	 * <p>
	 * This method receives messages, logs the reception, simulates business logic processing,
	 * creates a reply message, and sends it to the outQueue. The reply message includes
	 * the original message ID as a property.
	 * </p>
	 * <p>
	 * Every 100th message triggers a server shutdown (using {@code Runtime.halt(1)})
	 * to simulate failure scenarios for testing purposes.
	 * </p>
	 *
	 * @param rcvMessage the incoming JMS message to process
	 * @throws RuntimeException if message processing fails due to JMS exceptions
	 * @see MessageListener#onMessage(Message)
	 */
	public void onMessage(Message rcvMessage) {
		LOGGER.info("MDB: message received from " + JmsTestConstants.IN_QUEUE);
		TextMessage message;
		try {
			int processedMessages = numberOfProcessedMessages.incrementAndGet();
			if (rcvMessage instanceof TextMessage) {
				message = (TextMessage) rcvMessage;
				LOGGER.info("Received " + processedMessages + " Message from queue: "
						+ message.getText() + " details: " + message);
			} else {
				LOGGER.warning("Message of wrong type: " + processedMessages + " for message: "
						+ processedMessages + " details: " + rcvMessage);
			}

			simulateBusinessLogic();

			Message newMessage = context.createTextMessage(QUEUE_MDB_TEXT_REPLY_MESSAGE);
			newMessage.setStringProperty("inMessageId", rcvMessage.getJMSMessageID());
			JMSProducer producer = context.createProducer();
			producer.send(outQueue, newMessage);

			if (processedMessages % 100 == 0) {
				LOGGER.info("100th message received - killing server");
				// do not use System.exit() as it calls all shutdowns hooks and finalizers, this will kill instantly
				Runtime.getRuntime().halt(1);
			}
		} catch (JMSException e) {
			LOGGER.log(Level.SEVERE, "MDB failed to process message.", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Simulates business logic processing by introducing random delays.
	 * <p>
	 * This method intentionally slows down message processing by sleeping for a random
	 * duration between 50-150ms to simulate real-world business logic execution time.
	 * </p>
	 */
	private void simulateBusinessLogic() {
		for (int i = 0; i < (5 + 5 * Math.random()); i++) {
			try {
				Thread.sleep((int) (10 + 10 * Math.random()));
			} catch (InterruptedException ex) {
			}
		}
	}
}
