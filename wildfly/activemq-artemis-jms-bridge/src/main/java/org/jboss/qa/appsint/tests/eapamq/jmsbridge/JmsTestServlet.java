package org.jboss.qa.appsint.tests.eapamq.jmsbridge;

import jakarta.annotation.Resource;
import jakarta.inject.Inject;
import jakarta.jms.JMSContext;
import jakarta.jms.Queue;
import jakarta.jms.TextMessage;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.PrintWriter;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Test servlet that produces message to AMQ JMS bridge
 */
@WebServlet("/jms-test")
public class JmsTestServlet extends HttpServlet {

    static final String QUEUE_SEND_RESPONSE = "Sent a text message to ";
    static final String QUEUE_TEXT_MESSAGE = "Hello Servlet!";
    static final String QUEUE_COUNT_TEMPLATE = "browsed: %d messages";
    static final String REQUEST_PRODUCE = "produce";
    static final String REQUEST_COUNT = "count";

    private static final Logger LOGGER = Logger.getLogger(JmsTestServlet.class.toString());

    @Resource(lookup = "java:/jms/queue/jmsBridgeSourceQueue")
    private Queue queue;

    @Inject()
    private JMSContext context;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        resp.setContentType("text/html");
        TextMessage textMessage;

        String request = req.getParameter("request");

        try (PrintWriter out = resp.getWriter()) {
            switch (request) {
                case REQUEST_PRODUCE:
                    // produce and send a text message to a queue
                    textMessage = context.createTextMessage(QUEUE_TEXT_MESSAGE);
                    context.createProducer().send(queue, textMessage);
                    out.println(QUEUE_SEND_RESPONSE + queue.toString());
                    break;
                case REQUEST_COUNT:
                    // counts messages in the queue
                    out.println(String.format(QUEUE_COUNT_TEMPLATE,
                            Collections.list(context.createBrowser(queue).getEnumeration()).size()));
                    break;
                default:
                    out.println("Usage: use <b>?produce</b> parameter to sent a message to test queue");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        }
    }
}
