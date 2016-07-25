package net.wapwag.authn.ui;

import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.fluent.Request;
import org.apache.http.message.BasicNameValuePair;

import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Test third login wpg system.
 * Created by Administrator on 2016/7/21.
 */
@WebServlet(urlPatterns = "/wpg", name = "WPGServlet")
public class WPGServlet extends HttpServlet {

    /**
     * Called by the server (via the <code>service</code> method) to
     * allow a servlet to handle a GET request.
     * <p>
     * <p>Overriding this method to support a GET request also
     * automatically supports an HTTP HEAD request. A HEAD
     * request is a GET request that returns no body in the
     * response, only the request header fields.
     * <p>
     * <p>When overriding this method, read the request data,
     * write the response headers, get the response's writer or
     * output stream object, and finally, write the response data.
     * It's best to include content type and encoding. When using
     * a <code>PrintWriter</code> object to return the response,
     * set the content type before accessing the
     * <code>PrintWriter</code> object.
     * <p>
     * <p>The servlet container must write the headers before
     * committing the response, because in HTTP the headers must be sent
     * before the response body.
     * <p>
     * <p>Where possible, set the Content-Length header (with the
     * {@link ServletResponse#setContentLength} method),
     * to allow the servlet container to use a persistent connection
     * to return its response to the client, improving performance.
     * The content length is automatically set if the entire response fits
     * inside the response buffer.
     * <p>
     * <p>When using HTTP 1.1 chunked encoding (which means that the response
     * has a Transfer-Encoding header), do not set the Content-Length header.
     * <p>
     * <p>The GET method should be safe, that is, without
     * any side effects for which users are held responsible.
     * For example, most form queries have no side effects.
     * If a client request is intended to change stored data,
     * the request should use some other HTTP method.
     * <p>
     * <p>The GET method should also be idempotent, meaning
     * that it can be safely repeated. Sometimes making a
     * method safe also makes it idempotent. For example,
     * repeating queries is both safe and idempotent, but
     * buying a product online or modifying data is neither
     * safe nor idempotent.
     * <p>
     * <p>If the request is incorrectly formatted, <code>doGet</code>
     * returns an HTTP "Bad Request" message.
     *
     * @param req  an {@link HttpServletRequest} object that
     *             contains the request the client has made
     *             of the servlet
     * @param resp an {@link HttpServletResponse} object that
     *             contains the response the servlet sends
     *             to the client
     * @throws IOException      if an input or output error is
     *                          detected when the servlet handles
     *                          the GET request
     * @throws ServletException if the request for the GET
     *                          could not be handled
     * @see ServletResponse#setContentType
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String code = req.getParameter("code");
        if (StringUtils.isNotBlank(code)) {
            NameValuePair pair1 = new BasicNameValuePair("code", code);
            NameValuePair pair2 = new BasicNameValuePair("client_id", "wpgclient");
            NameValuePair pair3 = new BasicNameValuePair("redirect_uri", "http://localhost:8181/authn/wpg");
            NameValuePair pair4 = new BasicNameValuePair("client_secret", "dfdjfjkdkj23klaa1");
            NameValuePair pair5 = new BasicNameValuePair("grant_type", "authorization_code");
            String result = Request.Post("http://192.168.10.192:8181/authn/access_token")
                    .useExpectContinue()
                    .version(HttpVersion.HTTP_1_1)
                    .bodyForm(pair1, pair2, pair3, pair4, pair5)
                    .execute().returnContent().asString();
            Gson gson = new Gson();
            Map response = gson.fromJson(result, Map.class);
            String accessToken = (String) response.get("access_token");
            result = Request.Get("http://192.168.10.192:8181/services/user/1")
                    .addHeader("authorization", "Bearer " + accessToken)
                    .version(HttpVersion.HTTP_1_1)
                    .execute().returnContent().asString();
            resp.getWriter().write(result);
        } else {
            resp.sendRedirect("http://192.168.10.192:8181/authn/authorize?response_type=code&client_id=wpgclient&redirect_uri=http://localhost:8181/authn/wpg&scope=user:email");
        }
    }
}
