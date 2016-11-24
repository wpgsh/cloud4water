package net.wapwag.authn.ui;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import net.wapwag.authn.dao.model.Image;
import net.wapwag.authn.dao.model.User;
import net.wapwag.authn.util.OSGIUtil;

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
@WebServlet(urlPatterns = "/getAvatarByUserIdServlet", name = "GetAvatarByUserIdServlet")
public class GetAvatarByUserIdServlet extends HttpServlet{

	private static final long serialVersionUID = 1879037093111617850L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		OSGIUtil.useAuthenticationService(authnService -> {
			try {
				String userId = req.getParameter("userId");
				if(StringUtils.isNotEmpty(userId)){
					User user = authnService.getUser(Long.valueOf(userId));
					Image image = authnService.getAvatar(user.getAvartarId());
					if(image != null){
						byte[] content = image.getImage();
						
						resp.setContentType("image/*");
						OutputStream outputStream = resp.getOutputStream();
						outputStream.write(content);
						outputStream.flush();
						outputStream.close();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		},GetAvatarByUserIdServlet.class);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}
}
