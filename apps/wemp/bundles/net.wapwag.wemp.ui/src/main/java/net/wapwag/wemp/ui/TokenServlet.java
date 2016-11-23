package net.wapwag.wemp.ui;

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

// See https://github.com/wpgsh/cloud4water/wiki/Authentication-Application#invocation-of-access-token-endpoint
@WebServlet(urlPatterns = "/token", name = "WEMP_TokenServlet")
public class TokenServlet extends HttpServlet {

	private static final Logger logger = LoggerFactory.getLogger(TokenServlet.class);

	@SuppressWarnings("Duplicates")
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		OSGIUtil.useWaterEquipmentService(waterEquipmentService -> {
			String redirectURI;
			OAuthResponse oAuthResponse = null;

			try {

				OAuthTokenRequest oAuthTokenRequest = new OAuthTokenRequest(request);

				String clientId = oAuthTokenRequest.getClientId();
				String code = oAuthTokenRequest.getCode();
				String clientSecret = oAuthTokenRequest.getClientSecret();
				redirectURI = oAuthTokenRequest.getRedirectURI();

				String accessToken = waterEquipmentService.getAccessToken(clientId, clientSecret, code, redirectURI);

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
		}, TokenServlet.class);
	}


}
