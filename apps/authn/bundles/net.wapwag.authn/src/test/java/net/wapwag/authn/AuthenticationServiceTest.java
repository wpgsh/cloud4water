package net.wapwag.authn;

import static net.wapwag.authn.MockData.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import net.wapwag.authn.dao.model.Image;
import net.wapwag.authn.dao.model.RegisteredClient;
import net.wapwag.authn.dao.model.User;
import net.wapwag.authn.model.AccessTokenMapper;
import net.wapwag.authn.model.UserProfile;
import net.wapwag.authn.model.UserView;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationServiceTest {

	private static AuthenticationServiceImpl authenticationServiceImpl;
	
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
	
	@Test
	public void testGetUserProfile_null() throws UserDaoException, AuthenticationServiceException {
		UserId userid = new UserId(userId);
		when(userDao.getUser(userId)).thenReturn(user_null);
		UserProfile userProfile = authenticationServiceImpl.getUserProfile(userid);
		Assert.assertNull(userProfile);
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
		
        AccessTokenMapper accessTokenMapper = authenticationServiceImpl.lookupToken(handle);

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
	
//	@Test(expected = AuthenticationServiceException.class)
//    public void testLookupToken_handle_illegalBase64Format() throws Exception {
//        AccessTokenMapper accessTokenMapper = authenticationServiceImpl.lookupToken("invalid_handle");
//
//        assertNull(accessTokenMapper);
//    }

    @Test(expected = AuthenticationServiceException.class)
    public void testLookupToken_Exception() throws Exception {
        when(userDao.lookupAccessToken(invalidString)).thenThrow(UserDaoException.class);

        authenticationServiceImpl.lookupToken(invalidString);
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
    public void testGetUserInfo() throws Exception{
    	when(userDao.getUserByAccessToken(handle)).thenReturn(user);
    	UserView userView = authenticationServiceImpl.getUserInfo(handle);
    	assertNotNull(userView);
    }
    
    @Test
    public void testGetUserInfo_null() throws Exception{
//    	when(userDao.getUserByAccessToken(handle_null)).thenReturn(user);
    	UserView userView = authenticationServiceImpl.getUserInfo(handle_null);
    	assertNull(userView);
    }
    
    @Test(expected = OAuthProblemException.class)
    public void testGetUserInfo_exception() throws Exception{
    	when(userDao.getUserByAccessToken(handle)).thenThrow(UserDaoException.class);
    	UserView userView = authenticationServiceImpl.getUserInfo(handle);
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
	
	@Test
	public void testGetClient_null() throws AuthenticationServiceException, UserDaoException {
//		when(userDao.getClientByRedirectURI(redirectURI)).thenReturn(client);
		
		RegisteredClient client = authenticationServiceImpl.getClient(redirectURI_null);
		
		assertNull(client);
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
	
	@Test
	public void testGetUser_null() throws AuthenticationServiceException, UserDaoException {
		when(userDao.getUser(userId)).thenReturn(user_null);
		User user = authenticationServiceImpl.getUser(userId);
		assertNull(user);
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
	
	@Test(expected = AuthenticationServiceException.class)
	public void testGetUserByEmail_exception() throws AuthenticationServiceException, UserDaoException {
		when(userDao.getUserByEmail(email)).thenThrow(UserDaoException.class);
		authenticationServiceImpl.getUserByEmail(email);
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
