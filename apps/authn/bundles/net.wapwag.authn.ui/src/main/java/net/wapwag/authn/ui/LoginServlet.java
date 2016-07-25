package net.wapwag.authn.ui;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.wapwag.authn.AuthenticationServiceException;
import net.wapwag.authn.dao.model.User;
import net.wapwag.authn.info.ResultInfo;
import net.wapwag.authn.util.OSGIUtil;
import net.wapwag.authn.util.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
@WebServlet(urlPatterns = "/loginServlet", name = "LoginServlet")
public class LoginServlet extends HttpServlet {
	/** LOG */
	private static final Logger logger = LoggerFactory
			.getLogger(LoginServlet.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		OSGIUtil.useAuthenticationService(authnService -> {
			try {
				String userName = req.getParameter("userName");
				String passwd = req.getParameter("passWord");
				String checkCode = req.getParameter("checkCode");
				ResultInfo info = new ResultInfo();
				HttpSession session = req.getSession();
				String redirectUri = (String) session
						.getAttribute("redirect_uri");

				User user = authnService.getUserByName(userName);

				if (checkCode(session, checkCode)) {
					if (checkUser(user, passwd)) {
						session.setAttribute("userName", user.getUsername());
						session.setAttribute("authenticated", true);
						session.setAttribute("userId", user.getId());
						session.setAttribute("loginTime", getNowTime());
						info.setErrorCode("0");
					} else {
						info.setErrorCode("1");
					}
				} else {
					info.setErrorCode("2");
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
		}, LoginServlet.class);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}

	private boolean checkCode(HttpSession session, String checkCode) {
		String randomStr = (String) session.getAttribute("randomStr");
		if (StringUtil.isEmp(checkCode) || StringUtil.isEmp(randomStr)
				|| !randomStr.equals(checkCode)) {
			return true;
		}
		return true;
	}

	private boolean checkUser(User user, String passwd) {

		if (null != user && null != user.getPasswordHash() && passwd.equals(StringUtil.strMd5(user.getPasswordHash()))) {
			return true;
		}
		return false;
	}

	private long getNowTime(){
		Date date = new Date();
		return date.getTime();
	}
}