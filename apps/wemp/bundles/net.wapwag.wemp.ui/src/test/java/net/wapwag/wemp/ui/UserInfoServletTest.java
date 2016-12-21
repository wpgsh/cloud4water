package net.wapwag.wemp.ui;

import com.google.common.collect.ImmutableSet;
import com.google.gson.reflect.TypeToken;
import net.wapwag.wemp.WaterEquipmentServiceImpl;
import net.wapwag.wemp.dao.WaterEquipmentDao;
import net.wapwag.wemp.dao.model.permission.AccessToken;
import net.wapwag.wemp.dao.model.permission.AccessTokenId;
import net.wapwag.wemp.dao.model.permission.RegisteredClient;
import net.wapwag.wemp.dao.model.permission.User;
import net.wapwag.wemp.model.AccessTokenMapper;
import net.wapwag.wemp.model.AuthnUser;
import org.junit.Test;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserInfoServletTest extends BaseServletTest {

	private static final int port = 9100;

	private static final int maxServerThreads = 10;

	private static final int acceptQueueSize = 1;

    private static final String token = "token";
    private static final String invalidToken = "invalidToken";
    private static final AuthnUser authnUser;
    private static final AccessToken accessToken = new AccessToken();

    static {
        authnUser = new AuthnUser();
        authnUser.setSub("wemp");
        authnUser.setId(1L);
        authnUser.setEnabled("1");
        authnUser.setName("test1");
        authnUser.setUsername("test1");
        authnUser.setPhone2("15850817392");
        authnUser.setAvatar("avatar");
        authnUser.setEmail("jiangzehu@163.com");
        authnUser.setHomepage("http://www.baidu.com");
        authnUser.setPhone1("15850817392");

        User user = new User();
        user.setId(1L);
        RegisteredClient client = new RegisteredClient();
        client.setId(1L);
        client.setClientId("clientId");
        AccessTokenId accessTokenId = new AccessTokenId(user, client);

        accessToken.setAccessTokenId(accessTokenId);
        accessToken.setHandle(token);
        accessToken.setExpiration(Long.MAX_VALUE);
        accessToken.setScope("user:name");
    }

    private WaterEquipmentServiceImpl waterEquipmentService = mock(WaterEquipmentServiceImpl.class);

	public UserInfoServletTest() {
		super(port, maxServerThreads, acceptQueueSize);
	}

    @Override
    protected Filter createFilter() throws Exception {
        return new Filter() {
            @Override
            public void init(FilterConfig filterConfig) throws ServletException {

            }

            @Override
            public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                    throws IOException, ServletException {
                if (request instanceof HttpServletRequest) {
                    HttpServletRequest httpRequest = (HttpServletRequest) request;
                    HttpSession session = httpRequest.getSession();
                    if (session != null && session.getAttribute("authnUser") == null) {
                        session.setAttribute("authnUser", authnUser);
                    }
                }
                request.setCharacterEncoding("UTF-8");
                response.setCharacterEncoding("UTF-8");
                chain.doFilter(request, response);
            }

            @Override
            public void destroy() {

            }
        };
    }

    protected Servlet createServlet() {
		return new UserInfoServlet();
	}

    protected WaterEquipmentDao createWaterEquipmentDao() throws Exception {
        return WaterEquipmentDaoMock.getWaterEquipmentDao();
    }
	
    private static final String USER_INFO_PATH = "http://localhost:" + port + "/wemp/userinfo";

    @Override
    public void setupAuthenticationService() throws Exception {
        OSGIUtil.setWaterEquipmentService(waterEquipmentService);
    }

    @Test
    public void noBasicHttpAuth() throws Exception {
        QueryComponentResponse response = getAcceptQueryComponent(USER_INFO_PATH, false, null, APPLICATION_X_WWW_FORM_URLENCODED, null);

        assertEquals(SC_UNAUTHORIZED, response.responseCode);
    }

    @Test
    public void invalidToken() throws Exception {
        when(waterEquipmentService.lookupToken(invalidToken)).thenReturn(null);

        QueryComponentResponse response = getAcceptQueryComponent(USER_INFO_PATH, true, invalidToken, APPLICATION_X_WWW_FORM_URLENCODED, null);

        assertEquals(SC_UNAUTHORIZED, response.responseCode);
    }

    @Test
    public void noClientId() throws Exception {
        when(waterEquipmentService.lookupToken(token)).thenReturn(
                new AccessTokenMapper(
                        Long.toString(0L),
                        accessToken.getExpiration(),
                        "",
                        accessToken.getHandle(),
                        ImmutableSet.copyOf(
                                Optional.ofNullable(accessToken.getScope()).map(String::trim).map(s -> s.split(" ")).orElse(new String[0]))));

        Type type = new TypeToken<AuthnUser>(){}.getType();
        QueryComponentResponse response = getAcceptQueryComponent(USER_INFO_PATH, true, token, APPLICATION_X_WWW_FORM_URLENCODED, type);

        assertEquals(SC_UNAUTHORIZED, response.responseCode);
    }

    @Test
    public void getUserInfo() throws Exception {
        when(waterEquipmentService.lookupToken(token)).thenReturn(
                new AccessTokenMapper(
                        Long.toString(accessToken.getAccessTokenId().getUser().getId()),
                        accessToken.getExpiration(),
                        accessToken.getAccessTokenId().getRegisteredClient().getClientId(),
                        accessToken.getHandle(),
                        ImmutableSet.copyOf(
                                Optional.ofNullable(accessToken.getScope()).map(String::trim).map(s -> s.split(" ")).orElse(new String[0]))));

        Type type = new TypeToken<AuthnUser>(){}.getType();
        QueryComponentResponse response = getAcceptQueryComponent(USER_INFO_PATH, true, token, APPLICATION_X_WWW_FORM_URLENCODED, type);

        assertEquals(HttpServletResponse.SC_OK, response.responseCode);
        assertTrue(response.body.get("result") instanceof AuthnUser);
    }

}
