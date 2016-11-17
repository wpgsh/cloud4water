package net.wapwag.authn.ui;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import net.wapwag.authn.dao.model.User;
import net.wapwag.authn.info.ResultInfo;
import net.wapwag.authn.util.OSGIUtil;
import net.wapwag.authn.util.StringUtil;


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
@WebServlet(urlPatterns = "/changePasswordServlet", name = "ChangePasswordServlet")
public class ChangePasswordServlet extends HttpServlet{

	private static final long serialVersionUID = 1614886356167643422L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		OSGIUtil.useAuthenticationService(authnService -> {
			try {
				Gson gson = new Gson();
				ResultInfo info = new ResultInfo();
				//check session
				HttpSession session = req.getSession(false);
				
				if(session != null){
					String userId = req.getParameter("userId");
					String inputPassword = req.getParameter("inputPassword");
					String inputNewPassword = req.getParameter("inputNewPassword");
					
					User user = authnService.getUser(Long.valueOf(userId));
					boolean flag = true;
					//check the old password
					if(!StringUtil.strSHA1(StringUtil.strMd5(inputPassword) + user.getPasswordSalt()).equals(user.getPasswordHash())){
						info.setErrorCode("1");
						info.setErrorMsg("old password Incorrect");
						flag = false;
					};
					if(flag){
						user.setPasswordHash(StringUtil.strSHA1(StringUtil.strMd5(inputNewPassword) + user.getPasswordSalt()));
						int result = authnService.saveUser(user);
						
						if(result > 0){
							info.setErrorCode("0");
						}else{
							info.setErrorCode("2");
							info.setErrorMsg("save failure");
						}
					}
					
					PrintWriter out = null;
					try {
						out = resp.getWriter();
					} catch (Exception e) {
						e.printStackTrace();
					}
					out.println(gson.toJson(info));
					out.close();
				}else{
					PrintWriter out = null;
					info.setErrorCode("3");
					try {
						out = resp.getWriter();
					} catch (Exception e) {
						e.printStackTrace();
					}
					out.println(gson.toJson(info));
					out.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}, ChangePasswordServlet.class);
		
	}
	
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}
}
