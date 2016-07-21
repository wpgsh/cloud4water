package net.wapwag.authn.ui;

import net.wapwag.authn.AuthenticationServiceException;
import net.wapwag.authn.dao.model.RegisteredClient;
import net.wapwag.authn.util.OSGIUtil;
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

/**
 * /authorize?
 * response_type=code&
 * redirect_uri=https://swms.cloud4water.com/auth/github/return&scope=user:email&state=wpg/watersupply&client_id=30958fa5da6191a4828b
 * /authorize?
 * client_id=client1&
 * redirect_uri=http://www.baidu.com&scope=1&scope=2&scope=3
 * http://localhost:8181/authn/authorize?response_type=code&client_id=client1&redirect_uri=http://www.baidu.com&scope=1&scope=2&scope=3
 * Authorization servlet.
 */
@WebServlet(urlPatterns = "/authorize", name = "AuthorizationServlet")
public class AuthorizationServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(AuthorizationServlet.class);

    /**
     * The path for /authorize.
     */
    private static final String AUTHORIZE_PATH = "/authn/login?client_id=%s&return_to=%s?response_type=%s&redirect_uri=%s&client_id=%s&scope=%s";

	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        OSGIUtil.useAuthenticationService(authnService  -> {
        	RegisteredClient client = null;
            OAuthResponse oAuthResponse = null;

            HttpSession session = request.getSession();
            boolean authenticated = Boolean.valueOf(session.getAttribute("authenticated") + "");

	        try {

                OAuthAuthzRequest oauthRequest = new OAuthAuthzRequest(request);

                String scope = request.getParameter("scope");
                String code = null;
                String type = oauthRequest.getResponseType();
                String clientId = oauthRequest.getClientId();
                String redirectURI = oauthRequest.getRedirectURI();
                Set<String> scopes = oauthRequest.getScopes();

                if (authenticated) {
                    long userId = Long.valueOf(session.getAttribute("userId") + "");

                    //check client valid
                    client = authnService.getClient(redirectURI);

                    //Get authorization code.
                    code = authnService.getAuthorizationCode(userId, client.getId(), redirectURI, null);

                    oAuthResponse = OAuthASResponse
                            .authorizationResponse(request, HttpServletResponse.SC_FOUND)
                            .setCode(code)
                            .location(oauthRequest.getRedirectURI())
                            .buildQueryMessage();
                    response.sendRedirect(oAuthResponse.getLocationUri());

                } else {
                    redirectURI = String.format(AUTHORIZE_PATH, clientId, "/authn/authorize",
                            type, redirectURI, clientId, scope);
                    response.sendRedirect(redirectURI);
                }
            } catch (OAuthProblemException e) {
                try {
                    oAuthResponse = OAuthASResponse
                            .errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                            .error(e)
                            .buildJSONMessage();
                    response.setStatus(oAuthResponse.getResponseStatus());
                    response.getWriter().write(oAuthResponse.getBody());
                } catch (OAuthSystemException e1) {
                    try {
                        oAuthResponse = OAuthASResponse
                                .errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                                .error(e)
                                .buildJSONMessage();
                        response.setStatus(oAuthResponse.getResponseStatus());
                        response.getWriter().write(oAuthResponse.getBody());
                    } catch (Exception e11) {

                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            } catch (OAuthSystemException e) {
                try {
                    oAuthResponse = OAuthASResponse
                            .errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                            .setError("Invalid register identifier")
                            .buildJSONMessage();
                    response.setStatus(oAuthResponse.getResponseStatus());
                    response.getWriter().write(oAuthResponse.getBody());
                } catch (Exception e11) {

                }
            } catch (AuthenticationServiceException e) {
                try {
                    oAuthResponse = OAuthASResponse
                            .errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                            .setError(OAuthError.TokenResponse.INVALID_CLIENT)
                            .setErrorDescription("Error client identifier")
                            .buildJSONMessage();
                    response.setStatus(oAuthResponse.getResponseStatus());
                    response.getWriter().write(oAuthResponse.getBody());
                } catch (Exception e11) {

                }
            } catch (IOException e) {
                e.printStackTrace();
            }

		}, AuthorizationServlet.class);
    }

}
