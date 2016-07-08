package net.wapwag.authn.ui;

import javax.servlet.http.HttpServlet;

public class BaseServlet extends HttpServlet {

	public BaseServlet() {
		
	}
	
	private boolean checkSession()
	{
		return true;
	}
}
