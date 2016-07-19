package net.wapwag.authn.ui;

import com.google.gson.Gson;
import net.wapwag.authn.AuthenticationService;
import net.wapwag.authn.AuthenticationServiceException;
import net.wapwag.authn.dao.model.User;
import net.wapwag.authn.info.ResultInfo;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.function.Consumer;

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
			.getLogger(AuthorizationServlet.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		useAuthenticationService(authnService -> {
			try {
				String userName = req.getParameter("userName");
				String passwd = req.getParameter("password_hash");
				String email = req.getParameter("email");
				String phone1 = req.getParameter("phone1");
				ResultInfo info = new ResultInfo();
				User user = new User();
				user.setUsername(userName);
				System.out.println(passwd);
				user.setPasswordHash(passwd);
				user.setEmail(email);
				user.setPhone1(phone1);
				authnService.saveUser(user);

				Gson gson = new Gson();
				PrintWriter out = null;
				try {
					out = resp.getWriter();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				info.setErrorCode("0");
				out.println(gson.toJson(info));
				out.close();
			} catch (AuthenticationServiceException e) {
				e.printStackTrace();
			}
		});
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}

	private void useAuthenticationService(Consumer<AuthenticationService> fn)
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
	}
}
