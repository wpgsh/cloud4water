package net.wapwag.authn.ui;

import com.google.common.collect.Maps;
import junit.framework.TestCase;
import org.junit.Test;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Test for AuhtorizationServlet class
 * Created by Administrator on 2016/8/11.
 */
public class AuthorizationServletTest {

    private static final String AUTHORIZE_CONTEXT_PATH = "http://localhost:8181/authn/authorize";

    private static Map<String, Object> getRequest(boolean login, String path) throws Exception {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.addRequestProperty("content-type", "application/x-www-form-urlencoded");
        if (login) {
            conn.setRequestProperty("Cookie", login());
        }
        conn.setInstanceFollowRedirects(false);
        conn.connect();
        int respCode = conn.getResponseCode();

        Map<String, Object> result = Maps.newHashMap();
        if (respCode == HttpServletResponse.SC_FOUND) {
            result.put("redirectURI", conn.getHeaderField("Location"));
        }

        result.put("statusCode", respCode);


        conn.disconnect();

        return result;
    }

    private static String login() throws Exception {
        URL url = new URL("http://localhost:8181/authn/loginServlet");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.connect();
        OutputStream out = conn.getOutputStream();
        out.write("userName=test1&passWord=b52c96bea30646abf8170f333bbd42b9".getBytes());
        out.flush();
        conn.disconnect();
        int respCode = conn.getResponseCode();
        if (HttpServletResponse.SC_OK == respCode) {
            return conn.getHeaderField("Set-Cookie");
        } else {
            return null;
        }
    }

    //====================== invalid_request ========================

    @Test
    public void testError_InvalidRequest() throws Exception {
        emptyRequest();
        missingResponseType();
        missingClientId();
    }

    private void emptyRequest() throws Exception {
        Map<String, Object> resultMap = getRequest(false, AUTHORIZE_CONTEXT_PATH);
        TestCase.assertEquals(HttpServletResponse.SC_FOUND, resultMap.get("statusCode"));
        TestCase.assertEquals(
                "http://www.baidu.com?error_description=Missing+response_type+parameter+value&error=invalid_request",
                resultMap.get("redirectURI"));
    }

    private void missingResponseType() throws Exception {
        String path = AUTHORIZE_CONTEXT_PATH +
                "?client_id=client1&redirect_uri=http://www.baidu.com";
        Map<String, Object> resultMap = getRequest(false, path);
        TestCase.assertEquals(HttpServletResponse.SC_FOUND, resultMap.get("statusCode"));
        TestCase.assertEquals(
                "http://www.baidu.com?error_description=Missing+response_type+parameter+value&error=invalid_request",
                resultMap.get("redirectURI"));
    }

    private void missingClientId() throws Exception {
        String path = AUTHORIZE_CONTEXT_PATH +
                "?response_type=code&redirect_uri=http://www.baidu.com";
        Map<String, Object> resultMap = getRequest(false, path);
        TestCase.assertEquals(HttpServletResponse.SC_FOUND, resultMap.get("statusCode"));
        TestCase.assertEquals(
                "http://www.baidu.com?error_description=Missing+parameters%3A+client_id&error=invalid_request",
                resultMap.get("redirectURI"));
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
        Map<String, Object> resultMap = getRequest(true, path);
        TestCase.assertEquals(HttpServletResponse.SC_FOUND, resultMap.get("statusCode"));
        TestCase.assertEquals(
                "http://www.baidu.com?error_description=error+client+credential&error=unauthorized_client",
                resultMap.get("redirectURI"));
    }

    private void invalidRedirectURI() throws Exception {
        String path = AUTHORIZE_CONTEXT_PATH +
                "?response_type=code&client_id=invalidClient&redirect_uri=invalidRequestURI";
        Map<String, Object> resultMap = getRequest(true, path);
        TestCase.assertEquals(HttpServletResponse.SC_FOUND, resultMap.get("statusCode"));
        TestCase.assertEquals(
                "http://www.baidu.com?error_description=error+client+credential&error=unauthorized_client",
                resultMap.get("redirectURI"));
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
