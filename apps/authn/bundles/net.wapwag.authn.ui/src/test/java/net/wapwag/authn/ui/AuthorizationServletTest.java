package net.wapwag.authn.ui;

import com.google.common.collect.Maps;
import junit.framework.TestCase;
import org.junit.Test;

import javax.servlet.http.HttpServletResponse;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Test for AuhtorizationServlet class
 * Created by Administrator on 2016/8/11.
 */
public class AuthorizationServletTest {

    private static final String RESPONSETYPE = "code";
    private static final String CLIENT_ID = "client1";
    private static final String REDIRECT_URI = "http://www.baidu.com";
    private static final String AUTHORIZE_CONTEXT_PATH = "http://localhost:8181/authn/authorize";

    private static Map<String, Object> getRequest(String path) throws Exception {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.addRequestProperty("content-type", "application/x-www-form-urlencoded");
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

    @Test
    public void missingResponseType() throws Exception {
        String path = AUTHORIZE_CONTEXT_PATH +
                "?client_id=client1&redirect_uri=http://www.baidu.com&scope=1&scope=2&scope=3";
        Map<String, Object> resultMap = getRequest(path);
        TestCase.assertEquals(HttpServletResponse.SC_FOUND, resultMap.get("statusCode"));
        TestCase.assertEquals("http://www.baidu.com?error_description=Missing+response_type+parameter+value&error=invalid_request", resultMap.get("redirectURI"));
    }

    @Test
    public void missingClientId() throws Exception {
        String path = AUTHORIZE_CONTEXT_PATH +
                "?response_type=code&redirect_uri=http://www.baidu.com&scope=1&scope=2&scope=3";
        Map<String, Object> resultMap = getRequest(path);
        TestCase.assertEquals(HttpServletResponse.SC_FOUND, resultMap.get("statusCode"));
        TestCase.assertEquals("http://www.baidu.com?error_description=Missing+parameters%3A+client_id&error=invalid_request", resultMap.get("redirectURI"));
    }

    public void invalidClient() throws Exception {
        String path = AUTHORIZE_CONTEXT_PATH +
                "?response_type=code&client_id=client1&redirect_uri=invalid_client&scope=1&scope=2&scope=3";
        TestCase.assertEquals("Missing parameters: client_id", getRequest(path));
    }

}
