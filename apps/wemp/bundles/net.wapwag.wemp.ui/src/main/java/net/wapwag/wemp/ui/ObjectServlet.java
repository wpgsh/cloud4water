package net.wapwag.wemp.ui;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// See https://tools.ietf.org/html/rfc6749#section-4.1.1
@WebServlet(urlPatterns = "/object", name = "WEMP_ObjectServlet")
public class ObjectServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/object-dict.jsp").forward(request, response);
	}

}
