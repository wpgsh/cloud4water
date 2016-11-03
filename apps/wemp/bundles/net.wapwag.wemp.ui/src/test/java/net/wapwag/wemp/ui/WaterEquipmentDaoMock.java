package net.wapwag.wemp.ui;

import net.wapwag.wemp.dao.WaterEquipmentDao;
import net.wapwag.wemp.dao.model.permission.AccessToken;
import net.wapwag.wemp.dao.model.permission.AccessTokenId;
import net.wapwag.wemp.dao.model.permission.RegisteredClient;
import net.wapwag.wemp.dao.model.permission.User;

import static org.mockito.Mockito.*;

@SuppressWarnings("Duplicates")
public class WaterEquipmentDaoMock {
	
	static final long USER_ID = 1;
	static final long INTERNAL_CLIENT_ID = 2;
	static final String CLIENT_ID = "client1";
	static final String CLIENT_SECRET = "dfdjfjkdkj23klaa1";
	static final String REDIRECT_URI = "http://www.baidu.com";
	static final String ACCESS_TOKEN = "c";
	static final String AUTHORIZATION_CODE = "d";
	static final long EXPIRATION = 0;
	static final String SCOPE = "scope";
	
	protected static User createUser1() {
		User user = new User();
		user.setId(USER_ID);
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
		accessToken.setAccessTokenId(new AccessTokenId(user1, client1));
		accessToken.setAuthrizationCode(AUTHORIZATION_CODE);
		accessToken.setExpiration(EXPIRATION);
		accessToken.setHandle(ACCESS_TOKEN);
		accessToken.setScope(SCOPE);
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
	static void mockTx(WaterEquipmentDao dao) throws Exception {
		when(dao.txExpr(any(WaterEquipmentDao.ComplexActionWithResult.class), any(Class.class))).
			then(i -> {
				WaterEquipmentDao.ComplexActionWithResult a = i.getArgument(0);
				return a.apply();
			});
		doAnswer(i -> {
			WaterEquipmentDao.ComplexAction a = i.getArgument(0);
			a.apply();
			return null;
		}).when(dao).tx(any(WaterEquipmentDao.ComplexAction.class), any(Class.class));
	}

	public static WaterEquipmentDao getWaterEquipmentDao() throws Exception {		
		User user1 = createUser1();
		RegisteredClient client1 = createClient1();
		AccessToken accessToken1 = createAccessToken1(client1, user1);
		
		WaterEquipmentDao dao = mock(WaterEquipmentDao.class);
		
		when(dao.getUser(USER_ID)).thenReturn(user1);
		when(dao.lookupAccessToken(ACCESS_TOKEN)).thenReturn(accessToken1);
		when(dao.getClientByRedirectURI(REDIRECT_URI)).thenReturn(client1);
        when(dao.saveAccessToken(accessToken1)).thenReturn(1L);
        when(dao.getAccessTokenByCode(AUTHORIZATION_CODE)).thenReturn(accessToken1);
		
		mockTx(dao);
		
		return dao;
	}

}
