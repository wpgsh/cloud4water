package net.wapwag.authn.ui;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.function.Consumer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.wapwag.authn.AuthenticationService;
import net.wapwag.authn.AuthenticationServiceException;
import net.wapwag.authn.dao.model.User;
import net.wapwag.authn.info.ResultInfo;
import net.wapwag.authn.util.AsyncLoginUtil;
import net.wapwag.authn.util.SendEmailUtil;
import net.wapwag.authn.util.SequenceKey;
import net.wapwag.authn.util.StringUtil;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Component;

import com.google.gson.Gson;

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
@WebServlet(urlPatterns="/password_reseta", name="ResetPassWDServlet")
public class ResetPassWDServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String key = req.getParameter("key");
		String passWord = req.getParameter("passWord");
		if (!StringUtil.isEmp(key) && !StringUtil.isEmp(passWord)) {
			if (SequenceKey.isExit(key)) {
				useAuthenticationService(authnService -> {
					try {
						ResultInfo info = new ResultInfo();
						info.setErrorCode("1");
						User user  = new User();
						user.setId(Long.parseLong(SequenceKey.getUserId(key)));
						user.setPasswordHash(passWord);
						user = authnService.updateUserPwd(user);

						if (null != user) {
							SequenceKey.cleanKey(key);
							info.setErrorCode("0");
						}
						Gson gson = new Gson();
						PrintWriter out = null;
						try {
							out = resp.getWriter();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						out.println(gson.toJson(info));
						out.close();
					} catch (AuthenticationServiceException e) {
						e.printStackTrace();
					}
				});
			}
			req.getRequestDispatcher("authn/password_reset").forward(req, resp);
		}else {
			String path = req.getRemoteAddr();
			path = path.split("password_reset\\/")[1];
			if (!SequenceKey.isExit(path)) {
				req.getRequestDispatcher("authn/password_reset").forward(req, resp);
			}
			req.getRequestDispatcher("resetpassword.jsp").forward(req, resp);
		}
		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
