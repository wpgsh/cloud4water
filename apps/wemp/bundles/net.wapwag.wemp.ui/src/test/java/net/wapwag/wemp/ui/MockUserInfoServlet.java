package net.wapwag.wemp.ui;

import com.google.gson.Gson;
import net.wapwag.wemp.model.AuthnUser;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * WEMP return Servlet
 * Created by Administrator on 2016/11/13.
 */
public class MockUserInfoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AuthnUser authnUser = new AuthnUser();
        authnUser.setId(1L);
        authnUser.setSub("wemp");
        authnUser.setEnabled("1");
        authnUser.setName("test1");
        authnUser.setUsername("test1");
        authnUser.setPhone2("15850817392");
        authnUser.setAvatar("avatar");
        authnUser.setEmail("jiangzehu@163.com");
        authnUser.setHomepage("http://www.baidu.com");
        authnUser.setPhone1("15850817392");
        response.getWriter().write(new Gson().toJson(authnUser));
    }
}
