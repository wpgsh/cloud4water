package net.wapwag.authn.ui;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import junit.framework.TestCase;
import net.wapwag.authn.AuthenticationServiceImpl;
import net.wapwag.authn.dao.UserDao;
import net.wapwag.authn.dao.UserDaoException;
import net.wapwag.authn.dao.model.AccessToken;
import net.wapwag.authn.dao.model.RegisteredClient;
import net.wapwag.authn.dao.model.User;
import net.wapwag.authn.util.OSGIUtil;
import org.apache.commons.codec.binary.Base64;
import org.eclipse.jetty.server.NetworkTrafficServerConnector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.junit.Test;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class AccessTokenServletTest {
	
	private static final int port = 9100;
	
	private static final int maxServerThreads = 4;
	
	private static final int acceptQueueSize = 1;

	private Server server = null;
	
	static Server createServer(int port, int maxServerThreads, int acceptQueueSize, Servlet servlet) {
		QueuedThreadPool threadPool = new QueuedThreadPool();
		threadPool.setMaxThreads(maxServerThreads);
		threadPool.setName("JettyServer");
		threadPool.setStopTimeout(10000);
		threadPool.setDaemon(true);
		
		Server server = new Server(threadPool);
		NetworkTrafficServerConnector connector = new NetworkTrafficServerConnector(server);
		connector.setName("input");
		connector.setPort(port);
		connector.setIdleTimeout(1000);
		connector.setHost("0.0.0.0");
		connector.setStopTimeout(10000);
		connector.setAcceptQueueSize(acceptQueueSize);
		server.addConnector(connector);
		
		ServletHandler handler = new ServletHandler();
		ServletHolder servletHolder = new ServletHolder(servlet);
		handler.addServletWithMapping(servletHolder, "/*");
		server.setHandler(handler);
		
		return server;
	}	
	
	protected final long USER_ID = 1;
	protected final long INTERNAL_CLIENT_ID = 2;
	protected final String CLIENT_ID = "a";
	protected final String CLIENT_SECRET = "b";
	protected final String REDIRECT_URI = "http://localhost";
	protected final String ACCESS_TOKEN = "c";
	protected final String AUTHORIZATION_CODE = "d";
	protected final long EXPIRATION = 0;
	protected final String SCOPE = "scope";
	
	
	protected User createUser1() {
		User user = new User();
		user.setId(USER_ID);
		return user;
	}
	
	protected RegisteredClient createClient1() {
		RegisteredClient client = new RegisteredClient();
		client.setClientId(CLIENT_ID);
		client.setClientSecret(CLIENT_SECRET);
		client.setId(INTERNAL_CLIENT_ID);
		client.setRedirectURI(REDIRECT_URI);
		return client;
	}
	
	protected UserDao createUserDao() {
		return new UserDao() {
			
			private Map<Long, User> users = 
				ImmutableMap.of(USER_ID, createUser1());
			
			private Map<Long, RegisteredClient> clients = 
				ImmutableMap.of(INTERNAL_CLIENT_ID, createClient1());
			
			@Override
			public User getUser(long uid) throws UserDaoException {
				return users.get(uid);
			}

			@Override
			public int saveUser(User user) throws UserDaoException {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public int removeUser(long uid) throws UserDaoException {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public User getUserAvatar(long id) throws UserDaoException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public int saveUserAvatar(User user) throws UserDaoException {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public int removeUserAvatar(long id) throws UserDaoException {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public RegisteredClient getClientByClientId(long clientId) throws UserDaoException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public RegisteredClient getClientByRedirectURI(String redirectURI) throws UserDaoException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public AccessToken getAccessToken(AccessToken accessToken) throws UserDaoException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public AccessToken getAccessTokenByCode(String code) throws UserDaoException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public AccessToken getAccessTokenByUserIdAndClientId(long userId, long clientId) throws UserDaoException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public long saveAccessToken(AccessToken accessToken) throws UserDaoException {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public AccessToken lookupAccessToken(String handle) throws UserDaoException {
				if (ACCESS_TOKEN.equals(handle)) {					
					AccessToken accessToken = new AccessToken();
					accessToken.setAuthrizationCode(AUTHORIZATION_CODE);
					accessToken.setExpiration(EXPIRATION);
					accessToken.setHandle(ACCESS_TOKEN);
					accessToken.setRegisteredClient(clients.get(INTERNAL_CLIENT_ID));
					accessToken.setScope(SCOPE);
					accessToken.setUser(users.get(USER_ID));
					return accessToken;
				} else {
					return null;
				}
			}

			@Override
			public User getUserByName(String userName) throws UserDaoException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public User getUserByEmail(String email) throws UserDaoException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public User updateUserPwd(User user) throws UserDaoException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public <E extends Exception> void tx(ComplexAction<E> action, Class<E> exClass) throws E {
				action.apply();
			}

			@Override
			public <T, E extends Exception> T txExpr(ComplexActionWithResult<T, E> action, Class<E> exClass) throws E {
				return action.apply();
			}
			
		};
	}	
	
//	@Before
	public void setup() throws Exception {
		
		AuthenticationServiceImpl authnService = new AuthenticationServiceImpl();
		authnService.setUserDao(createUserDao());
		
		OSGIUtil.setAuthenticationService(authnService);
		
		AccessTokenServlet servlet = new AccessTokenServlet();
		
		if (port >= 0) {		
			server = createServer(port, maxServerThreads, acceptQueueSize, servlet);
			server.start();
		}
				
	}
	
//	@Test
	public void testError_InvalidClient1() throws Exception {
		URL url = new URL(String.format("http://localhost:%d", port));
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.addRequestProperty("content-type", "application/x-www-form-urlencoded");
		conn.addRequestProperty("authorization", 
			"Basic "+
				Base64.encodeBase64String(String.format("%s:%s", "invalid_client", "invalid_pwd").getBytes()));
		conn.connect();
		OutputStream out = conn.getOutputStream();
		out.write(String.format(
			"grant_type=authorization_code&code=%s&redirect_uri=%s", "c", "invalid_uri").getBytes());
		out.flush();
		int respCode = conn.getResponseCode();
		
		TestCase.assertEquals(401, respCode);
		
		Map<String, Object> result = 
			new Gson().fromJson(new InputStreamReader(conn.getInputStream()), new TypeToken<Map<String, Object>>(){}.getType());
		
		TestCase.assertEquals("invalid_client", result.get("error"));
		
		conn.disconnect();
	}
	
//	@After
	public void complete() throws Exception {
		if (server != null) {
			server.stop();
			server.join();
		}
	}

    private static final String ACCESSTOKEN_CONTEXT_PATH = "http://localhost:8181/authn/access_token";

    private static Map<String, Object> getRequest(boolean auth, String basicAuth, String queryString) throws Exception {
        URL url = new URL(ACCESSTOKEN_CONTEXT_PATH);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.addRequestProperty("content-type", "application/x-www-form-urlencoded");
        if (auth) {
            conn.addRequestProperty("Authorization",
                    "Basic "+
                            Base64.encodeBase64String(basicAuth.getBytes()));
        }
        conn.connect();
        OutputStream out = conn.getOutputStream();
        out.write(queryString.getBytes());
        out.flush();
        int respCode = conn.getResponseCode();
        Map<String, Object> result = null;
        if (respCode == HttpServletResponse.SC_UNAUTHORIZED) {
            result = Maps.newHashMap();
        } else if (respCode == HttpServletResponse.SC_BAD_REQUEST) {
            result = new Gson().fromJson(
                    new InputStreamReader(conn.getErrorStream()),
                    new TypeToken<Map<String, Object>>(){}.getType());
        } else {
            result = new Gson().fromJson(
                    new InputStreamReader(conn.getInputStream()),
                    new TypeToken<Map<String, Object>>(){}.getType());
        }

        result.put("statusCode", respCode);
        conn.disconnect();

        return result;
    }

    //====================== invalid_request ========================
    @Test
    public void testError_BasicAuthFailed() throws Exception {
        noBasicHttpAuth();
    }

    private void noBasicHttpAuth() throws Exception {
        Map<String, Object> resultMap = getRequest(false, null, "");
        TestCase.assertEquals(HttpServletResponse.SC_UNAUTHORIZED, resultMap.get("statusCode"));
    }

    //====================== invalid_request ========================
    @Test
	public void testError_InvalidRequest() throws Exception {
        emptyRequest();
        missingGrantType();
        invalidGrantType();
        missingCode();
        missingRedirectURI();
	}

	private void emptyRequest() throws Exception {
        Map<String, Object> resultMap = getRequest(true, "invalidClientId:invalidSecret", "");
        TestCase.assertEquals(HttpServletResponse.SC_BAD_REQUEST, resultMap.get("statusCode"));
        TestCase.assertEquals("invalid_request", resultMap.get("error"));
    }

    private void missingGrantType() throws Exception {
        Map<String, Object> resultMap = getRequest(true,
                "invalidClientId:invalidSecret",
                        "&code=2b69443d5aebfe6c34a3e90a71e34169" +
                        "&redirect_uri=http://www.baidu.com");
        TestCase.assertEquals(HttpServletResponse.SC_BAD_REQUEST, resultMap.get("statusCode"));
        TestCase.assertEquals("invalid_request", resultMap.get("error"));
    }

    private void invalidGrantType() throws Exception {
        Map<String, Object> resultMap = getRequest(true,
                "invalidClientId:invalidSecret",
                "grant_type=invalid_grant_type" +
                "&code=2b69443d5aebfe6c34a3e90a71e34169" +
                        "&redirect_uri=http://www.baidu.com");
        TestCase.assertEquals(HttpServletResponse.SC_BAD_REQUEST, resultMap.get("statusCode"));
        TestCase.assertEquals("invalid_request", resultMap.get("error"));
    }

    private void missingCode() throws Exception {
        Map<String, Object> resultMap = getRequest(true,
                "invalidClientId:invalidSecret",
                "grant_type=authorization_code" +
                        "&redirect_uri=http://www.baidu.com");
        TestCase.assertEquals(HttpServletResponse.SC_BAD_REQUEST, resultMap.get("statusCode"));
        TestCase.assertEquals("invalid_request", resultMap.get("error"));
    }

    private void missingRedirectURI() throws Exception {
        Map<String, Object> resultMap = getRequest(true,
                "invalidClientId:invalidSecret",
                "grant_type=authorization_code" +
                        "&code=2b69443d5aebfe6c34a3e90a71e34169");
        TestCase.assertEquals(HttpServletResponse.SC_BAD_REQUEST, resultMap.get("statusCode"));
        TestCase.assertEquals("invalid_request", resultMap.get("error"));
    }

    //====================== invalid_client ========================
    @Test
	public void testError_InvalidClient() throws Exception {
        invalidCredential();
	}

    private void invalidCredential() throws Exception {
        Map<String, Object> resultMap = getRequest(true,
                "invalidClientId:invalidSecret",
                "grant_type=authorization_code" +
                        "&code=2b69443d5aebfe6c34a3e90a71e34169" +
                        "&redirect_uri=http://www.baidu.com");
        TestCase.assertEquals(HttpServletResponse.SC_BAD_REQUEST, resultMap.get("statusCode"));
        TestCase.assertEquals("invalid_client", resultMap.get("error"));
    }

    //====================== invalid_grant ========================
    @Test
	public void testError_InvalidGrant() throws Exception {
        invalidAuthorizationCode();
	}

	private void invalidAuthorizationCode() throws Exception {
        Map<String, Object> resultMap = getRequest(true,
                "client1:dfdjfjkdkj23klaa1",
                "grant_type=authorization_code" +
                        "&code=2b69443d5aebfe6c34a3e90a71e34169" +
                        "&redirect_uri=http://www.baidu.com");
        TestCase.assertEquals(HttpServletResponse.SC_BAD_REQUEST, resultMap.get("statusCode"));
        TestCase.assertEquals("invalid_grant", resultMap.get("error"));
    }

    //====================== unauthorized_client ========================

	public void testError_UnauthorizedClient() {
        //There is no scenario at this time
	}

    //====================== unsupported_grant_type ========================

	public void testError_UnsupportedGrantType() {
        //There is no scenario at this time
	}

    //====================== invalid_scope ========================

	public void testError_InvalidScope() {
        //There is no scenario at this time
        //All client now registered is wpg client and has a implicit scope for themself
	}

}
