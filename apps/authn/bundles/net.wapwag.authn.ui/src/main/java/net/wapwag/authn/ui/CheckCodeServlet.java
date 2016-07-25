package net.wapwag.authn.ui;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.wapwag.authn.util.RandomImageGenerator;

import org.osgi.service.component.annotations.Component;

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
@WebServlet(urlPatterns = "/checkCode", name = "CheckCodeServlet")
public class CheckCodeServlet extends HttpServlet {
	int width = 140;
	int height = 44;
	int randomStrNum = 4;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("CheckCode");
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		HttpSession session = request.getSession();
		String randomStr = RandomImageGenerator.random(randomStrNum);
		if (null != session) {
			session.setAttribute("randomStr", randomStr);
			response.setDateHeader("Expires", 1L);
			response.setHeader("Cache-Control", "no-cache, no-store, max-age=0");
			response.addHeader("Pragma", "no-cache");
			response.setContentType("image/jpeg");
			RandomImageGenerator.render(randomStr, response.getOutputStream(),
					width, height);
		}
	}
}
