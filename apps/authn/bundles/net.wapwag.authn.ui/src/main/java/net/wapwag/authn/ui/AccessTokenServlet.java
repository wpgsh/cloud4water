package net.wapwag.authn.ui;

import net.wapwag.authn.AuthenticationServiceException;
import net.wapwag.authn.dao.model.RegisteredClient;
import net.wapwag.authn.util.OSGIUtil;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
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

    private static final String ACCESSTOKEN_PATH = "/access_token?client_id=%s&client_secret=%s&code=%s&redirect_uri=%s";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        OSGIUtil.useAuthenticationService(authnService -> {
            RegisteredClient client = null;
            OAuthResponse oAuthResponse = null;

            try {
                OAuthTokenRequest oAuthTokenRequest = new OAuthTokenRequest(request);

                String code = oAuthTokenRequest.getCode();
                String clientSecret = oAuthTokenRequest.getClientSecret();
                String redirectURI = oAuthTokenRequest.getRedirectURI();
                Set<String> scopes = oAuthTokenRequest.getScopes();

                //check client valid
                client = authnService.getClient(redirectURI);

                String accessToken = authnService.getAccessToken(client.getId(), clientSecret, code, redirectURI);

                oAuthResponse = OAuthASResponse
                        .tokenResponse(HttpServletResponse.SC_OK)
                        .setAccessToken(accessToken)
                        .setExpiresIn("3600")
                        .buildJSONMessage();
                response.setStatus(oAuthResponse.getResponseStatus());
                response.getWriter().write(oAuthResponse.getBody());
            } catch (OAuthSystemException e) {
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
            } catch (AuthenticationServiceException e) {
                try {
                    oAuthResponse = OAuthASResponse
                            .errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                            .setError(OAuthError.ResourceResponse.EXPIRED_TOKEN)
                            .setErrorDescription("The authorization code has expired")
                            .buildJSONMessage();
                    response.setStatus(oAuthResponse.getResponseStatus());
                    response.getWriter().write(oAuthResponse.getBody());
                } catch (Exception e11) {

                }
            } catch (OAuthProblemException e) {
                try {
                    oAuthResponse = OAuthASResponse
                            .errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                            .error(e)
                            .buildJSONMessage();
                } catch (OAuthSystemException e1) {
                    e1.printStackTrace();
                }
                response.setStatus(oAuthResponse.getResponseStatus());
                try {
                    response.getWriter().write(oAuthResponse.getBody());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, AccessTokenServlet.class);
    }

}
