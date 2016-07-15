package net.wapwag.authn.ui;

import net.wapwag.authn.AuthenticationService;
import net.wapwag.authn.AuthenticationServiceException;
import net.wapwag.authn.dao.model.RegisteredClient;
import net.wapwag.authn.exception.InvalidRequestException;
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
 * Access token servlet.
 * Created by Administrator on 2016/7/14.
 */
@WebServlet(urlPatterns = "/access_token", name = "AccessTokenServlet")
public class AccessTokenServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(AccessTokenServlet.class);

    @Reference
    private AuthenticationService authnService;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accessToken = null;
        String code = request.getParameter("code");
        String clientId = request.getParameter("client_id");
        String clientSecret = request.getParameter("client_secret");
        String redirectURI = request.getParameter("redirect_uri");
        String scope = request.getParameter("scope");
        RegisteredClient client = null;
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
        } else if (clientSecret != null) {
            throw new InvalidRequestException("require client secret");
        } else if (clientSecret.equals(client.getClientSecret())) {
            throw new InvalidRequestException("Invalid client secret");
        }

        HttpSession session = request.getSession();
        boolean authenticated = Boolean.valueOf(session.getAttribute("authenticated") + "");
        long userId = Long.valueOf(session.getAttribute("userId") + "");
        try {
            accessToken = authnService.getAccessToken(userId, client.getId(), clientSecret, code, redirectURI);
        } catch (AuthenticationServiceException e) {
            e.printStackTrace();
            logger.error("can not get acess_token : " + e);
        }

        response.getWriter().write("{access_token:" + accessToken + "}");
    }

}
