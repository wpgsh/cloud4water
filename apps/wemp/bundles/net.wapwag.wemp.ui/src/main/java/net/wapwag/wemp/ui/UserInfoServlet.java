package net.wapwag.wemp.ui;

import com.google.gson.Gson;
import net.wapwag.wemp.model.AuthnUser;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
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
                String tokenHeader = request.getHeader("Authorization");
                if (StringUtils.isNotBlank(tokenHeader) && tokenHeader.contains("Bearer ")) {
                    String token = tokenHeader.replace("Bearer ", "");

                    HttpSession session = request.getSession();
                    AuthnUser authnUser = (AuthnUser) session.getAttribute("authnUser");
                    int result = waterEquipmentService.saveAuthnUser(authnUser);

                    if (result > 0) {
                        response.setContentType("application/json");
                        PrintWriter writer = response.getWriter();
                        writer.write(new Gson().toJson(authnUser));
                    } else {
                        response.setContentType("application/json");
                        response.sendError(HttpServletResponse.SC_FORBIDDEN);
                    }
                } else {
                    response.setContentType("application/json");
                    response.sendError(HttpServletResponse.SC_FORBIDDEN);
                }
            } catch (Exception e) {
                logger.error(ExceptionUtils.getStackTrace(e));
            }
        }, UserInfoServlet.class);
    }

}
