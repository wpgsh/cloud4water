package net.wapwag.authn.ui;

import net.wapwag.authn.AuthenticationServiceException;
import net.wapwag.authn.dao.model.RegisteredClient;
import net.wapwag.authn.exception.ResourceNotFoundException;
import net.wapwag.authn.util.OSGIUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * http://localhost:8181/authn/access_token?code=925fac4f958a4085b3b61988a72606b3
 * Access token servlet.
 * Created by Administrator on 2016/7/14.
 */
@WebServlet(urlPatterns = "/access_token", name = "AccessTokenServlet")
public class AccessTokenServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(AccessTokenServlet.class);

    private static final String ACCESSTOKEN_PATH = "/access_token?client_id=%s&client_secret=%s&code=%s&redirect_uri=%s";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code = request.getParameter("code");
        request.getRequestDispatcher(String.format(ACCESSTOKEN_PATH, "client1", "dfdjfjkdkj23klaa1", code, "http://www.baidu.com"))
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        OSGIUtil.useAuthenticationService(authnService -> {
            RegisteredClient client = null;
            String accessToken = null;
            String code = request.getParameter("code");
            String clientId = request.getParameter("client_id");
            String clientSecret = request.getParameter("client_secret");
            String redirectURI = request.getParameter("redirect_uri");
            try {
                //check client valid
                client = authnService.getClient(redirectURI);
            } catch (AuthenticationServiceException e) {
                e.printStackTrace();
                logger.error("can not get client : " + e);
            }

            if (client == null) {
                throw new ResourceNotFoundException("client not found : " + redirectURI);
            }

            HttpSession session = request.getSession();
            long userId = Long.valueOf(session.getAttribute("userId") + "");
            try {
                accessToken = authnService.getAccessToken(userId, client.getId(), clientSecret, code, redirectURI);
            } catch (AuthenticationServiceException e) {
                e.printStackTrace();
                logger.error("can not get acess_token : " + e);
            }
            try {
                response.getWriter().write("{access_token:" + accessToken + "}");
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("can not write access_token : " + e);
            }
        }, AccessTokenServlet.class);
    }

}
