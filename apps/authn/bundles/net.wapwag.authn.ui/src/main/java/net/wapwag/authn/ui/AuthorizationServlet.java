package net.wapwag.authn.ui;

import net.wapwag.authn.util.OSGIUtil;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Set;

/**
 * http://localhost:8181/authn/authorize
 * ?response_type=code
 * &client_id=client1
 * &redirect_uri=http://www.baidu.com
 * &state=wapwag
 * &scope=1&scope=2&scope=3
 * Authorization servlet.
 */
@WebServlet(urlPatterns = "/authorize", name = "AuthorizationServlet")
public class AuthorizationServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(AuthorizationServlet.class);

    /**
     * The path for /authorize.
     */
    private static final String AUTHORIZE_PATH = "/authn/login?client_id=%s" +
            "&return_to=/authn/authorize?response_type=%s&redirect_uri=%s&client_id=%s&scope=%s";

	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        OSGIUtil.useAuthenticationService(authnService  -> {
            OAuthResponse oAuthResponse = null;
            String redirectURI = null;

            HttpSession session = request.getSession();

            boolean authenticated = Boolean.valueOf(String.valueOf(session.getAttribute("authenticated")));

	        try {

                OAuthAuthzRequest oauthRequest = new OAuthAuthzRequest(request);

                String scope = request.getParameter("scope");
                String code = null;
                String type = oauthRequest.getResponseType();
                String clientId = oauthRequest.getClientId();
                redirectURI = oauthRequest.getRedirectURI();
                Set<String> scopes = oauthRequest.getScopes();

                if (authenticated) {

                    long userId = Long.valueOf(String.valueOf(session.getAttribute("userId")));

                    //Get authorization code.
                    code = authnService.getAuthorizationCode(userId, redirectURI, scopes);

                    oAuthResponse = OAuthASResponse
                            .authorizationResponse(request, HttpServletResponse.SC_FOUND)
                            .setCode(code)
                            .location(oauthRequest.getRedirectURI())
                            .buildQueryMessage();
                    response.sendRedirect(oAuthResponse.getLocationUri());

                } else {
                    //build return_to uri if not login.
                    redirectURI = String.format(AUTHORIZE_PATH, clientId, type, redirectURI, clientId, scope);
                    response.sendRedirect(redirectURI);
                }
            } catch (Exception e) {
                if (e instanceof OAuthProblemException) {
                    try {
                        oAuthResponse = OAuthASResponse
                                .errorResponse(HttpServletResponse.SC_FOUND)
                                .error((OAuthProblemException) e)
                                .location(((OAuthProblemException) e).getRedirectUri())
                                .buildQueryMessage();
                    } catch (OAuthSystemException ex) {
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
                    try {
                        response.sendRedirect(oAuthResponse.getLocationUri());
                    } catch (IOException e) {
                        if (logger.isErrorEnabled()) {
                            logger.error(ExceptionUtils.getStackTrace(e));
                        }
                    }
                }
            }

		}, AuthorizationServlet.class);
    }
}
