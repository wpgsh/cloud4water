package net.wapwag.authn.ui;

import net.wapwag.authn.AuthenticationService;
import net.wapwag.authn.AuthenticationServiceException;
import net.wapwag.authn.dao.model.RegisteredClient;
import net.wapwag.authn.exception.ResourceNotFoundException;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Authorization servlet.
 * Created by Administrator on 2016/7/14.
 */
@Component(service = HttpServlet.class, property = {"httpContext.id=authn"})
@WebServlet(urlPatterns = "/authorize", name = "AuthorizationServlet")
public class AuthorizationServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(AuthorizationServlet.class);

    /**
     * The path for /authorize.
     */
    private static final String AUTHORIZE_PATH = "/authn/login?client_id=%s&return_to=%s&redirect_uri=%s&scope=%s";

    @Reference
    protected AuthenticationService authnService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
                response.sendRedirect(redirectURI);
            }
        } else {
            String scope = request.getParameter("scope");
            redirectURI = String.format(AUTHORIZE_PATH, client.getClientId(),
                    "/authn/authorize", client.getRedirectURI(), scope);
            response.sendRedirect(redirectURI);
        }
    }
}
