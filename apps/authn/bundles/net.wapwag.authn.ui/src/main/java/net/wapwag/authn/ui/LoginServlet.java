package net.wapwag.authn.ui;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.wapwag.authn.info.CheckResultInfo;
import net.wapwag.authn.info.UserInfo;
import net.wapwag.authn.util.HttpUtil;
import net.wapwag.authn.util.StringUtil;

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
@Component(service = HttpServlet.class, property = { "httpContext.id=authn" })
@WebServlet(urlPatterns = "/loginServlet", name = "LoginServlet")
public class LoginServlet extends HttpServlet {

	private static final String QUERY_USER_URL = "http://localhost:8181/services/user/getUserByName/";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String userName = req.getParameter("userName");
		String passwd = req.getParameter("passWord");
		String checkCode = req.getParameter("checkCode");
		PrintWriter out = resp.getWriter();
		CheckResultInfo info = new CheckResultInfo();
		HttpSession session = req.getSession();
		String redirectUri = (String)session.getAttribute("redirect_uri");
		if (checkCode(session, checkCode)) {
			if (checkUser(userName, passwd)) {
				session.setAttribute("userName", userName);
				if (StringUtil.isEmp(redirectUri)) {
					info.setErrorCode("0");
				}else {
					info.setErrorCode("000000");
					info.setErrorMsg(redirectUri);
				}
				
			}else {
				info.setErrorCode("1");
			}
		} else {
			info.setErrorCode("2");
		}
		Gson gson = new Gson();
		out.println(gson.toJson(info));
		out.close();
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
			return false;
		}
		return true;
	}

	private boolean checkUser(String userName, String passwd) {
		String returnJson = HttpUtil.httpSendGET(QUERY_USER_URL + userName);
		Gson gson = new Gson();
		UserInfo info = gson.fromJson(returnJson, UserInfo.class);
		System.out.println(info.toString());
		if (null != info && passwd.equals(info.getPassword_hash())) {
			return true;
		}

		return false;
	}

}
