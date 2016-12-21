package net.wapwag.authn.ui;

import com.google.gson.Gson;
import net.wapwag.authn.model.UserView;
import net.wapwag.authn.util.OSGIUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.oltu.oauth2.common.OAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * User info servlet.
 * Created by Administrator on 2016/7/14.
 */
@SuppressWarnings("Duplicates")
@WebServlet(urlPatterns = "/userinfo", name = "Authn_UserInfoServlet")
public class UserInfoServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(UserInfoServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        OSGIUtil.useAuthenticationService(authnService -> {
            try {
                String tokenHeader = request.getHeader(OAuth.HeaderType.AUTHORIZATION);
                if (StringUtils.isNotBlank(tokenHeader) && tokenHeader.contains("Bearer ")) {
                    String token = tokenHeader.replace("Bearer ", "");

                    UserView user = authnService.getUserInfo(token);

                    if (user != null) {
                        response.setContentType("application/json");
                        PrintWriter writer = response.getWriter();
                        writer.write(new Gson().toJson(user));
                    } else {
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                        response.setHeader(OAuth.HeaderType.WWW_AUTHENTICATE, "error=\"invalid_token\"");
                    }
                } else {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setHeader(OAuth.HeaderType.WWW_AUTHENTICATE, "realm=\"authn\"");
                }
            } catch (Exception e) {
                logger.error(ExceptionUtils.getStackTrace(e));
            }
        }, UserInfoServlet.class);
    }

}
