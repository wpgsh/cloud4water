package net.wapwag.wemp.ui;

import net.wapwag.wemp.dao.WaterEquipmentDao;
import org.junit.Ignore;
import org.junit.Test;

import javax.servlet.Filter;
import javax.servlet.Servlet;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("Duplicates")
public class TokenServletTest extends BaseServletTest {
	
	private static final int port = 9100;
	
	private static final int maxServerThreads = 10;
	
	private static final int acceptQueueSize = 1;

	public TokenServletTest() {
		super(port, maxServerThreads, acceptQueueSize);
	}

    @Override
    protected Filter createFilter() throws Exception {
        return null;
    }

    protected Servlet createServlet() {
		return new TokenServlet();		
	}
	
	protected WaterEquipmentDao createWaterEquipmentDao() throws Exception {
		return WaterEquipmentDaoMock.getWaterEquipmentDao();
	}
	
    private static final String ACCESSTOKEN = "http://localhost:"+port+"/authn/access_token";

    //====================== invalid_request ========================
    @Test
    public void testError_BasicAuthFailed() throws Exception {
        noBasicHttpAuth();
    }

    private void noBasicHttpAuth() throws Exception {
        BaseServletTest.JsonResponse res = postAcceptJson(ACCESSTOKEN, false, null, APPLICATION_X_WWW_FORM_URLENCODED, "");
        assertEquals(SC_UNAUTHORIZED, res.responseCode);
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
        BaseServletTest.JsonResponse res = postAcceptJson(ACCESSTOKEN, true, "invalidClientId:invalidSecret", APPLICATION_X_WWW_FORM_URLENCODED, "");
        assertEquals(SC_BAD_REQUEST, res.responseCode);
        assertEquals("invalid_request", res.body.get("error"));
    }

    private void missingGrantType() throws Exception {
        BaseServletTest.JsonResponse res = postAcceptJson(ACCESSTOKEN, true,
                "invalidClientId:invalidSecret",
                APPLICATION_X_WWW_FORM_URLENCODED,
                "&code=2b69443d5aebfe6c34a3e90a71e34169" +
                "&redirect_uri=http://www.baidu.com");
        assertEquals(SC_BAD_REQUEST, res.responseCode);
        assertEquals("invalid_request", res.body.get("error"));
    }

    private void invalidGrantType() throws Exception {
        BaseServletTest.JsonResponse res = postAcceptJson(ACCESSTOKEN, true,
                "invalidClientId:invalidSecret",
                APPLICATION_X_WWW_FORM_URLENCODED,
                "grant_type=invalid_grant_type" +
                "&code=2b69443d5aebfe6c34a3e90a71e34169" +
                        "&redirect_uri=http://www.baidu.com");
        assertEquals(SC_BAD_REQUEST, res.responseCode);
        assertEquals("invalid_request", res.body.get("error"));
    }

    private void missingCode() throws Exception {
        BaseServletTest.JsonResponse res = postAcceptJson(ACCESSTOKEN, true,
                "invalidClientId:invalidSecret",
                APPLICATION_X_WWW_FORM_URLENCODED,
                "grant_type=authorization_code" +
                        "&redirect_uri=http://www.baidu.com");
        assertEquals(SC_BAD_REQUEST, res.responseCode);
        assertEquals("invalid_request", res.body.get("error"));
    }

    private void missingRedirectURI() throws Exception {
        BaseServletTest.JsonResponse res = postAcceptJson(ACCESSTOKEN, true,
                "invalidClientId:invalidSecret",
                APPLICATION_X_WWW_FORM_URLENCODED,
                "grant_type=authorization_code" +
                        "&code=2b69443d5aebfe6c34a3e90a71e34169");
        assertEquals(SC_BAD_REQUEST, res.responseCode);
        assertEquals("invalid_request", res.body.get("error"));
    }

    //====================== invalid_client ========================
    @Test
	public void testError_InvalidClient() throws Exception {
        invalidCredential();
	}

    private void invalidCredential() throws Exception {
        BaseServletTest.JsonResponse res = postAcceptJson(ACCESSTOKEN, true,
                "invalidClientId:invalidSecret",
                APPLICATION_X_WWW_FORM_URLENCODED,
                "grant_type=authorization_code" +
                        "&code=2b69443d5aebfe6c34a3e90a71e34169" +
                        "&redirect_uri=http://www.baidu.com");
        assertEquals(SC_BAD_REQUEST, res.responseCode);
        assertEquals("invalid_client", res.body.get("error"));
    }

    //====================== invalid_grant ========================
    @Test
	public void testError_InvalidGrant() throws Exception {
        invalidAuthorizationCode();
	}

	private void invalidAuthorizationCode() throws Exception {
        BaseServletTest.JsonResponse res = postAcceptJson(ACCESSTOKEN, true,
                "client1:dfdjfjkdkj23klaa1",
                APPLICATION_X_WWW_FORM_URLENCODED, 
                "grant_type=authorization_code" +
                        "&code=2b69443d5aebfe6c34a3e90a71e34169" +
                        "&redirect_uri=http://www.baidu.com");
        assertEquals(SC_BAD_REQUEST, res.responseCode);
        assertEquals("invalid_grant", res.body.get("error"));
    }

    //====================== unauthorized_client ========================

    @Test
    @Ignore("There is no scenario at this time")
	public void testError_UnauthorizedClient() {}

    //====================== unsupported_grant_type ========================

    @Test
    @Ignore("There is no scenario at this time")
	public void testError_UnsupportedGrantType() {}

    //====================== invalid_scope ========================

    @Test
    @Ignore("There is no scenario at this time since All client now registered is wpg client and has a implicit scope for themself")
	public void testError_InvalidScope() {}

}
