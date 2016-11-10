package net.wapwag.authn;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import net.wapwag.authn.Ids.UserId;
import net.wapwag.authn.dao.UserDao;
import net.wapwag.authn.dao.UserDaoException;
import net.wapwag.authn.dao.model.AccessToken;
import net.wapwag.authn.dao.model.AccessTokenId;
import net.wapwag.authn.dao.model.Image;
import net.wapwag.authn.dao.model.RegisteredClient;
import net.wapwag.authn.dao.model.User;
import net.wapwag.authn.model.AccessTokenMapper;
import net.wapwag.authn.model.UserProfile;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationServiceTest {

	private static AuthenticationServiceImpl authenticationServiceImpl;
	
	static final long clientId = 1L;
    static final long objId = 1L;
    static final long userId = 1L;
    static final long groupId = 1L;
    static final long orgId = 1L;
    static final long invalidId = 0L;

    static final int count = 5;
    static final int addCount = 1;
    static final int removeCount = 1;
    static final int updateCount = 1;

    static final String email = "1163525902@qq.com";
    static final String redirectURI = "http://www.baidu.com3";
    static final String handle = "dfa8aea0a6ee11e68635247703754e80";
    static final String encodeHandle = Base64.getEncoder().encodeToString(handle.getBytes());
    static final String code = "dsfjdjfk23skjdsds3";
    static final String clientIdentity = "client3";
    static final String clientSecret = "dfdjfjkdkj23klaa3";
    static final String scope = "user:name user:avatar";
    static final String nonAuthzedscope = "user:email";
    static final String action = "read";
    static final String invalidString = "invalid_string";
    static final String invalidEncodeHanlde = "aW52YWxpZF9zdHJpbmc=";
	
    static Image image = new Image();
    static final User user = new User();
	static final AccessToken accessToken = new AccessToken();
	static final AccessToken accessToken_expired = new AccessToken();
	static final RegisteredClient wpgClient = new RegisteredClient();
	static final RegisteredClient client = new RegisteredClient();
	static final RegisteredClient nonWPGclientWithNoScope = new RegisteredClient();
    static final RegisteredClient nonWPGclientWithAuthzedScope = new RegisteredClient();
    static final RegisteredClient nonWPGclientWithNonAuthzedScope = new RegisteredClient();
	static final AccessTokenId accessTokenId = new AccessTokenId(user, client);
	
	static final AccessToken accessToken_nonWPGclientWithNoScope = new AccessToken();
    static final AccessToken accessToken_nonWPGclientWithAuthzedScope = new AccessToken();
    static final AccessToken accessToken_nonWPGclientWithNonAuthzedScope = new AccessToken();
	
	static final Set<String> scopes = new HashSet<>();
    static final Set<String> nonAuthzedscopes = new HashSet<>();
	
	static{
		image.setId("qwe123");
		image.setImage("testImage".getBytes());
		
		
		user.setId(userId);
        user.setName("test1");
		user.setEmail(email);
		
        wpgClient.setId(clientId);
        wpgClient.setClientId(clientIdentity);
        wpgClient.setClientVendor("wapwag");
        
		client.setId(3l);
        client.setClientId(clientIdentity);
        client.setClientSecret(clientSecret);
		
		accessToken.setAccessTokenId(accessTokenId);
        accessToken.setHandle(handle);
        accessToken.setAuthrizationCode(code);
        accessToken.setExpiration(Long.MAX_VALUE);
        accessToken.setScope(scope);
        
        nonWPGclientWithNoScope.setId(clientId);
        nonWPGclientWithNoScope.setClientId(clientIdentity);

        nonWPGclientWithAuthzedScope.setId(clientId);
        nonWPGclientWithAuthzedScope.setClientId(clientIdentity);

        nonWPGclientWithNonAuthzedScope.setId(clientId);
        nonWPGclientWithNonAuthzedScope.setClientId(clientIdentity);
        
        accessToken_expired.setAccessTokenId(accessTokenId);
        accessToken_expired.setHandle(handle);
        accessToken_expired.setAuthrizationCode(code);
        accessToken_expired.setExpiration(0);
        accessToken_expired.setScope(scope);
        
        scopes.add("user:name");
        scopes.add("user:avatar");

        nonAuthzedscopes.add(nonAuthzedscope);
        
        accessToken_nonWPGclientWithNoScope.setAccessTokenId(accessTokenId);
        accessToken_nonWPGclientWithNoScope.setHandle(handle);
        accessToken_nonWPGclientWithNoScope.setAuthrizationCode(code);
        accessToken_nonWPGclientWithNoScope.setExpiration(Long.MAX_VALUE);

        accessToken_nonWPGclientWithAuthzedScope.setAccessTokenId(accessTokenId);
        accessToken_nonWPGclientWithAuthzedScope.setHandle(handle);
        accessToken_nonWPGclientWithAuthzedScope.setAuthrizationCode(code);
        accessToken_nonWPGclientWithAuthzedScope.setExpiration(Long.MAX_VALUE);
        accessToken_nonWPGclientWithAuthzedScope.setScope(scope);

        accessToken_nonWPGclientWithNonAuthzedScope.setAccessTokenId(accessTokenId);
        accessToken_nonWPGclientWithNonAuthzedScope.setHandle(handle);
        accessToken_nonWPGclientWithNonAuthzedScope.setAuthrizationCode(code);
        accessToken_nonWPGclientWithNonAuthzedScope.setExpiration(Long.MAX_VALUE);

        accessToken_nonWPGclientWithNonAuthzedScope.setScope(nonAuthzedscope);

	}
	
    @Mock
	private UserDao userDao;
	
    @SuppressWarnings("unchecked")
	@Before
    public void before() throws Exception {
        when(userDao
                .txExpr(any(UserDao.ComplexActionWithResult.class), any(Class.class)))
                .then(method -> {
                	@SuppressWarnings("rawtypes")
					UserDao.ComplexActionWithResult a = method.getArgument(0);
                    return a.apply();
                });

        authenticationServiceImpl = new AuthenticationServiceImpl();
        authenticationServiceImpl.setUserDao(userDao);
    }
    
	@Test
	public void testGetUserProfile() throws UserDaoException, AuthenticationServiceException {
		UserId userid = new UserId(userId);
		when(userDao.getUser(userId)).thenReturn(user);
		UserProfile userProfile = authenticationServiceImpl.getUserProfile(userid);
		Assert.assertNotNull(userProfile);
	}
	
	@Test(expected = AuthenticationServiceException.class)
	public void testGetUserProfile_Exception() throws UserDaoException, AuthenticationServiceException {
		UserId userid = new UserId(userId);
		when(userDao.getUser(userId)).thenThrow(UserDaoException.class);
		authenticationServiceImpl.getUserProfile(userid);
	}

	@Test
	public void testLookupToken() throws Exception{
		when(userDao.lookupAccessToken(handle)).thenReturn(accessToken);
		
        AccessTokenMapper accessTokenMapper = authenticationServiceImpl.lookupToken(encodeHandle);

        assertNotNull(accessTokenMapper);
        assertTrue(Long.parseLong(accessTokenMapper.userId) == userId);
        assertTrue(clientIdentity.equals(accessTokenMapper.clientId));
        assertTrue(accessTokenMapper.scope.size() == 2);
	}

	@Test
    public void testLookupToken_CanNotFindToken() throws Exception {
        when(userDao.lookupAccessToken(anyString())).thenReturn(null);

        AccessTokenMapper accessTokenMapper = authenticationServiceImpl.lookupToken(encodeHandle);

        assertNull(accessTokenMapper);
    }
	
	@Test(expected = AuthenticationServiceException.class)
    public void testLookupToken_handle_illegalBase64Format() throws Exception {
        AccessTokenMapper accessTokenMapper = authenticationServiceImpl.lookupToken("invalid_handle");

        assertNull(accessTokenMapper);
    }

    @Test(expected = AuthenticationServiceException.class)
    public void testLookupToken_Exception() throws Exception {
        when(userDao.lookupAccessToken(invalidString)).thenThrow(UserDaoException.class);

        AccessTokenMapper accessTokenMapper = authenticationServiceImpl.lookupToken(invalidEncodeHanlde);

        assertNull(accessTokenMapper);
    }
	
    @Test
    public void testGetAccessToken() throws Exception {
        when(userDao.getClientByRedirectURI(redirectURI)).thenReturn(client);
        when(userDao.getAccessTokenByCode(code)).thenReturn(accessToken);
        when(userDao.saveAccessToken(accessToken)).thenReturn(1L);

        String token = authenticationServiceImpl.getAccessToken(clientIdentity, clientSecret, code, redirectURI);

        assertEquals(handle, token);
    }

    @Test(expected = OAuthProblemException.class)
    public void testGetAccessToken_UnRegistredClient() throws Exception {
        when(userDao.getClientByRedirectURI(redirectURI)).thenReturn(null);

        authenticationServiceImpl.getAccessToken(clientIdentity, clientSecret, code, redirectURI);
    }

    @Test(expected = OAuthProblemException.class)
    public void testGetAccessToken_InvalidClientIdOrClientSecret() throws Exception {
        when(userDao.getClientByRedirectURI(redirectURI)).thenReturn(client);

        authenticationServiceImpl.getAccessToken("invalid_clientIdentity", "invalid_clientSecret", code, redirectURI);
    }

    @Test(expected = OAuthProblemException.class)
    public void testGetAccessToken_InvalidAuthorizationCode() throws Exception {
        when(userDao.getClientByRedirectURI(redirectURI)).thenReturn(client);
        when(userDao.getAccessTokenByCode(anyString())).thenReturn(null);

        authenticationServiceImpl.getAccessToken(clientIdentity, clientSecret,  "invalidAuthorizationCode", redirectURI);
    }

    @Test(expected = OAuthProblemException.class)
    public void testGetAccessToken_ExpiredAuthorizationCode() throws Exception {
        when(userDao.getClientByRedirectURI(redirectURI)).thenReturn(client);
        when(userDao.getAccessTokenByCode(code)).thenReturn(accessToken_expired);

        authenticationServiceImpl.getAccessToken(clientIdentity, clientSecret,  code, redirectURI);
    }

    @Test(expected = OAuthProblemException.class)
    public void testGetAccessToken_Exception() throws Exception {
        when(userDao.getClientByRedirectURI(invalidString)).thenThrow(UserDaoException.class);

        authenticationServiceImpl.getAccessToken(clientIdentity, clientSecret, code, invalidString);
    }
    
    @Test
    public void testGetAuthorizationCode_WPGClient() throws Exception {
        ArgumentCaptor<AccessToken> tokenArgumentCaptor = ArgumentCaptor.forClass(AccessToken.class);

        when(userDao.getClientByRedirectURI(redirectURI)).thenReturn(wpgClient);
        when(userDao.getAccessTokenByUserIdAndClientId(userId, clientId)).thenReturn(accessToken);
        when(userDao.saveAccessToken(any(AccessToken.class))).thenReturn(1L);

        String resultCode = authenticationServiceImpl.getAuthorizationCode(userId, clientIdentity, redirectURI, scopes);

        verify(userDao).saveAccessToken(tokenArgumentCaptor.capture());

        assertNotNull(resultCode);
        assertEquals(tokenArgumentCaptor.getValue().getAuthrizationCode(), resultCode);
    }

    @SuppressWarnings("Duplicates")
    @Test
    public void testGetAuthorizationCode_NonWPGClientWithAuthzedScope() throws Exception {
        ArgumentCaptor<AccessToken> tokenArgumentCaptor = ArgumentCaptor.forClass(AccessToken.class);

        when(userDao.getClientByRedirectURI(redirectURI)).thenReturn(nonWPGclientWithAuthzedScope);
        when(userDao.getAccessTokenByUserIdAndClientId(userId, clientId)).thenReturn(accessToken_nonWPGclientWithAuthzedScope);
        when(userDao.saveAccessToken(any(AccessToken.class))).thenReturn(1L);

        String resultCode = authenticationServiceImpl.getAuthorizationCode(userId, clientIdentity, redirectURI, scopes);

        verify(userDao).saveAccessToken(tokenArgumentCaptor.capture());

        assertNotNull(resultCode);
        assertEquals(tokenArgumentCaptor.getValue().getAuthrizationCode(), resultCode);
    }

    @Test(expected = OAuthProblemException.class)
    public void testGetAuthorizationCode_NonWPGclientWithNoScope() throws Exception {
        when(userDao.getClientByRedirectURI(redirectURI)).thenReturn(nonWPGclientWithNoScope);
        when(userDao.getAccessTokenByUserIdAndClientId(userId, clientId)).thenReturn(accessToken_nonWPGclientWithNoScope);

        authenticationServiceImpl.getAuthorizationCode(userId, clientIdentity, redirectURI, null);
    }

    @SuppressWarnings("Duplicates")
    @Test
    public void testGetAuthorizationCode_NonWPGClientWithNonAuthzedScope() throws Exception {
        ArgumentCaptor<AccessToken> tokenArgumentCaptor = ArgumentCaptor.forClass(AccessToken.class);

        when(userDao.getClientByRedirectURI(redirectURI)).thenReturn(nonWPGclientWithNonAuthzedScope);
        when(userDao.getAccessTokenByUserIdAndClientId(userId, clientId)).thenReturn(accessToken_nonWPGclientWithNonAuthzedScope);
        when(userDao.saveAccessToken(any(AccessToken.class))).thenReturn(1L);

        String resultCode = authenticationServiceImpl.getAuthorizationCode(userId, clientIdentity, redirectURI, nonAuthzedscopes);

        verify(userDao).saveAccessToken(tokenArgumentCaptor.capture());

        assertNotNull(resultCode);
        assertEquals(tokenArgumentCaptor.getValue().getAuthrizationCode(), resultCode);
    }


    @Test(expected = OAuthProblemException.class)
    public void testGetAuthorizationCode_Exception() throws Exception {
        when(userDao.getClientByRedirectURI(invalidString)).thenThrow(UserDaoException.class);

        authenticationServiceImpl.getAuthorizationCode(userId, clientIdentity, invalidString, scopes);
    }

    @Test(expected = OAuthProblemException.class)
    public void testGetAuthorizationCode_SaveFailed() throws Exception {
        when(userDao.getClientByRedirectURI(redirectURI)).thenReturn(client);
//        when(userDao.getAccessTokenByUserIdAndClientId(userId, clientId)).thenReturn(accessToken);
        when(userDao.saveAccessToken(any(AccessToken.class))).thenReturn(0L);

        authenticationServiceImpl.getAuthorizationCode(userId, clientIdentity, redirectURI, scopes);
    }

    @Test(expected = OAuthProblemException.class)
    public void testGetAuthorizationCode_ErrorClientCredential() throws Exception {
        when(userDao.getClientByRedirectURI(redirectURI)).thenReturn(null);

        authenticationServiceImpl.getAuthorizationCode(userId, clientIdentity, redirectURI, scopes);
    }

	@Test
	public void testGetClient() throws AuthenticationServiceException, UserDaoException {
		when(userDao.getClientByRedirectURI(redirectURI)).thenReturn(client);
		
		RegisteredClient client = authenticationServiceImpl.getClient(redirectURI);
		
		assertEquals("client3", client.getClientId());
	}
	
	@Test(expected = AuthenticationServiceException.class)
	public void testGetClient_Exception() throws AuthenticationServiceException, UserDaoException {
		when(userDao.getClientByRedirectURI(redirectURI)).thenThrow(UserDaoException.class);
		
		authenticationServiceImpl.getClient(redirectURI);
	}
	
	@Test
	public void testGetUser() throws AuthenticationServiceException, UserDaoException {
		when(userDao.getUser(userId)).thenReturn(user);
		User user = authenticationServiceImpl.getUser(userId);
		assertNotNull(user);
	}
	
	@Test(expected = AuthenticationServiceException.class)
	public void testGetUser_Exception() throws AuthenticationServiceException, UserDaoException {
		when(userDao.getUser(userId)).thenThrow(UserDaoException.class);
		authenticationServiceImpl.getUser(userId);
	}
	
	@Test
	public void testSaveUser() throws UserDaoException, AuthenticationServiceException {
		when(userDao.saveUser(user)).thenReturn(1);
		int count = authenticationServiceImpl.saveUser(user);
		assertEquals(1, count);
	}
	
	@Test(expected = AuthenticationServiceException.class)
	public void testSaveUser_Exception() throws UserDaoException, AuthenticationServiceException {
		when(userDao.saveUser(user)).thenThrow(UserDaoException.class);
		authenticationServiceImpl.saveUser(user);
	}
	
	
	@Test
	public void testRemoveUser() throws UserDaoException, AuthenticationServiceException {
		when(userDao.removeUser(userId)).thenReturn(1);
		int count = authenticationServiceImpl.removeUser(userId);
		assertEquals(1, count);
	}
	
	@Test(expected = AuthenticationServiceException.class)
	public void testRemoveUser_Exception() throws UserDaoException, AuthenticationServiceException {
		when(userDao.removeUser(userId)).thenThrow(UserDaoException.class);
		authenticationServiceImpl.removeUser(userId);
	}
	
	@Test
	public void testGetUserByName() throws UserDaoException, AuthenticationServiceException {
		when(userDao.getUserByName("test1")).thenReturn(user);
		User user = authenticationServiceImpl.getUserByName("test1");
		assertEquals("test1", user.getName());
	}
	
	@Test(expected = AuthenticationServiceException.class)
	public void testGetUserByName_Exception() throws UserDaoException, AuthenticationServiceException {
		when(userDao.getUserByName("test1")).thenThrow(UserDaoException.class);
		authenticationServiceImpl.getUserByName("test1");
	}

	@Test
	public void testGetUserByEmail() throws AuthenticationServiceException, UserDaoException {
		when(userDao.getUserByEmail(email)).thenReturn(user);
		User user = authenticationServiceImpl.getUserByEmail(email);
		assertEquals(email, user.getEmail());
	}
	
	@Test
	public void testUpdateUserPwd() throws UserDaoException, AuthenticationServiceException {
		when(userDao.updateUserPwd(user)).thenReturn(user);
		assertNotNull(authenticationServiceImpl.updateUserPwd(user));
	}
	
	@Test(expected = AuthenticationServiceException.class)
	public void testUpdateUserPwd_Exception() throws UserDaoException, AuthenticationServiceException {
		when(userDao.updateUserPwd(user)).thenThrow(UserDaoException.class);
		authenticationServiceImpl.updateUserPwd(user);
	}

	@Test
	public void testSaveImg() throws UserDaoException, AuthenticationServiceException {
		when(userDao.saveImg(image)).thenReturn(1);
		int count = authenticationServiceImpl.saveImg(image);
		assertEquals(1, count);
	}
	
	@Test(expected = AuthenticationServiceException.class)
	public void testSaveImg_Exception() throws UserDaoException, AuthenticationServiceException {
		when(userDao.saveImg(image)).thenThrow(UserDaoException.class);
		authenticationServiceImpl.saveImg(image);
	}

	@Test
	public void testDeleteImg() throws UserDaoException, AuthenticationServiceException {
		when(userDao.deleteImg("1")).thenReturn(1);
		int count = authenticationServiceImpl.deleteImg("1");
		assertEquals(1, count);
	}
	
	@Test(expected = AuthenticationServiceException.class)
	public void testDeleteImg_Exception() throws UserDaoException, AuthenticationServiceException {
		when(userDao.deleteImg("1")).thenThrow(UserDaoException.class);
		authenticationServiceImpl.deleteImg("1");
	}
	
	@Test
	public void testGetAvatar() throws AuthenticationServiceException, UserDaoException {
		when(userDao.getAvatar("1")).thenReturn(image);
		Image image = authenticationServiceImpl.getAvatar("1");
		assertNotNull(image);
	}
	
	@Test(expected = AuthenticationServiceException.class)
	public void testGetAvatar_Exception() throws AuthenticationServiceException, UserDaoException {
		when(userDao.getAvatar("1")).thenThrow(UserDaoException.class);
		authenticationServiceImpl.getAvatar("1");
	}

}
