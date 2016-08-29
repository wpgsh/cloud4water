package net.wapwag.authn.ui;

import junit.framework.TestCase;
import net.wapwag.authn.dao.UserDao;
import org.junit.Test;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Test for AuhtorizationServlet class
 * Created by Administrator on 2016/8/11.
 */
public class AuthorizationServletTest extends BaseServletTest {

    private static final int port = 9100;

    private static final int maxServerThreads = 10;

    private static final int acceptQueueSize = 1;

    private static final String AUTHORIZE_CONTEXT_PATH = "http://localhost:" + port + "/authn/authorize";

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

    protected Servlet createServlet() {
        return new AuthorizationServlet();
    }

    protected UserDao createUserDao() throws Exception {
        return UserDaoMock.getUserDao();
    }

    public AuthorizationServletTest() {
        super(port, maxServerThreads, acceptQueueSize);
    }

    //====================== invalid_request ========================

    @Test
    public void testError_InvalidRequest() throws Exception {
        emptyRequest();
        missingResponseType();
        missingClientId();
    }

    private void emptyRequest() throws Exception {
        QueryComponentResponse response = getAcceptQueryComponent(AUTHORIZE_CONTEXT_PATH,
                APPLICATION_X_WWW_FORM_URLENCODED);
        TestCase.assertEquals(SC_FOUND, response.responseCode);
        TestCase.assertEquals(
                "http://www.baidu.com?error_description=Missing+response_type+parameter+value&error=invalid_request",
                response.body.get("redirectURI"));
    }

    private void missingResponseType() throws Exception {
        String path = AUTHORIZE_CONTEXT_PATH + "?client_id=client1&redirect_uri=http://www.baidu.com";
        QueryComponentResponse response = getAcceptQueryComponent(path, APPLICATION_X_WWW_FORM_URLENCODED);
        TestCase.assertEquals(SC_FOUND, response.responseCode);
        TestCase.assertEquals(
                "http://www.baidu.com?error_description=Missing+response_type+parameter+value&error=invalid_request",
                response.body.get("redirectURI"));
    }

    private void missingClientId() throws Exception {
        String path = AUTHORIZE_CONTEXT_PATH + "?response_type=code&redirect_uri=http://www.baidu.com";
        QueryComponentResponse response = getAcceptQueryComponent(path, APPLICATION_X_WWW_FORM_URLENCODED);
        TestCase.assertEquals(SC_FOUND, response.responseCode);
        TestCase.assertEquals(
                "http://www.baidu.com?error_description=Missing+parameters%3A+client_id&error=invalid_request",
                response.body.get("redirectURI"));
    }

    //====================== unauthorized_client ========================

    @Test
    public void testError_UnauthorizedClient() throws Exception {
        invalidClient();
        invalidRedirectURI();
    }

    private void invalidClient() throws Exception {
        String path = AUTHORIZE_CONTEXT_PATH +
                "?response_type=code&client_id=invalidClient&redirect_uri=http://www.baidu.com";
        QueryComponentResponse response = getAcceptQueryComponent(path, APPLICATION_X_WWW_FORM_URLENCODED);
        TestCase.assertEquals(SC_FOUND, response.responseCode);
        TestCase.assertEquals(
                "http://www.baidu.com?error_description=error+client+credential&error=unauthorized_client",
                response.body.get("redirectURI"));
    }

    private void invalidRedirectURI() throws Exception {
        String path = AUTHORIZE_CONTEXT_PATH +
                "?response_type=code&client_id=invalidClient&redirect_uri=invalidRequestURI";
        QueryComponentResponse response = getAcceptQueryComponent(path, APPLICATION_X_WWW_FORM_URLENCODED);
        TestCase.assertEquals(SC_FOUND, response.responseCode);
        TestCase.assertEquals(
                "http://www.baidu.com?error_description=error+client+credential&error=unauthorized_client",
                response.body.get("redirectURI"));
    }

    //====================== access_denied ========================
    public void testError_AccessDenied() throws Exception {
        //There is no scenario at this time
    }

    //====================== unsupported_response_type ========================

    public void testError_UnsupportedResponseType() throws Exception {
        //There is no scenario at this time
    }

    //====================== invalid_scope ========================

    public void testError_InvalidScope() throws Exception {
        //There is no scenario at this time
        //All client now registered is wpg client
    }

    //====================== server_error ========================

    public void testError_ServerError() throws Exception {
        //There is no scenario at this time
    }

    //====================== temporarily_unavailable ========================

    public void testError_TemporarilyUnavalible() throws Exception {
        //There is no scenario at this time
    }

}
