package net.wapwag.authn;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import junit.framework.TestCase;
import net.wapwag.authn.dao.UserDao;
import net.wapwag.authn.dao.UserDaoException;
import net.wapwag.authn.dao.model.AccessToken;
import net.wapwag.authn.dao.model.RegisteredClient;
import net.wapwag.authn.dao.model.User;

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
	
}
