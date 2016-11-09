package net.wapwag.authn.ui;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;

import com.google.gson.Gson;

import net.wapwag.authn.dao.model.Image;
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
@WebServlet(urlPatterns = "/updateProfileServlet", name = "UpdateProfileServlet")
public class UpdateProfileServlet extends HttpServlet{

	private static final long serialVersionUID = 1386135190838528237L;
	
	// fileMaxSize 2M
	public static final long maxSize = 1024 * 1024 * 2;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		OSGIUtil.useAuthenticationService(authnService -> {
			try {
				String userId = "";
				String userName = "";
				String phone = "";
				String email = "";
				String homePage = "";
				
				DiskFileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);
				boolean flag = ServletFileUpload.isMultipartContent(req);
		        InputStream is = null;
		        Image image = new Image();
		        User user = new User();
		        Gson gson = new Gson();
		        ResultInfo info = new ResultInfo();
		        boolean flagImage = false;
		        try {
		            if (flag) {
		                FileItemIterator iter = upload.getItemIterator(req);
		                while (iter.hasNext()){
		                	FileItemStream fis = iter.next();
		                	is = fis.openStream();
		                    if(fis.isFormField()){
		                    	if("userId".equals(fis.getFieldName())){
		                    		userId = Streams.asString(is);
		                    		System.out.println(fis.getFieldName() + "-" + userId);
		                    	}
		                    	if("inputName".equals(fis.getFieldName())){
		                    		userName = Streams.asString(is);
		                    		System.out.println(fis.getFieldName() + "-" + userName);
		                    	}
		                    	if("inputPhone".equals(fis.getFieldName())){
		                    		phone = Streams.asString(is);
		                    		System.out.println(fis.getFieldName() + "-" + phone);
		                    	}
		                    	if("inputEmail".equals(fis.getFieldName())){
		                    		email = Streams.asString(is);
		                    		System.out.println(fis.getFieldName() + "-" + email);
		                    	}
		                    	if("inputHomePage".equals(fis.getFieldName())){
		                    		homePage = Streams.asString(is);
		                    		System.out.println(fis.getFieldName() + "-" + homePage);
		                    	}
		                    }else{
		                    	
		                    	System.out.println("file name is :" + fis.getName());
		                    	System.out.println(is.available());

		                    	ByteArrayOutputStream output = new ByteArrayOutputStream();  
		                        byte[] buf = new byte[1024];  
		                        int numBytesRead = 0;  
		                        while ((numBytesRead = is.read(buf)) != -1) {  
		                            output.write(buf, 0, numBytesRead);  
		                        }
		                        byte[] photo = output.toByteArray();
		                    	
		                    	if(photo.length <= maxSize){
			                    	user = authnService.getUser(Long.valueOf(userId));
			                    	flagImage = true;
			                    	if(fis.getName() != null){
			                    		System.out.println("user.getAvartarId :" + user.getAvartarId());
			                    		if(user.getAvartarId() != null && !user.getAvartarId().trim().equals("")){
			                    			image.setId(user.getAvartarId());
			                    		}else{
			                    			String avartarId = StringUtil.getUUID();
			                    			image.setId(avartarId);
			                    			user.setAvartarId(avartarId);
			                    		}
				                    	image.setImage(photo);
				                    	System.out.println("photo detail byte : " + photo);
				                    	
			        			        //update image
			        			        int resultImage = authnService.saveImg(image);
			        			        System.out.println("image save result : " + resultImage);
			                    	}
		                    	}else{
		                    		info.setErrorCode("2");
		                    		info.setErrorMsg("file size over 2M");
		                    		
		                    		PrintWriter out = null;
		            				try {
		            					out = resp.getWriter();
		            				} catch (Exception e) {
		            					e.printStackTrace();
		            				}
		            				out.println(gson.toJson(info));
		            				out.close();
		                    	}
		                    }
		                }
		            }
		        }catch (Exception e){
		        }
		        
		        if(!flagImage){
		        	user = authnService.getUser(Long.valueOf(userId));
		        }
		        user.setId(Long.valueOf(userId));
				user.setUsername(userName);
				user.setPhone1(phone);
				user.setEmail(email);
				user.setHomepage(homePage);
				
				int result = authnService.saveUser(user);
				
				if(result > 0){
					info.setErrorCode("0");
					HttpSession session = req.getSession(false);
					session.setAttribute("userName", user.getUsername());
					session.setAttribute("phone", user.getPhone1());
					session.setAttribute("email", user.getEmail());
					session.setAttribute("homePage", user.getHomepage());
				}else{
					info.setErrorCode("1");
				}
				
				PrintWriter out = null;
				try {
					out = resp.getWriter();
				} catch (Exception e) {
					e.printStackTrace();
				}
				out.println(gson.toJson(info));
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}, UpdateProfileServlet.class);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}
}
