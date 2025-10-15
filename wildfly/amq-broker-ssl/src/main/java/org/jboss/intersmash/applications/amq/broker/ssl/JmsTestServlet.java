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
import jakarta.inject.Inject;
import jakarta.jms.Connection;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSConsumer;
import jakarta.jms.JMSContext;
import jakarta.jms.JMSDestinationDefinition;
import jakarta.jms.JMSDestinationDefinitions;
import jakarta.jms.MessageProducer;
import jakarta.jms.Queue;
import jakarta.jms.Session;
import jakarta.jms.TextMessage;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Test servlet for performing JMS operations with ActiveMQ message broker.
 * <p>
 * This servlet provides a web interface to test various JMS operations including:
 * <ul>
 *   <li>Sending messages to queues</li>
 *   <li>Consuming messages from queues</li>
 *   <li>Interacting with message-driven beans (MDBs)</li>
 *   <li>Testing failure scenarios (server shutdown after processing messages)</li>
 * </ul>
 * </p>
 * <p>
 * The servlet is mapped to {@code /jms-test} and accepts a {@code request} parameter
 * that specifies which JMS operation to perform. See {@link JmsTestRequestType} for
 * available operations.
 * </p>
 * <p>
 * Three JMS queues are defined:
 * <ul>
 *   <li><b>testQueue</b> - General purpose test queue for direct message send/receive</li>
 *   <li><b>inQueue</b> - Input queue consumed by the MDB</li>
 *   <li><b>outQueue</b> - Output queue where the MDB sends reply messages</li>
 * </ul>
 * </p>
 */
@JMSDestinationDefinitions(value = {
		@JMSDestinationDefinition(name = "java:/queue/testQueue", interfaceName = "jakarta.jms.Queue", destinationName = "test-queue", properties = {
				"enable-amq1-prefix=false" }),
		@JMSDestinationDefinition(name = "java:/queue/inQueue", interfaceName = "jakarta.jms.Queue", destinationName = "in-queue", properties = {
				"enable-amq1-prefix=false" }),
		@JMSDestinationDefinition(name = "java:/queue/outQueue", interfaceName = "jakarta.jms.Queue", destinationName = "out-queue", properties = {
				"enable-amq1-prefix=false" })
})
@WebServlet("/jms-test")
public class JmsTestServlet extends HttpServlet {

	/**
	 * Logger instance for this servlet.
	 */
	private static final Logger LOGGER = Logger.getLogger(JmsTestServlet.class.toString());

	/**
	 * General purpose test queue for direct message send/receive operations.
	 */
	@Resource(lookup = "java:/queue/testQueue")
	private Queue testQueue;

	/**
	 * Input queue consumed by the message-driven bean (MDB).
	 */
	@Resource(lookup = "java:/queue/inQueue")
	private Queue inQueue;

	/**
	 * Output queue where the MDB sends reply messages after processing.
	 */
	@Resource(lookup = "java:/queue/outQueue")
	private Queue outQueue;

	/**
	 * JMS context for simplified JMS API operations (send/receive messages).
	 */
	@Inject()
	private JMSContext jmsContext;

	/**
	 * Connection factory for creating JMS connections when transactional sessions are needed.
	 */
	@Resource(lookup = "java:jboss/DefaultJMSConnectionFactory")
	private ConnectionFactory connectionFactory;

	/**
	 * Handles HTTP GET requests to perform JMS operations.
	 * <p>
	 * The method expects a {@code request} parameter that specifies which JMS operation
	 * to perform. If no parameter or an invalid parameter is provided, it returns usage
	 * instructions.
	 * </p>
	 * <p>
	 * Supported operations include:
	 * <ul>
	 *   <li>Sending messages to various queues</li>
	 *   <li>Consuming messages from queues</li>
	 *   <li>Batch sending messages for MDB processing</li>
	 *   <li>Testing failure scenarios</li>
	 * </ul>
	 * </p>
	 *
	 * @param req  the HTTP request containing the {@code request} parameter
	 * @param resp the HTTP response where results are written
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
		resp.setContentType("text/html");
		TextMessage textMessage;

		String request = req.getParameter("request");

		try (PrintWriter out = resp.getWriter()) {

			if (request == null || "".equals(request)) { // log usage and return
				logUsage(out, null);
				return;
			}

			switch (JmsTestRequestType.fromValue(request)) {
				case REQUEST_SEND:
					textMessage = jmsContext.createTextMessage(JmsTestConstants.QUEUE_TEXT_MESSAGE);
					jmsContext.createProducer().send(testQueue, textMessage);
					out.println(JmsTestConstants.QUEUE_SEND_RESPONSE + testQueue.toString());
					break;
				// produce and send a text message to testQueue
				case REQUEST_SEND_REQUEST_MESSAGE_FOR_MDB:
					textMessage = jmsContext.createTextMessage(JmsTestConstants.QUEUE_MDB_TEXT_MESSAGE);
					jmsContext.createProducer().send(inQueue, textMessage);
					out.println(JmsTestConstants.QUEUE_MDB_SEND_RESPONSE + inQueue.toString());
					break;
				// produce 180 text messages to inQueue, MDB will kill server when 100th message is consumed
				// this must done in transaction to avoid situation that MDB kill server before this call is finished
				// note that JMSContext cannot be used as Wildfly/EAP does no allow to inject it with transacted session
				case REQUEST_SEND_REQUEST_MESSAGE_FOR_MDB_AND_KILL_SERVER:
					String messageCount = req.getParameter("messageCount");
					for (String name : req.getParameterMap().keySet()) {
						System.out.println("param" + name);
					}
					System.out.println("messageCount" + messageCount);
					int messageCountToSend = isEmpty(messageCount) ? 180 : Integer.parseInt(messageCount);
					try (Connection connection = connectionFactory.createConnection()) {
						Session session = connection.createSession(true, Session.SESSION_TRANSACTED);
						MessageProducer producer = session.createProducer(inQueue);
						for (int i = 0; i < messageCountToSend; i++) {
							textMessage = session.createTextMessage(JmsTestConstants.QUEUE_MDB_TEXT_MESSAGE);
							producer.send(textMessage);
						}
						out.println(messageCountToSend + " messages were sent into queue: " + inQueue.toString());
						session.commit();
					} catch (Exception ex) {
						ex.printStackTrace();
						out.println(ex);
					}
					break;
				// consume a text message from testQueue
				case REQUEST_CONSUME_MESSAGE:
					textMessage = (TextMessage) jmsContext.createConsumer(testQueue).receive(1000);
					out.println(textMessage.getText());
					break;
				// consume a reply text message from outQueue, processed by MDB
				case REQUEST_CONSUME_REPLY_MESSAGE_FOR_MDB:
					textMessage = (TextMessage) jmsContext.createConsumer(outQueue).receive(1000);
					out.println((textMessage == null ? null : textMessage.getText()) + " message details: " + textMessage);
					break;
				// consume all reply text messages from outQueue, processed by MDB
				case REQUEST_CONSUME_ALL_REPLY_MESSAGES_FOR_MDB:
					JMSConsumer consumer = jmsContext.createConsumer(outQueue);
					int count = 0;
					while ((consumer.receive(1000)) != null) {
						count++;
					}
					out.print(count);
					break;
				// print usage
				default:
					logUsage(out, request);
			}
		} catch (Exception ex) {
			LOGGER.log(Level.SEVERE, ex.getMessage());
		}
	}

	/**
	 * Checks if a string value is null or empty.
	 *
	 * @param value the string to check
	 * @return {@code true} if the value is null or empty, {@code false} otherwise
	 */
	private boolean isEmpty(String value) {
		return value == null || "".equals(value);
	}

	/**
	 * Logs usage instructions to the HTTP response.
	 * <p>
	 * This method outputs HTML-formatted documentation showing all available request
	 * parameters and their purposes.
	 * </p>
	 *
	 * @param out     the PrintWriter to write the usage instructions to
	 * @param request the invalid request parameter that triggered this usage display, or null
	 */
	private void logUsage(PrintWriter out, String request) {
		out.println("Invalid request parameter: " + request + "<br>" +
				"Usage:<ul>" +
				"<li>use <b>?request=" + JmsTestRequestType.REQUEST_SEND.value()
				+ "</b> parameter to send a message to test queue</li>" +
				"<li>use <b>?request=" + JmsTestRequestType.REQUEST_SEND_REQUEST_MESSAGE_FOR_MDB.value()
				+ " </b> parameter to send a message " +
				"to test queue</li>" +
				"<li>use <b>?request=" + JmsTestRequestType.REQUEST_SEND_REQUEST_MESSAGE_FOR_MDB_AND_KILL_SERVER.value()
				+ " </b> parameter " +
				"to send 180 messages to test queue, MDB will kill server when 100th message is received," +
				"you can use paramter &messageCount=20 to change number of messages</li>" +
				"<li>use <b>?request=" + JmsTestRequestType.REQUEST_CONSUME_MESSAGE.value()
				+ "</b> parameter to consume a message from test queue</li>" +
				"<li>use <b>?request=" + JmsTestRequestType.REQUEST_CONSUME_REPLY_MESSAGE_FOR_MDB.value()
				+ "</b> parameter to consume a reply " +
				"message from outQueue queue which was processed by MDB</li>" +
				"<li>use <b>?request=" + JmsTestRequestType.REQUEST_CONSUME_ALL_REPLY_MESSAGES_FOR_MDB.value()
				+ "</b> parameter to consume all reply " +
				"messages from outQueue queue which were processed by MDB</li>" +
				"</ul>");
	}
}
