package net.wapwag.authn.ui;

import net.wapwag.authn.dao.UserDao;
import net.wapwag.authn.dao.model.AccessToken;
import net.wapwag.authn.dao.model.AccessTokenId;
import net.wapwag.authn.dao.model.Image;
import net.wapwag.authn.dao.model.RegisteredClient;
import net.wapwag.authn.dao.model.User;

import static org.mockito.Mockito.*;

public class UserDaoMock {
	
	static final long USER_ID = 1;
	static final String userName = "test1";
	static final long INTERNAL_CLIENT_ID = 2;
	static final String CLIENT_ID = "client1";
	static final String CLIENT_SECRET = "dfdjfjkdkj23klaa1";
	static final String REDIRECT_URI = "http://www.baidu.com";
	static final String ACCESS_TOKEN = "c";
	static final String AUTHORIZATION_CODE = "d";
	static final long EXPIRATION = 0;
	static final String SCOPE = "scope";
	
	static final User user = new User();
	static final Image image = new Image();
	static{
		
		image.setId("1");
		image.setImage("test".getBytes());
		
		user.setId(1l);
		user.setEnabled("1");
        user.setName("test1");
        user.setUsername("test1");
        user.setPhone2("15850817392");
		user.setEmail("jiangzehu@163.com");
		user.setAvartarId("1");
		user.setAvatar("avatar");
		user.setEmail("jiangzehu@163.com");
		user.setHomepage("http://www.baidu.com");
		user.setPhone1("15850817392");
		user.setPasswordHash("bff5c1b718386f23ac472d983a02907671f8932d");
		user.setPasswordSalt("1478156919009");
	}
	
	protected static User createUser1() {
//		User user = new User();
//		user.setId(USER_ID);
		return user;
	}
	
	protected static RegisteredClient createClient1() {
		RegisteredClient client = new RegisteredClient();
		client.setClientId(CLIENT_ID);
		client.setClientSecret(CLIENT_SECRET);
		client.setId(INTERNAL_CLIENT_ID);
		client.setRedirectURI(REDIRECT_URI);
		return client;
	}		
	
	protected static AccessToken createAccessToken1(RegisteredClient client1, User user1) {
		AccessToken accessToken = new AccessToken();
		accessToken.setAuthrizationCode(AUTHORIZATION_CODE);
		accessToken.setExpiration(EXPIRATION);
		accessToken.setHandle(ACCESS_TOKEN);
		accessToken.setScope(SCOPE);
//		accessToken.setAccessTokenId(new AccessTokenId());
		return accessToken;
	}
	/**
	 * Provide implementation of tx* methods
	 * in the given mock
	 * 
	 * @param dao
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	static void mockTx(UserDao dao) throws Exception {
		when(dao.txExpr(any(UserDao.ComplexActionWithResult.class), any(Class.class))).
			then(i -> {
				UserDao.ComplexActionWithResult a = i.getArgument(0);
				return a.apply();
			});
		doAnswer(i -> {
			UserDao.ComplexAction a = i.getArgument(0);
			a.apply();
			return null;
		}).when(dao).tx(any(UserDao.ComplexAction.class), any(Class.class));
	}

	public static UserDao getUserDao() throws Exception {		
		User user1 = createUser1();
		RegisteredClient client1 = createClient1();
		AccessToken accessToken1 = createAccessToken1(client1, user1);
		
		UserDao dao = mock(UserDao.class);
		
		when(dao.getUser(USER_ID)).thenReturn(user1);
		when(dao.lookupAccessToken(ACCESS_TOKEN)).thenReturn(accessToken1);
		when(dao.getClientByRedirectURI(REDIRECT_URI)).thenReturn(client1);
        when(dao.saveAccessToken(accessToken1)).thenReturn(1L);
        when(dao.getAccessTokenByCode(AUTHORIZATION_CODE)).thenReturn(accessToken1);
		when(dao.getUserByName(userName)).thenReturn(user);
		when(dao.saveUser(any(User.class))).thenReturn(1);
		when(dao.getAvatar(user1.getAvartarId())).thenReturn(image);
        
		mockTx(dao);
		
		return dao;
	}

}
