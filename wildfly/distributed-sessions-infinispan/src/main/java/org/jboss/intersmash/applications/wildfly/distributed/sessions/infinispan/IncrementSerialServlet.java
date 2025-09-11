package org.jboss.intersmash.applications.wildfly.distributed.sessions.infinispan;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jboss.intersmash.applications.wildfly.distributed.sessions.infinispan.utils.Constants;

/**
 * <p>Returns a serial value; the serial value is then incremented and stored as session data; this way, next time this
 * endpoint is invoked for the same session, from any cluster node, it is expected to return the incremented value;
 * basically if you invoked this endpoint and got value N back, next time you invoke it, you expect to get back N + 1</p>
 * <p>Example:
 * <ul>
 *     <li>"/serial" is invoked for the first time: a new session is created, the value 0 is returned to the client
 *     while the value 1 is stored as session data</li>
 *     <li>"/serial" is invoked a second time for the same session: the value 1 is retrieved from the session and
 *     returned to the client while the value 2 is stored as session data</li>
 * </ul>
 * </p>
 */
@SuppressWarnings("serial")
@WebServlet("/serial")
public class IncrementSerialServlet extends HttpServlet {
	protected static final Logger log = Logger.getLogger(IncrementSerialServlet.class.getName());
	public static final String KEY = IncrementSerialServlet.class.getName();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		HttpSession session = req.getSession(true);
		session.setMaxInactiveInterval(1800); // Sets timeout to 30 minutes
		if (session.isNew())
			log.log(Level.INFO, "New session created: {0}", session.getId());
		int serial = (session.isNew() || session.getAttribute(KEY) == null) ? 0 : (Integer) session.getAttribute(KEY);

		session.setAttribute(KEY, serial + 1);
		resp.getWriter().print(serial);

		// Invalidate?
		if (req.getParameter(Constants.INVALIDATE) != null) {
			log.log(Level.INFO, "Invalidating: {0}", session.getId());
			session.invalidate();
		}
	}
}
