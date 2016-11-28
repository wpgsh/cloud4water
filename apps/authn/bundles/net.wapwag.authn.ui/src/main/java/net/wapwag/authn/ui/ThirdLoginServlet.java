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

	private static final long serialVersionUID = 1194899719979790615L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String clientId = req.getParameter("client_id");
		String redirectUri = req.getParameter("redirect_uri");
		String returnTo = req.getParameter("return_to");
		HttpSession session = req.getSession();
		
		System.out.println("returnTo: " + returnTo);
		
		session.setAttribute("client_id", clientId);
		session.setAttribute("redirect_uri", redirectUri);
		session.setAttribute("return_to", returnTo);
		req.getRequestDispatcher("login.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}
}
