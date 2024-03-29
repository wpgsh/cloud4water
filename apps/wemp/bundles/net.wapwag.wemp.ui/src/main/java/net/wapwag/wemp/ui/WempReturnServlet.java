package net.wapwag.wemp.ui;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.wapwag.wemp.WaterEquipmentServiceException;
import net.wapwag.wemp.model.AuthnUser;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import static net.wapwag.wemp.WempUtil.encodeBase64String;
import static net.wapwag.wemp.ui.WempConstant.*;

/**
 * WEMP return Servlet
 * Created by Administrator on 2016/11/13.
 */
@WebServlet(urlPatterns = "/return", name = "WEMP_ReturnServlet")
public class WempReturnServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(WempReturnServlet.class);

    private static final String WEMP_BAISC_ORIGIN = String.format("%s:%s", WEMP_ID.value(), WEMP_SECRET.value());

    private static final String WEMP_BAISC_ENCODED_ORIGIN = encodeBase64String(WEMP_BAISC_ORIGIN);

    private final String WEMP_BASIC_ENCODED_CREDENTIAL = String.format("Basic %s", WEMP_BAISC_ENCODED_ORIGIN);

    private static final String AUTHN_GET_ACCESSTOKEN_PATH = "http://%s:%s/authn/access_token";
    private static final String AUTHN_USERINFO_PATH = "http://%s:%s/authn/userinfo";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        OSGIUtil.useWaterEquipmentService(waterEquipmentService -> {
            try {
                String authzCode = request.getParameter("code");
                String serverIp = "localhost";
                int serverPort = request.getLocalPort();

                if (StringUtils.isNotBlank(authzCode)) {
                    String authnTokenPath = String.format(AUTHN_GET_ACCESSTOKEN_PATH, serverIp, serverPort);
                    String token = getAcessToken(authzCode, authnTokenPath);

                    if (StringUtils.isNotBlank(token)) {
                        HttpSession session = request.getSession();
                        String authnUserInfoPath = String.format(AUTHN_USERINFO_PATH, serverIp, serverPort);

                        AuthnUser authnUser = getUserInfo(token, authnUserInfoPath);

                        if (authnUser != null && WEMP_ID.value().equals(authnUser.getSub())) {

                            session.setAttribute("authnUser", authnUser);
                            session.setAttribute("userId", authnUser.getId());
                            session.setAttribute("authenticated", true);

                            String wempRedirect = (String) session.getAttribute("wempRedirect");

                            if (StringUtils.isNotBlank(wempRedirect)) {
                                response.sendRedirect(String.format("/wemp/authorize?%s", wempRedirect));
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
            } catch (Exception e) {
                logger.error(ExceptionUtils.getStackTrace(e));
            }
        }, UserInfoServlet.class);

    }

    @SuppressWarnings("Duplicates")
    private AuthnUser getUserInfo(String token, String path) throws IOException, ServletException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet get = new HttpGet(path);
        get.setHeader(OAuth.HeaderType.AUTHORIZATION, "Bearer " + token);
        HttpResponse result = client.execute(get);
        if (result.getStatusLine().getStatusCode() == SC_OK) {

            String userJson = EntityUtils.toString(result.getEntity(), "utf-8");
            AuthnUser authnUser = new Gson().fromJson(userJson, AuthnUser.class);

            OSGIUtil.useWaterEquipmentService(waterEquipmentService -> {
                try {
                    waterEquipmentService.saveAuthnUser(authnUser);
                } catch (WaterEquipmentServiceException e) {
                    if (logger.isErrorEnabled()) {
                        logger.error(ExceptionUtils.getStackTrace(e));
                    }
                }
            }, TokenServlet.class);

            return authnUser;
        } else {
            return null;
        }
    }

    private String getAcessToken(String authzCode, String path) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(path);
        post.setHeader(OAuth.HeaderType.AUTHORIZATION, WEMP_BASIC_ENCODED_CREDENTIAL);
        List<NameValuePair> forms = new ArrayList<>();
        forms.add(new BasicNameValuePair("code", authzCode));
        forms.add(new BasicNameValuePair(OAuth.OAUTH_GRANT_TYPE, "authorization_code"));
        forms.add(new BasicNameValuePair("redirect_uri", WEMP_RETURN_PATH.value()));

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
