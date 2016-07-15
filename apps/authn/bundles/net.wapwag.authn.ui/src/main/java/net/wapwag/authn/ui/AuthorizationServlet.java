package net.wapwag.authn.ui;

import net.wapwag.authn.AuthenticationService;
import net.wapwag.authn.AuthenticationServiceException;
import net.wapwag.authn.dao.model.RegisteredClient;
import net.wapwag.authn.exception.ResourceNotFoundException;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
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
import java.util.function.Consumer;

/**
 * Authorization servlet.
 */
@WebServlet(urlPatterns = "/authorize", name = "AuthorizationServlet")
public class AuthorizationServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(AuthorizationServlet.class);

    /**
     * The path for /authorize.
     */
    private static final String AUTHORIZE_PATH = "/authn/login?client_id=%s&return_to=%s&redirect_uri=%s&scope=%s";
    
    private void useAuthenticationService(Consumer<AuthenticationService> fn) throws ServletException {
    	BundleContext ctx = FrameworkUtil.getBundle(AuthorizationServlet.class).getBundleContext();
    	ServiceReference<AuthenticationService> reference = ctx.getServiceReference(AuthenticationService.class);
    	AuthenticationService authenticationService = ctx.getService(reference);
    	
    	if (authenticationService == null) {
    		throw new ServletException("AuthenticationService reference not bound");
    	} else {
    		try {
    			fn.accept(authenticationService);
    		} catch (Throwable e) {
    			throw new ServletException("Error processing request", e);
    		}
    		finally {
    			ctx.ungetService(reference);    			
    		}
    	}
    }
          
	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        useAuthenticationService(authnService -> {
        	RegisteredClient client = null;
            String code = null;
            String redirectURI = request.getParameter("redirect_uri");
            
			logger.info("AuthenticationService ------->" + authnService);
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
	        logger.info("--------> AuthorizationServlet" + authenticated);
	        if (authenticated) {
	            long userId = Long.valueOf(session.getAttribute("userId") + "");
	            long clientId = client.getId();
	
	            try {
	                //Get authorization code.
	                code = authnService.getAuthorizationCode(userId, clientId, redirectURI, null);
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
	            redirectURI = String.format(AUTHORIZE_PATH, client.getClientId(),
	                    "/authn/authorize", client.getRedirectURI(), scope);
	            try {
					response.sendRedirect(redirectURI);
				} catch (IOException e) {
					throw new UncheckedIOException(e);
				}
	        }	
		});
    }
}
