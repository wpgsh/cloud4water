package net.wapwag.wemp.ui;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.wapwag.wemp.WaterEquipmentServiceException;
import net.wapwag.wemp.model.AuthnUser;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.oltu.oauth2.common.OAuth;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static javax.servlet.http.HttpServletResponse.SC_OK;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static net.wapwag.wemp.ui.WempConstant.*;

/**
 * WEMP return Servlet
 * Created by Administrator on 2016/11/13.
 */
@WebServlet(urlPatterns = "/return", name = "WEMP_ReturnServlet")
public class WempReturnServlet extends HttpServlet {

    private static final String AUTHN_USERINFO_PATH = "http://localhost:8181/authn/userinfo";
    private static final String AUTHN_GET_ACCESSTOKEN_PATH = "http://localhost:8181/authn/access_token";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String authzCode = request.getParameter("code");

        if (StringUtils.isNotBlank(authzCode)) {
            String token = getAcessToken(authzCode);

            if (StringUtils.isNotBlank(token)) {
                HttpSession session = request.getSession();
                AuthnUser authnUser = getUserInfo(token);

                if (authnUser != null) {
                    session.setAttribute("authnUser", authnUser);
                    session.setAttribute("userId", authnUser.getId());
                    session.setAttribute("authenticated", true);

                    String wempRedirect = String.valueOf(session.getAttribute("wempRedirect"));

                    if (StringUtils.isNotBlank(wempRedirect)) {
                        request.getRequestDispatcher(String.format("/authorize?%s", wempRedirect)).forward(request, response);
                    } else {
                        response.sendError(SC_UNAUTHORIZED);
                    }
                } else {
                    response.sendError(SC_UNAUTHORIZED);
                }
            } else {
                response.sendError(SC_UNAUTHORIZED);
            }
        } else {
            response.sendError(SC_UNAUTHORIZED);
        }

    }

    @SuppressWarnings("Duplicates")
    private AuthnUser getUserInfo(String token) throws IOException, ServletException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet get = new HttpGet(AUTHN_USERINFO_PATH);
        get.setHeader("Authorization", "Bearer " + token);
        HttpResponse result = client.execute(get);
        if (result.getStatusLine().getStatusCode() == SC_OK) {

            String userJson = EntityUtils.toString(result.getEntity(), "utf-8");
            AuthnUser authnUser = new Gson().fromJson(userJson, AuthnUser.class);

            OSGIUtil.useWaterEquipmentService(waterEquipmentService -> {
                try {
                    waterEquipmentService.saveAuthnUser(authnUser);
                } catch (WaterEquipmentServiceException e) {
                    e.printStackTrace();
                }
            }, TokenServlet.class);

            return authnUser;
        } else {
            return null;
        }
    }

    private String getAcessToken(String authzCode) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(AUTHN_GET_ACCESSTOKEN_PATH);
        post.setHeader("authorization", WEMP_BASIC_ENCODED_CREDENTIAL);
        List<NameValuePair> forms = new ArrayList<>();
        forms.add(new BasicNameValuePair("code", authzCode));
        forms.add(new BasicNameValuePair(OAuth.OAUTH_GRANT_TYPE, "authorization_code"));
        forms.add(new BasicNameValuePair("redirect_uri", WEMP_RETURN_PATH));

        post.setEntity(new UrlEncodedFormEntity(forms));
        HttpResponse result = client.execute(post);

        if (result.getStatusLine().getStatusCode() == SC_OK) {
            String tokenJson = EntityUtils.toString(result.getEntity(), "utf-8");
            if (StringUtils.isNotBlank(tokenJson) && tokenJson.contains("access_token")) {
                JsonObject jsonObject = new JsonParser().parse(tokenJson).getAsJsonObject();
                return jsonObject.get("access_token").getAsString();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}
