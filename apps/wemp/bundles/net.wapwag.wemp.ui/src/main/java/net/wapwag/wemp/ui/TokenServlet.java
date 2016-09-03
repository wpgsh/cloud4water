package net.wapwag.wemp.ui;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// See https://github.com/wpgsh/cloud4water/wiki/Authentication-Application#invocation-of-access-token-endpoint
@WebServlet(urlPatterns = "/token", name = "WEMP_TokenServlet")
public class TokenServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Implement the flow between "WEMP" and "Auth" components 
		//      as described at https://github.com/wpgsh/cloud4water/wiki/Authentication-Application#no-user-sessions-exists
		throw new RuntimeException("TODO - not implemented");
	}


}
