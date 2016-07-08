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
@Component(service = HttpServlet.class, property = { "httpContext.id=authn" })
@WebServlet(urlPatterns = "/checkCode", name = "CheckCodeServlet")
public class CheckCodeServlet extends HttpServlet {
	// 图片宽度
	int width = 140;
	// 图片高度
	int height = 44;
	// 图片上随机字符个数
	int randomStrNum = 4;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("CheckCode");
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		// 获取HttpSession对象
		HttpSession session = request.getSession();
		// 获取随机字符串
		String randomStr = RandomImageGenerator.random(randomStrNum);
		if (null != session) {
			// 设置参数
			session.setAttribute("randomStr", randomStr);
			// 设置响应类型,输出图片客户端不缓存
			response.setDateHeader("Expires", 1L);
			response.setHeader("Cache-Control", "no-cache, no-store, max-age=0");
			response.addHeader("Pragma", "no-cache");
			response.setContentType("image/jpeg");
			// 输出到页面
			RandomImageGenerator.render(randomStr, response.getOutputStream(),
					width, height);
		}
	}

	public void init() throws ServletException {
		// 获取宽度
		//width = Integer.parseInt(this.getInitParameter("width"));
		// 获取高度
		//height = Integer.parseInt(this.getInitParameter("height"));
		// 获取个数
		//randomStrNum = Integer.parseInt(this.getInitParameter("num"));
	}
}
