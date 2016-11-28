package net.wapwag.wemp.ui;

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
import java.util.Set;

import static javax.servlet.http.HttpServletResponse.SC_FOUND;
import static net.wapwag.wemp.WempUtil.encodeBase64String;
import static net.wapwag.wemp.WempUtil.encodeURL;
import static net.wapwag.wemp.ui.WempConstant.AUTHORIZE_PATH;
import static net.wapwag.wemp.ui.WempConstant.WEMP_ERROR_PATH;

@WebServlet(urlPatterns = "/authorize", name = "WEMP_AuthorizeServlet")
public class AuthorizeServlet extends HttpServlet {

	private static final Logger logger = LoggerFactory.getLogger(AuthorizeServlet.class);

    @SuppressWarnings("Duplicates")
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        OSGIUtil.useWaterEquipmentService(waterEquipmentService -> {
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
                    String originalState = (String) session.getAttribute("oauthOriginalState");

                    if (!state.equals(originalState)) {
                        throw OAuthProblemException.error(OAuthError.CodeResponse.INVALID_REQUEST, "invalid state");
                    }

                    //Get authorization code.
                    code = waterEquipmentService.getAuthorizationCode(userId, clientId, redirectURI, scopes);

                    oAuthResponse = OAuthASResponse
                            .authorizationResponse(request, SC_FOUND)
                            .setCode(code)
                            .location(oauthRequest.getRedirectURI())
                            .buildQueryMessage();
                    response.sendRedirect(oAuthResponse.getLocationUri());

                } else {
                    String requestState = encodeBase64String(session.getId() + System.nanoTime());
                    session.setAttribute("wempRedirect", request.getQueryString());
                    session.setAttribute("oauthRequestState", requestState);
                    session.setAttribute("oauthOriginalState", state);

                    //redirect to authn app if there is no security session
                    redirectURI = AUTHORIZE_PATH + String.format("&state=%s&scope=%s", encodeURL(requestState), encodeURL(StringUtils.join(scopes, " ")));

                    response.sendRedirect(redirectURI);
                }

                oAuthResponse = null;
            } catch (Exception e) {
                if (e instanceof OAuthProblemException) {
                    try {
                        redirectURI = StringUtils.isNotBlank(redirectURI) ? redirectURI : ((OAuthProblemException) e).getRedirectUri();
                        oAuthResponse = OAuthASResponse
                                .errorResponse(SC_FOUND)
                                .error((OAuthProblemException) e)
                                .location(redirectURI == null ? WEMP_ERROR_PATH.value() : redirectURI)
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

        }, AuthorizeServlet.class);
    }

	

}
