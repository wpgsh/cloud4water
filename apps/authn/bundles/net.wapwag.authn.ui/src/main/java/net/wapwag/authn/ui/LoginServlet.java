package net.wapwag.authn.ui;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.wapwag.authn.AuthenticationService;
import net.wapwag.authn.AuthenticationServiceException;
import net.wapwag.authn.dao.UserDaoException;
import net.wapwag.authn.dao.UserDaoImpl;
import net.wapwag.authn.dao.model.User;
import net.wapwag.authn.util.StringUtil;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

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
@Component(service=HttpServlet.class, property={ "httpContext.id=authn" })
@WebServlet(urlPatterns="/login", name="LoginServlet")
public class LoginServlet extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String userName = req.getParameter("userName");
		String passwd =  req.getParameter("passWord");
		String checkCode = req.getParameter("checkCode");
		HttpSession session = req.getSession();
		
		if (checkCode(session, checkCode)) {
			if (checkUser(userName, passwd)) {
				session.setAttribute("userName", userName);
				req.getRequestDispatcher("index.jsp").forward(req, resp);
				return;
			}
		}
		req.getRequestDispatcher("login.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
	
	private boolean checkCode(HttpSession session,String checkCode)
	{
		String randomStr = (String) session.getAttribute("randomStr");
		if (StringUtil.isEmp(checkCode) || StringUtil.isEmp(randomStr) || !randomStr.equals(checkCode)) {
			return false;
		}
		return true;
	}
	
	private boolean checkUser(String userName,String passwd)
	{
		if ("gong".equals(userName)) {
			return true;
		}
		return false;
	}

}
