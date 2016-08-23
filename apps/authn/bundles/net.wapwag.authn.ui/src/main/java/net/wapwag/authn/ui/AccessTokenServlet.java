package net.wapwag.authn.ui;

import net.wapwag.authn.util.OSGIUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
            String redirectURI = null;
            OAuthResponse oAuthResponse = null;


            try {

                String auth = request.getHeader("Authorization");

                if (StringUtils.isBlank(auth)) {
                    response.setHeader("WWW-Authenticate", "BASIC realm=\"Client Credential\"");
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }

                OAuthTokenRequest oAuthTokenRequest = new OAuthTokenRequest(request);

                String clientId = oAuthTokenRequest.getClientId();
                String code = oAuthTokenRequest.getCode();
                String clientSecret = oAuthTokenRequest.getClientSecret();
                redirectURI = oAuthTokenRequest.getRedirectURI();

                String accessToken = authnService.getAccessToken(clientId, clientSecret, code, redirectURI);

                oAuthResponse = OAuthASResponse
                        .tokenResponse(HttpServletResponse.SC_OK)
                        .setTokenType(OAuth.OAUTH_HEADER_NAME)
                        .setAccessToken(accessToken)
                        .setExpiresIn(String.valueOf(Long.MAX_VALUE))
                        .buildJSONMessage();
            } catch (Exception e) {
                if (e instanceof OAuthProblemException) {
                    try {
                        oAuthResponse = OAuthASResponse
                                .errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                                .error((OAuthProblemException) e)
                                .location(((OAuthProblemException) e).getRedirectUri())
                                .buildJSONMessage();
                    } catch (Exception ex) {
                        if (logger.isErrorEnabled()) {
                            logger.error(ExceptionUtils.getStackTrace(ex));
                        }
                    }
                } else {
                    if (logger.isErrorEnabled()) {
                        logger.error(ExceptionUtils.getStackTrace(e));
                    }
                }
            } finally {
                if (oAuthResponse != null) {
                    response.setContentType("application/json;charset=UTF-8");
                    response.setHeader("Cache-Type", "no-store");
                    response.setHeader("Pragma", "no-cache");
                    response.setStatus(oAuthResponse.getResponseStatus());
                    try {
                        response.getWriter().write(oAuthResponse.getBody());
                    } catch (IOException e) {
                        if (logger.isErrorEnabled()) {
                            logger.error(ExceptionUtils.getStackTrace(e));
                        }
                    }
                }
            }
        }, AccessTokenServlet.class);
    }

}
