package net.wapwag.authn.ui;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

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

public class ChangePasswordServletTest extends BaseServletTest{

	
	private static final int port = 9100;

    private static final int maxServerThreads = 10;

    private static final int acceptQueueSize = 1;

    private static final String LOGIN_CONTEXT_PATH = "http://localhost:" + port + "/authn/changePasswordServlet?userId=1&inputPassword=dfdf&inputNewPassword=123123";
    
    private static final String LOGIN_CONTEXT_PATH_WRONGPASSWORD = "http://localhost:" + port + "/authn/changePasswordServlet?userId=1&inputPassword=123123&inputNewPassword=123123";
    
	public ChangePasswordServletTest() {
		super(port, maxServerThreads, acceptQueueSize);
	}
    
	@Test
	public void testDoPostHttpServletRequestHttpServletResponse() throws Exception {
		QueryComponentResponse resqponse = getAcceptQueryComponent(LOGIN_CONTEXT_PATH, APPLICATION_X_WWW_FORM_URLENCODED);
		assertEquals(HttpServletResponse.SC_OK, resqponse.responseCode);
	}
	
	@Test
	public void testDoPostHttpServletRequestHttpServletResponse_wrongpassword() throws Exception {
		QueryComponentResponse resqponse = getAcceptQueryComponent(LOGIN_CONTEXT_PATH_WRONGPASSWORD, APPLICATION_X_WWW_FORM_URLENCODED);
		assertEquals(HttpServletResponse.SC_OK, resqponse.responseCode);
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
                    HttpServletRequest httpRequest = (HttpServletRequest) request;
                    HttpSession session = httpRequest.getSession();
                    if (session != null && session.getAttribute("Authenticated") == null) {
                        session.setAttribute("userId", 1L);
                        session.setAttribute("authenticated", true);
                    }
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
		return new ChangePasswordServlet();
	}

	@Override
	protected UserDao createUserDao() throws Exception {
		return UserDaoMock.getUserDao();
	}
}
