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
	// ͼƬ���
	int width = 140;
	// ͼƬ�߶�
	int height = 44;
	// ͼƬ������ַ�����
	int randomStrNum = 4;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("CheckCode");
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		// ��ȡHttpSession����
		HttpSession session = request.getSession();
		// ��ȡ����ַ���
		String randomStr = RandomImageGenerator.random(randomStrNum);
		if (null != session) {
			// ���ò���
			session.setAttribute("randomStr", randomStr);
			// ������Ӧ����,���ͼƬ�ͻ��˲�����
			response.setDateHeader("Expires", 1L);
			response.setHeader("Cache-Control", "no-cache, no-store, max-age=0");
			response.addHeader("Pragma", "no-cache");
			response.setContentType("image/jpeg");
			// �����ҳ��
			RandomImageGenerator.render(randomStr, response.getOutputStream(),
					width, height);
		}
	}

	public void init() throws ServletException {
		// ��ȡ���
		//width = Integer.parseInt(this.getInitParameter("width"));
		// ��ȡ�߶�
		//height = Integer.parseInt(this.getInitParameter("height"));
		// ��ȡ����
		//randomStrNum = Integer.parseInt(this.getInitParameter("num"));
	}
}
