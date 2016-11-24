package net.wapwag.authn.ui;

import com.google.gson.reflect.TypeToken;
import net.wapwag.authn.AuthenticationServiceImpl;
import net.wapwag.authn.dao.UserDao;
import net.wapwag.authn.dao.model.User;
import net.wapwag.authn.model.UserView;
import net.wapwag.authn.util.OSGIUtil;
import org.junit.Test;

import javax.servlet.Filter;
import javax.servlet.Servlet;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Type;

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
    private static final UserView userView;

    static {
        User user = new User();
        user.setId(1L);
        user.setEnabled(true);
        user.setName("test1");
        user.setUsername("test1");
        user.setPhone2("15850817392");
        user.setAvatar("avatar");
        user.setEmail("jiangzehu@163.com");
        user.setHomepage("http://www.baidu.com");
        user.setPhone1("15850817392");
        userView = UserView.newInstance(user);
    }

    private  AuthenticationServiceImpl authnService = mock(AuthenticationServiceImpl.class);

	public UserInfoServletTest() {
		super(port, maxServerThreads, acceptQueueSize);
	}

    @Override
    protected Filter createFilter() throws Exception {
        return null;
    }

    protected Servlet createServlet() {
		return new UserInfoServlet();
	}
	
	protected UserDao createUserDao() throws Exception {
		return UserDaoMock.getUserDao();
	}
	
    private static final String USER_INFO_PATH = "http://localhost:"+port+"/authn/userinfo";

    @Override
    public void setupAuthenticationService() throws Exception {
        OSGIUtil.setAuthenticationService(authnService);
    }

    @Test
    public void noBasicHttpAuth() throws Exception {
        QueryComponentResponse response = getAcceptQueryComponent(USER_INFO_PATH, false, null, APPLICATION_X_WWW_FORM_URLENCODED, null);

        assertEquals(SC_UNAUTHORIZED, response.responseCode);
    }

    @Test
    public void invalidToken() throws Exception {
        when(authnService.getUserInfo(invalidToken)).thenReturn(null);

        QueryComponentResponse response = getAcceptQueryComponent(USER_INFO_PATH, true, invalidToken, APPLICATION_X_WWW_FORM_URLENCODED, null);

        assertEquals(SC_UNAUTHORIZED, response.responseCode);
    }

    @Test
    public void getUserInfo() throws Exception {
        when(authnService.getUserInfo(token)).thenReturn(userView);

        Type type = new TypeToken<UserView>(){}.getType();
        QueryComponentResponse response = getAcceptQueryComponent(USER_INFO_PATH, true, token, APPLICATION_X_WWW_FORM_URLENCODED, type);

        assertEquals(HttpServletResponse.SC_OK, response.responseCode);
        assertTrue(response.body.get("result") instanceof UserView);
    }

}
