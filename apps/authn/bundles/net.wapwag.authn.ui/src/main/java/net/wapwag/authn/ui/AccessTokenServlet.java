package net.wapwag.authn.ui;

import net.wapwag.authn.util.OSGIUtil;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * http://localhost:8181/authn/access_token?code=925fac4f958a4085b3b61988a72606b3
 * Access token servlet.
 * Created by Administrator on 2016/7/14.
 */
@WebServlet(urlPatterns = "/access_token", name = "AccessTokenServlet")
public class AccessTokenServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(AccessTokenServlet.class);

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        OSGIUtil.useAuthenticationService(authnService -> {
            OAuthResponse oAuthResponse = null;

            try {

                OAuthTokenRequest oAuthTokenRequest = new OAuthTokenRequest(request);

                String code = oAuthTokenRequest.getCode();
                String clientSecret = oAuthTokenRequest.getClientSecret();
                String redirectURI = oAuthTokenRequest.getRedirectURI();

                String accessToken = authnService.getAccessToken(clientSecret, code, redirectURI);

                oAuthResponse = OAuthASResponse
                        .tokenResponse(HttpServletResponse.SC_OK)
                        .setAccessToken(accessToken)
                        .setExpiresIn("3600")
                        .buildJSONMessage();

                response.setStatus(oAuthResponse.getResponseStatus());
                response.getWriter().write(oAuthResponse.getBody());
            } catch (Exception e) {
                AuthorizationServlet.buildException(oAuthResponse, e, response);
            }
        }, AccessTokenServlet.class);
    }

}
