package net.wapwag.authn.ui;

import net.wapwag.authn.AuthenticationServiceException;
import net.wapwag.authn.dao.model.RegisteredClient;
import net.wapwag.authn.exception.InvalidRequestException;
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
            String accessToken = null;
            String code = request.getParameter("code");
            String clientId = request.getParameter("client_id");
            String clientSecret = request.getParameter("client_secret");
            String redirectURI = request.getParameter("redirect_uri");
            try {
                //check client valid
                client = authnService.getClient(redirectURI);
            } catch (AuthenticationServiceException e) {
                throw new InvalidRequestException("Can't get client.", e);
            }

            if (client == null) {
                throw new ResourceNotFoundException("client not found : " + redirectURI);
            }

            HttpSession session = request.getSession();
            session.setAttribute("authenticated", true);
            session.setAttribute("userId", 1L);
            boolean authenticated = Boolean.valueOf(session.getAttribute("authenticated") + "");
            long userId = Long.valueOf(session.getAttribute("userId") + "");

            if (authenticated) {
                try {
                    accessToken = authnService.getAccessToken(userId, client.getId(), clientSecret, code, redirectURI);
                } catch (AuthenticationServiceException e) {
                    throw new InvalidRequestException("Can't get access token.", e);
                }
                try {
                    response.getWriter().write("{access_token:" + accessToken + "}");
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            } else {
                try {
                    response.sendRedirect("/login.jsp");
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            }
        }, AccessTokenServlet.class);
    }

}
