package net.wapwag.authn.ui;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String clientId = req.getParameter("client_id");
		String redirectUri = req.getParameter("redirect_uri");
		HttpSession session = req.getSession();
		String userName = (String)session.getAttribute("userName");
		
		/**
		if (!StringUtil.isEmp(userName) && clientId.equals(session.getAttribute("client_id"))
				&& redirectUri.equals((String)session.getAttribute("redirect_uri"))) {
			
			resp.sendRedirect(redirectUri);
		}else {
			session.setAttribute("client_id", clientId);
			session.setAttribute("redirect_uri", redirectUri);
			req.getRequestDispatcher("login.jsp").forward(req, resp);
		}
		**/
		session.setAttribute("client_id", clientId);
		session.setAttribute("redirect_uri", redirectUri);
		req.getRequestDispatcher("login.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}
}
