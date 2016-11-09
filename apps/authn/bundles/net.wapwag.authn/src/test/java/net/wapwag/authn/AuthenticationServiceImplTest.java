package net.wapwag.authn;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import junit.framework.TestCase;
import net.wapwag.authn.dao.UserDao;
import net.wapwag.authn.dao.UserDaoException;
import net.wapwag.authn.dao.model.AccessToken;
import net.wapwag.authn.dao.model.Image;
import net.wapwag.authn.dao.model.RegisteredClient;
import net.wapwag.authn.dao.model.User;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class AuthenticationServiceImplTest {

	private AuthenticationServiceImpl authnService;
	
	@Before
	public void setUp() {
		authnService = new AuthenticationServiceImpl();
		authnService.userDao = createUserDao();
	}
	
	protected final long USER_ID = 1;
	protected final long INTERNAL_CLIENT_ID = 2;
	protected final String CLIENT_ID = "a";
	protected final String CLIENT_SECRET = "b";
    protected final String CLIENT_VENDOR = "wapwag";
	protected final String REDIRECT_URI = "http://localhost";
	protected final String ACCESS_TOKEN = "c";
	protected final String AUTHORIZATION_CODE = "d";
	protected final long EXPIRATION = Long.MAX_VALUE;
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
		client.setClientVendor(CLIENT_VENDOR);
		client.setId(INTERNAL_CLIENT_ID);
		client.setRedirectURI(REDIRECT_URI);
		return client;
	}

	protected AccessToken createAccessToken1() {
        AccessToken accessToken = new AccessToken();
        accessToken.setAuthrizationCode(AUTHORIZATION_CODE);
        accessToken.setExpiration(EXPIRATION);
        accessToken.setHandle(ACCESS_TOKEN);
        accessToken.setRegisteredClient(createClient1());
        accessToken.setScope(SCOPE);
        accessToken.setUser(createUser1());
        return accessToken;
    }

	protected UserDao createUserDao() {
		return new UserDao() {

			private Map<Long, User> users = 
				ImmutableMap.of(USER_ID, createUser1());
			
			private Map<Long, RegisteredClient> clients = 
				ImmutableMap.of(INTERNAL_CLIENT_ID, createClient1());

            private Map<String, AccessToken> initToken =
                    ImmutableMap.of(AUTHORIZATION_CODE, createAccessToken1());

            private Map<String, AccessToken> tokens = new HashMap<>(initToken);

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
			public RegisteredClient getClientByClientId(long clientId) throws UserDaoException {
				return clients.get(clientId);
			}

			@Override
			public RegisteredClient getClientByRedirectURI(String redirectURI) throws UserDaoException {
			    if (REDIRECT_URI.equals(redirectURI)) {
			        return clients.get(INTERNAL_CLIENT_ID);
                }
				return null;
			}

			@Override
			public AccessToken getAccessToken(AccessToken accessToken) throws UserDaoException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public AccessToken getAccessTokenByCode(String code) throws UserDaoException {
				return tokens.get(code);
			}

			@Override
			public AccessToken getAccessTokenByUserIdAndClientId(long userId, long clientId) throws UserDaoException {
			    for (String key : tokens.keySet()) {
			        AccessToken accessToken = tokens.get(key);
                    if (userId == accessToken.getUser().getId()
                            && clientId == accessToken.getRegisteredClient().getId()) {
                        return accessToken;
                    }
                }
				return null;
			}

			@Override
			public long saveAccessToken(AccessToken accessToken) throws UserDaoException {
			    if (tokens.containsKey(accessToken.getAuthrizationCode())) {
			        return 0;
                } else {
                    tokens.put(accessToken.getAuthrizationCode(), accessToken);
                    return 1;
                }
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

			@Override
			public int saveImg(Image image) throws UserDaoException {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public int deleteImg(String avartarId) throws UserDaoException {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public Image getAvatar(String avartarId) throws UserDaoException {
				// TODO Auto-generated method stub
				return null;
			}
			
		};
	}

	@Test
	public void testLookupAccessToken() throws Exception {
		net.wapwag.authn.model.AccessToken accessToken = authnService.lookupToken(ACCESS_TOKEN);
		TestCase.assertEquals(Long.MAX_VALUE, accessToken.expiration);
		TestCase.assertEquals(ACCESS_TOKEN, accessToken.handle);
		TestCase.assertEquals(ImmutableSet.of(SCOPE), accessToken.scope);
		TestCase.assertEquals(CLIENT_ID, accessToken.clientId);
		TestCase.assertEquals(USER_ID, accessToken.userId);
	}

	@Test
	public void testGetAccessToken() throws Exception {

		String token = authnService.getAccessToken(CLIENT_ID, CLIENT_SECRET, AUTHORIZATION_CODE, REDIRECT_URI);
		TestCase.assertEquals(ACCESS_TOKEN, token);
	}

	@Test
	public void testGetAuthorizationCode() throws Exception {
		String authorizationCode = authnService.getAuthorizationCode(USER_ID, CLIENT_ID, REDIRECT_URI, null);
        TestCase.assertEquals(StringUtils.isNotBlank(authorizationCode), true);
        TestCase.assertEquals(authorizationCode.length(), 32);
	}
	
}
