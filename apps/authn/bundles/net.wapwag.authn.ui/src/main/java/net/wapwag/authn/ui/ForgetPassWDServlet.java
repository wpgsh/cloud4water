package net.wapwag.authn.ui;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.wapwag.authn.AuthenticationServiceException;
import net.wapwag.authn.dao.model.User;
import net.wapwag.authn.info.ResultInfo;
import net.wapwag.authn.util.OSGIUtil;
import net.wapwag.authn.util.SendEmailUtil;
import net.wapwag.authn.util.SequenceKey;
import net.wapwag.authn.util.StringUtil;

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
@WebServlet(urlPatterns="/password_reset", name="ForgetPassWDServlet")
public class ForgetPassWDServlet extends HttpServlet {

	private static final long serialVersionUID = -4026159133625327458L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String email = req.getParameter("email");
		OSGIUtil.useAuthenticationService(authnService -> {
			try {
				String url = req.getRequestURL().toString();
				url = url.split("/password_reset")[0];
				ResultInfo info = new ResultInfo();
				info.setErrorCode("1");
				User user = authnService.getUserByEmail(email);
				if (null != user && !StringUtil.isEmp(user.getId() + "")) {
					String resetKey = SequenceKey.createResetKey(user.getId() + "");
					if (SendEmailUtil.sendEmail(resetKey, user.getEmail(),url)) {
						SequenceKey.addResetKey(resetKey);
						info.setErrorCode("0");
					}else {
						info.setErrorCode("2");
					}
				}
				Gson gson = new Gson();
				PrintWriter out = null;
				try {
					out = resp.getWriter();
				} catch (Exception e) {
					e.printStackTrace();
				}
				out.println(gson.toJson(info));
				out.close();
			} catch (AuthenticationServiceException e) {
				e.printStackTrace();
			}
		}, ForgetPassWDServlet.class);
		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
