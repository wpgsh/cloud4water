package net.wapwag.authn.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Test;

import net.wapwag.authn.dao.UserDao;

public class UpdateProfileServletNoSessionTest extends BaseServletTest{

	private static final int port = 9100;

    private static final int maxServerThreads = 10;

    private static final int acceptQueueSize = 1;

    private static final String UPDATEPROFILE_CONTEXT_PATH = "http://localhost:" + port + "/authn/updateProfileServlet";
    

	public UpdateProfileServletNoSessionTest() {
		super(port, maxServerThreads, acceptQueueSize);
	}

	@Test
	public void testDoPostHttpServletRequestHttpServletResponse_no_session() throws Exception {
		ArrayList<FormFieldKeyValuePair> ffkvp = new ArrayList<FormFieldKeyValuePair>();  
        ffkvp.add(new FormFieldKeyValuePair("userId", "1"));
        ffkvp.add(new FormFieldKeyValuePair("inputName", "test1"));
        ffkvp.add(new FormFieldKeyValuePair("inputPhone", "15850817392"));
        ffkvp.add(new FormFieldKeyValuePair("inputEmail", "jiangzehu@163.com"));
        ffkvp.add(new FormFieldKeyValuePair("inputHomePage", "http://www.baidu.com"));
        ffkvp.add(new FormFieldKeyValuePair("type", "png"));
  
        ArrayList<UploadFileItem> ufi = new ArrayList<UploadFileItem>();
        URL path = UpdateProfileServletNoSessionTest.class.getClassLoader().getResource("123.png");

        assertNotNull(path);
        ufi.add(new UploadFileItem("image", path.getPath()));
		
		QueryComponentResponse resqponse =  sendHttpPostRequest(UPDATEPROFILE_CONTEXT_PATH, ffkvp, ufi);
		assertEquals(HttpServletResponse.SC_OK, resqponse.responseCode);
		assertEquals(true, resqponse.body.get("response").toString().contains("3"));
	}

	@Override
	protected Filter createFilter() throws Exception {
		return new Filter() {
			
			@Override
			public void init(FilterConfig filterConfig) throws ServletException {
				
			}
			
			@Override
			public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
					throws IOException, ServletException {
				if (request instanceof HttpServletRequest) {
//                    HttpServletRequest httpRequest = (HttpServletRequest) request;
//                    HttpSession session = httpRequest.getSession();
//                    if (session != null && session.getAttribute("Authenticated") == null) {
//                        session.setAttribute("userId", 1L);
//                        session.setAttribute("authenticated", true);
//                    }
                }
                request.setCharacterEncoding("UTF-8");
                response.setCharacterEncoding("UTF-8");
                chain.doFilter(request, response);
			}
			
			@Override
			public void destroy() {
				
			}
		};
	}

	@Override
	protected Servlet createServlet() throws Exception {
		return new UpdateProfileServlet();
	}

	@Override
	protected UserDao createUserDao() throws Exception {
		return UserDaoMock.getUserDao();
	}

}
