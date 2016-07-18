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
import java.io.UncheckedIOException;

/**
 * /authorize?
 * response_type=code&
 * redirect_uri=https://swms.cloud4water.com/auth/github/return&scope=user:email&state=wpg/watersupply&client_id=30958fa5da6191a4828b
 * /authorize?
 * client_id=client1&
 * redirect_uri=http://www.baidu.com&scope=1&scope=2&scope=3
 * http://localhost:8181/authn/authorize?client_id=client1&redirect_uri=http://www.baidu.com&scope=1&scope=2&scope=3
 * Authorization servlet.
 */
@WebServlet(urlPatterns = "/authorize", name = "AuthorizationServlet")
public class AuthorizationServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(AuthorizationServlet.class);

    /**
     * The path for /authorize.
     */
    private static final String AUTHORIZE_PATH = "/authn/login?client_id=%s&return_to=%s?redirect_uri=%s&client_id=%s&scope=%s";
    
	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        OSGIUtil.useAuthenticationService(authnService  -> {
        	RegisteredClient client = null;
            String code = null;
            String clientId = request.getParameter("client_id");
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
	
	        //check authenticated session exist
	        HttpSession session = request.getSession();
	        boolean authenticated = Boolean.valueOf(session.getAttribute("authenticated") + "");
	        if (authenticated) {
	            long userId = Long.valueOf(session.getAttribute("userId") + "");

	            try {
	                //Get authorization code.
	                code = authnService.getAuthorizationCode(userId, client.getId(), redirectURI, null);
	            } catch (AuthenticationServiceException e) {
	                e.printStackTrace();
	                logger.error("can not get authorization code : " + e);
	            }
	
	            if (code != null) {
	                redirectURI += "?code=" + code;
	                try {
						response.sendRedirect(redirectURI);
					} catch (IOException e) {
						throw new UncheckedIOException(e);
					}
	            }
	        } else {
	            String scope = request.getParameter("scope");
	            redirectURI = String.format(AUTHORIZE_PATH, clientId,
	                    "/authn/authorize", redirectURI, clientId, scope);
	            try {
					response.sendRedirect(redirectURI);
				} catch (IOException e) {
					throw new UncheckedIOException(e);
				}
	        }	
		}, AuthorizationServlet.class);
    }

}
