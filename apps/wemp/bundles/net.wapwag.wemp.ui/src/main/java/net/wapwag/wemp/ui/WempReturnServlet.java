package net.wapwag.wemp.ui;

import net.wapwag.wemp.dao.model.permission.RegisteredClient;
import net.wapwag.wemp.dao.model.permission.User;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * WEMP return Servlet
 * Created by Administrator on 2016/11/13.
 */
@WebServlet(urlPatterns = "/wemp_return", name = "WEMP_Servlet")
public class WempReturnServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String authzCode = request.getParameter("code");

        if (StringUtils.isNotBlank(authzCode)) {
            String token = getAcessToken(authzCode);
            User user = getUserInfo(authzCode);
            RegisteredClient client = new RegisteredClient();
            if ("wapwag".equals(client.getClientVendor())) {
                response.sendRedirect("/authorize");
            } else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "not avaliable to non-wpg app");
            }
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }

    }

    private User getUserInfo(String authzCode) {
        return null;
    }

    private String getAcessToken(String authzCode) {
        return null;
    }
}
