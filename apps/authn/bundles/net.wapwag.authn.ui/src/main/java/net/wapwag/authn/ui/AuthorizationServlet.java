package net.wapwag.authn.ui;

import net.wapwag.authn.util.OSGIUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.error.OAuthError;
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
import java.net.URLEncoder;
import java.util.Set;

import static net.wapwag.authn.ui.AuthnConstant.AUTHN_ERROR_PATH;

/**
 * http://localhost:8181/authn/authorize
 * ?response_type=code
 * &client_id=client1
 * &redirect_uri=http://www.baidu.com
 * &state=wapwag
 * &scope=1&scope=2&scope=3
 * Authorization servlet.
 */
@SuppressWarnings("Duplicates")
@WebServlet(urlPatterns = "/authorize", name = "AuthorizationServlet")
public class AuthorizationServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(AuthorizationServlet.class);

    /**
     * The path for /authorize.
     */
    private static final String AUTHORIZE_PATH = "/authn/login?return_to=%s";

	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        OSGIUtil.useAuthenticationService(authnService -> {
            OAuthResponse oAuthResponse = null;
            String redirectURI = null;

            HttpSession session = request.getSession();

            Boolean authenticated = (Boolean) session.getAttribute("authenticated");

	        try {

                OAuthAuthzRequest oauthRequest = new OAuthAuthzRequest(request);

                String code;
                String clientId = oauthRequest.getClientId();
                String state = oauthRequest.getState();
                redirectURI = oauthRequest.getRedirectURI();
                Set<String> scopes = oauthRequest.getScopes();

                if (StringUtils.isBlank(state)) {
                    throw OAuthProblemException.error(OAuthError.CodeResponse.INVALID_REQUEST, "require state field");
                }

                if (authenticated != null && authenticated) {

                    long userId = (Long) session.getAttribute("userId");
                    String originalState = (String) session.getAttribute("oauthState");

                    if (!state.equals(originalState)) {
                        throw OAuthProblemException.error(OAuthError.CodeResponse.INVALID_REQUEST, "invalid state");
                    }

                    //Get authorization code.
                    code = authnService.getAuthorizationCode(userId, clientId, redirectURI, scopes);

                    oAuthResponse = OAuthASResponse
                            .authorizationResponse(request, HttpServletResponse.SC_FOUND)
                            .setCode(code)
                            .location(oauthRequest.getRedirectURI())
                            .buildQueryMessage();
                    response.sendRedirect(oAuthResponse.getLocationUri());

                } else {
                    String pathBuilder = request.getRequestURI() + "?" + request.getQueryString();

                    session.setAttribute("oauthState", state);

                    //build return_to uri if not login.
                    redirectURI = String.format(AUTHORIZE_PATH, URLEncoder.encode(pathBuilder, "UTF-8"));
                    response.sendRedirect(redirectURI);
                }
                oAuthResponse = null;
            } catch (Exception e) {
                if (e instanceof OAuthProblemException) {
                    try {
                        redirectURI = StringUtils.isNotBlank(redirectURI) ? redirectURI : ((OAuthProblemException) e).getRedirectUri();
                        oAuthResponse = OAuthASResponse
                                .errorResponse(HttpServletResponse.SC_FOUND)
                                .error((OAuthProblemException) e)
                                .location(redirectURI == null ? AUTHN_ERROR_PATH.value() : redirectURI)
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
