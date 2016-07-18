package net.wapwag.authn.ui;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
@WebServlet(urlPatterns = "/login", name = "ThirdLoginServlet")
public class ThirdLoginServlet extends HttpServlet {

	private static final String CHECK_URL = "http://localhost:8181/services/oauth/authorize?client_id=";
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String clientId = req.getParameter("client_id");
		String redirectUri = req.getParameter("redirect_uri");
		if (StringUtil.isEmp(clientId) || StringUtil.isEmp(redirectUri) || !checkClientId(clientId, redirectUri)) {
			req.getRequestDispatcher("error.jsp").forward(req, resp);
		}else {
			HttpSession session = req.getSession();
			String userName = (String)session.getAttribute("userName");
			if (!StringUtil.isEmp(userName) && clientId.equals(session.getAttribute("client_id"))
					&& redirectUri.equals((String)session.getAttribute("redirect_uri"))) {
				
				resp.sendRedirect(redirectUri);
			}else {
				session.setAttribute("client_id", clientId);
				session.setAttribute("redirect_uri", redirectUri);
				req.getRequestDispatcher("login.jsp").forward(req, resp);
			}
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}
	
	private boolean checkClientId(String clientId,String redirectUri)
	{
		String returnJson = HttpUtil.httpSendGET(CHECK_URL + clientId +"&redirect_uri="+redirectUri);
		
		if (!StringUtil.isEmp(returnJson) && -1 != returnJson.indexOf("authorizationCode")) {
			return true;
		}
		return true;
	}
	
	public static void main(String[] args) {
		System.out.println("gong".indexOf("a"));
	}

}
