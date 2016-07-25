package net.wapwag.authn.ui;

import java.io.IOException;
import java.util.function.Consumer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.wapwag.authn.AuthenticationService;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * Definition of a servlet. Use the following annotations so that
 * OPS4J PAX Web Whiteboard Extended can hook it up into the Jetty server:
 * 
 *   @Component(service=HttpServlet.class, property={ "httpContext.id=<http-context" })
 *   @WebServlet(urlPatterns=<path>, name=<name>)
 *   
 * Other SCR annotations can be used to configure injection
 * 
 */
@WebServlet(urlPatterns = "/register", name = "RegisterServlet")
public class RegisterServlet extends HttpServlet {
	/** LOG */
	private static final Logger logger = LoggerFactory
			.getLogger(RegisterServlet.class);

	/*@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		//req.getRequestDispatcher("rest/user/{userId}/public").forward(req, resp);
		req.getRequestDispatcher("rest/user/{userId}").forward(req, resp);
		//req.getRequestDispatcher("rest/user/{userId}/image").forward(req, resp);
	}*/

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		logger.info("req="+req);
		ServletContext ctx = req.getServletContext();
		ServletContext target = ctx.getContext("/services/user");
		logger.info("target="+target);
		target.getRequestDispatcher("/services/user").forward(req, resp);
	}
	/*@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.getRequestDispatcher("rest/user/{userId}").forward(req, resp);
	}
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.getRequestDispatcher("rest/user/{userId}").forward(req, resp);
	}*/
	
	

/*	private void useAuthenticationService(Consumer<AuthenticationService> fn)
			throws ServletException {
		BundleContext ctx = FrameworkUtil.getBundle(AuthorizationServlet.class)
				.getBundleContext();
		ServiceReference<AuthenticationService> reference = ctx
				.getServiceReference(AuthenticationService.class);
		AuthenticationService authenticationService = ctx.getService(reference);

		if (authenticationService == null) {
			throw new ServletException(
					"AuthenticationService reference not bound");
		} else {
			try {
				fn.accept(authenticationService);
			} catch (Throwable e) {
				throw new ServletException("Error processing request", e);
			} finally {
				ctx.ungetService(reference);
			}
		}
	}*/
}