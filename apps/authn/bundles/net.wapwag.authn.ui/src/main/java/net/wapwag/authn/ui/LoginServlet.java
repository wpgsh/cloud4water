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
	
	private static final long serialVersionUID = 5293268147114505088L;
	
	/** LOG */
	private static final Logger logger = LoggerFactory
			.getLogger(LoginServlet.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			PrintWriter out = null;
			ResultInfo info = new ResultInfo();
			Gson gson = new Gson();
			info.setErrorCode("11");
			info.setErrorMsg("Do not support GET method");
		
			out = resp.getWriter();
			out.println(gson.toJson(info));
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		OSGIUtil.useAuthenticationService(authnService -> {
			try {
				String userName = req.getParameter("userName");
				String passwd = req.getParameter("passWord");
				String checkCode = req.getParameter("checkCode");
				
				String check = req.getParameter("check");
				
				ResultInfo info = new ResultInfo();
				HttpSession session = req.getSession();
				
				String return_to = (String) session.getAttribute("return_to");
				
				User user = authnService.getUserByName(userName);

				if (checkCode(session, checkCode)) {
					if (checkUser(user, passwd)) {
						session.setAttribute("userName", user.getUsername());
						session.setAttribute("authenticated", true);
						session.setAttribute("userId", user.getId());
						session.setAttribute("phone", user.getPhone1());
						session.setAttribute("email", user.getEmail());
						session.setAttribute("homePage", user.getHomepage());
						session.setAttribute("loginTime", getNowTime());
						session.setMaxInactiveInterval(30 * 60);
						info.setErrorCode("0");
						
						if(!"1".equals(check)){
							if(return_to != null){
								try {
									resp.sendRedirect(return_to);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}else{
								try {
									resp.sendRedirect("index.jsp");
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
					} else {
						logger.info("Error password");
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
					e.printStackTrace();
				}
				out.println(gson.toJson(info));
				out.close();
			} catch (AuthenticationServiceException e) {
				e.printStackTrace();
			}
		}, LoginServlet.class);
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

		if (null != user && null != user.getPasswordHash() && 
				user.getPasswordHash().equals(StringUtil.strSHA1(StringUtil.strMd5(passwd) + user.getPasswordSalt()))) {
			return true;
		}
		return false;
	}

	private long getNowTime(){
		Date date = new Date();
		return date.getTime();
	}
}
