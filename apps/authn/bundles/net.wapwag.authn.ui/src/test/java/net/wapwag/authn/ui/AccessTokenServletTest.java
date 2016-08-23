package net.wapwag.authn.ui;

import javax.servlet.Servlet;

import org.junit.Test;

import junit.framework.TestCase;
import net.wapwag.authn.dao.UserDao;

public class AccessTokenServletTest extends BaseServletTest {
	
	private static final int port = 9100;
	
	private static final int maxServerThreads = 4;
	
	private static final int acceptQueueSize = 1;

	public AccessTokenServletTest() {
		super(port, maxServerThreads, acceptQueueSize);
	}
	
	protected Servlet createServlet() {
		return new AccessTokenServlet();		
	}
	
	protected UserDao createUserDao() throws Exception {
		return UserDaoMock.getUserDao();
	}
	
    private static final String ACCESSTOKEN = "http://localhost:"+port+"/authn/access_token";

    //====================== invalid_request ========================
    @Test
    public void testError_BasicAuthFailed() throws Exception {
        noBasicHttpAuth();
    }

    private void noBasicHttpAuth() throws Exception {
        JsonResponse res = postAcceptJson(ACCESSTOKEN, false, null, APPLICATION_X_WWW_FORM_URLENCODED, "");
        TestCase.assertEquals(SC_UNAUTHORIZED, res.responseCode);
    }

    //====================== invalid_request ========================
    @Test
	public void testError_InvalidRequest() throws Exception {
        emptyRequest();
        missingGrantType();
        invalidGrantType();
        missingCode();
        missingRedirectURI();
	}

	private void emptyRequest() throws Exception {
        JsonResponse res = postAcceptJson(ACCESSTOKEN, true, "invalidClientId:invalidSecret", APPLICATION_X_WWW_FORM_URLENCODED, "");
        TestCase.assertEquals(SC_BAD_REQUEST, res.responseCode);
        TestCase.assertEquals("invalid_request", res.body.get("error"));
    }

    private void missingGrantType() throws Exception {
        JsonResponse res = postAcceptJson(ACCESSTOKEN, true,
                "invalidClientId:invalidSecret",
                APPLICATION_X_WWW_FORM_URLENCODED, 
                "&code=2b69443d5aebfe6c34a3e90a71e34169" +
                "&redirect_uri=http://www.baidu.com");
        TestCase.assertEquals(SC_BAD_REQUEST, res.responseCode);
        TestCase.assertEquals("invalid_request", res.body.get("error"));
    }

    private void invalidGrantType() throws Exception {
        JsonResponse res = postAcceptJson(ACCESSTOKEN, true,
                "invalidClientId:invalidSecret",
                APPLICATION_X_WWW_FORM_URLENCODED, 
                "grant_type=invalid_grant_type" +
                "&code=2b69443d5aebfe6c34a3e90a71e34169" +
                        "&redirect_uri=http://www.baidu.com");
        TestCase.assertEquals(SC_BAD_REQUEST, res.responseCode);
        TestCase.assertEquals("invalid_request", res.body.get("error"));
    }

    private void missingCode() throws Exception {
        JsonResponse res = postAcceptJson(ACCESSTOKEN, true,
                "invalidClientId:invalidSecret",
                APPLICATION_X_WWW_FORM_URLENCODED, 
                "grant_type=authorization_code" +
                        "&redirect_uri=http://www.baidu.com");
        TestCase.assertEquals(SC_BAD_REQUEST, res.responseCode);
        TestCase.assertEquals("invalid_request", res.body.get("error"));
    }

    private void missingRedirectURI() throws Exception {
        JsonResponse res = postAcceptJson(ACCESSTOKEN, true,
                "invalidClientId:invalidSecret",
                APPLICATION_X_WWW_FORM_URLENCODED, 
                "grant_type=authorization_code" +
                        "&code=2b69443d5aebfe6c34a3e90a71e34169");
        TestCase.assertEquals(SC_BAD_REQUEST, res.responseCode);
        TestCase.assertEquals("invalid_request", res.body.get("error"));
    }

    //====================== invalid_client ========================
    @Test
	public void testError_InvalidClient() throws Exception {
        invalidCredential();
	}

    private void invalidCredential() throws Exception {
        JsonResponse res = postAcceptJson(ACCESSTOKEN, true,
                "invalidClientId:invalidSecret",
                APPLICATION_X_WWW_FORM_URLENCODED, 
                "grant_type=authorization_code" +
                        "&code=2b69443d5aebfe6c34a3e90a71e34169" +
                        "&redirect_uri=http://www.baidu.com");
        TestCase.assertEquals(SC_BAD_REQUEST, res.responseCode);
        TestCase.assertEquals("invalid_client", res.body.get("error"));
    }

    //====================== invalid_grant ========================
    @Test
	public void testError_InvalidGrant() throws Exception {
        invalidAuthorizationCode();
	}

	private void invalidAuthorizationCode() throws Exception {
        JsonResponse res = postAcceptJson(ACCESSTOKEN, true,
                "client1:dfdjfjkdkj23klaa1",
                APPLICATION_X_WWW_FORM_URLENCODED, 
                "grant_type=authorization_code" +
                        "&code=2b69443d5aebfe6c34a3e90a71e34169" +
                        "&redirect_uri=http://www.baidu.com");
        TestCase.assertEquals(SC_BAD_REQUEST, res.responseCode);
        TestCase.assertEquals("invalid_grant", res.body.get("error"));
    }

    //====================== unauthorized_client ========================

	public void testError_UnauthorizedClient() {
        //There is no scenario at this time
	}

    //====================== unsupported_grant_type ========================

	public void testError_UnsupportedGrantType() {
        //There is no scenario at this time
	}

    //====================== invalid_scope ========================

	public void testError_InvalidScope() {
        //There is no scenario at this time
        //All client now registered is wpg client and has a implicit scope for themself
	}

}
