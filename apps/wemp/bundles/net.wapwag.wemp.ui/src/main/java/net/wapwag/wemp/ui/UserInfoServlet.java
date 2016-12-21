package net.wapwag.wemp.ui;

import com.google.gson.Gson;
import net.wapwag.wemp.model.AccessTokenMapper;
import net.wapwag.wemp.model.AuthnUser;
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
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * User info servlet.
 * Created by Administrator on 2016/7/14.
 */
@SuppressWarnings("Duplicates")
@WebServlet(urlPatterns = "/userinfo", name = "WEMP_UserInfoServlet")
public class UserInfoServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(UserInfoServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        OSGIUtil.useWaterEquipmentService(waterEquipmentService -> {
            try {
                String tokenHeader = request.getHeader(OAuth.HeaderType.AUTHORIZATION);
                if (StringUtils.isNotBlank(tokenHeader) && tokenHeader.contains("Bearer ")) {
                    String token = tokenHeader.replace("Bearer ", "");

                    AccessTokenMapper accessTokenMapper = waterEquipmentService.lookupToken(token);

                    HttpSession session = request.getSession();
                    AuthnUser authnUser = (AuthnUser) session.getAttribute("authnUser");

                    if (authnUser != null
                            && accessTokenMapper != null
                            && StringUtils.isNotBlank(accessTokenMapper.clientId)) {
                        authnUser.setSub(accessTokenMapper.clientId);
                        response.setContentType("application/json");
                        PrintWriter writer = response.getWriter();
                        writer.write(new Gson().toJson(authnUser));
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
