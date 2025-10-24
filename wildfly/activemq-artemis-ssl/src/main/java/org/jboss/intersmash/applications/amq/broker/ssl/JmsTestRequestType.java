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

/**
 * Enumeration of request types supported by the JMS test servlet.
 * <p>
 * Each enum constant represents a specific JMS operation that can be invoked
 * via HTTP request parameters to test various message broker interactions.
 * </p>
 */
public enum JmsTestRequestType {
	/**
	 * Request to send a test message to the testQueue.
	 */
	REQUEST_SEND("send-message"),

	/**
	 * Request to send a message to the inQueue to be processed by the MDB.
	 */
	REQUEST_SEND_REQUEST_MESSAGE_FOR_MDB("send-request-message-for-mdb"),

	/**
	 * Request to send multiple messages (default 180) to the inQueue.
	 * The MDB will kill the server after processing the 100th message to test failure scenarios.
	 */
	REQUEST_SEND_REQUEST_MESSAGE_FOR_MDB_AND_KILL_SERVER("send-request-message-for-mdb-and-kill-server"),

	/**
	 * Request to consume a message from the testQueue.
	 */
	REQUEST_CONSUME_MESSAGE("consume-message"),

	/**
	 * Request to consume a single reply message from the outQueue that was processed by the MDB.
	 */
	REQUEST_CONSUME_REPLY_MESSAGE_FOR_MDB("consume-reply-message-for-mdb"),

	/**
	 * Request to consume all reply messages from the outQueue that were processed by the MDB.
	 */
	REQUEST_CONSUME_ALL_REPLY_MESSAGES_FOR_MDB("consume-all-reply-messages-for-mdb");

	/**
	 * The string value representing this request type, used in HTTP request parameters.
	 */
	private final String value;

	/**
	 * Returns the string value of this request type.
	 *
	 * @return the string representation of this request type
	 */
	public String value() {
		return value;
	}

	/**
	 * Constructs a JmsTestRequestType with the specified string value.
	 *
	 * @param value the string value for this request type
	 */
	JmsTestRequestType(String value) {
		this.value = value;
	}

	/**
	 * Converts a string value to its corresponding JmsTestRequestType enum constant.
	 *
	 * @param value the string value to convert
	 * @return the matching JmsTestRequestType enum constant
	 * @throws IllegalArgumentException if the value doesn't match any enum constant
	 */
	public static JmsTestRequestType fromValue(String value) {
		for (JmsTestRequestType e : values()) {
			if (e.value.equals(value)) {
				return e;
			}
		}
		throw new IllegalArgumentException(
				String.format("Unsupported value for %s: %s", JmsTestRequestType.class.getSimpleName(), value));
	}
}
