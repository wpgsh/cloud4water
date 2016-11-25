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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import static org.mockito.Mockito.eq;

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
import net.wapwag.authn.model.ImageResponse;
import net.wapwag.authn.model.UserMsgResponse;
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
        when(userDao.getUser(eq(userId))).thenReturn(user);
        String resultCode = authenticationServiceImpl.getAuthorizationCode(userId, clientIdentity, redirectURI, scopes);

        verify(userDao).saveAccessToken(tokenArgumentCaptor.capture());

        assertNotNull(resultCode);
        assertEquals(tokenArgumentCaptor.getValue().getAuthrizationCode(), resultCode);
    }

    @Test(expected = OAuthProblemException.class)
    public void testGetAuthorizationCode_null_client() throws Exception {
        when(userDao.getClientByRedirectURI(redirectURI)).thenReturn(null);
        when(userDao.getUser(eq(userId))).thenReturn(user);
        authenticationServiceImpl.getAuthorizationCode(userId, clientIdentity, redirectURI, scopes);
    }
    
    @SuppressWarnings("Duplicates")
    @Test
    public void testGetAuthorizationCode_NonWPGClientWithAuthzedScope() throws Exception {
        ArgumentCaptor<AccessToken> tokenArgumentCaptor = ArgumentCaptor.forClass(AccessToken.class);

        when(userDao.getClientByRedirectURI(redirectURI)).thenReturn(nonWPGclientWithAuthzedScope);
        when(userDao.getAccessTokenByUserIdAndClientId(userId, clientId)).thenReturn(accessToken_nonWPGclientWithAuthzedScope);
        when(userDao.saveAccessToken(any(AccessToken.class))).thenReturn(1L);
        when(userDao.getUser(eq(userId))).thenReturn(user);
        String resultCode = authenticationServiceImpl.getAuthorizationCode(userId, clientIdentity, redirectURI, scopes);

        verify(userDao).saveAccessToken(tokenArgumentCaptor.capture());

        assertNotNull(resultCode);
        assertEquals(tokenArgumentCaptor.getValue().getAuthrizationCode(), resultCode);
    }

    @Test(expected = OAuthProblemException.class)
    public void testGetAuthorizationCode_NonWPGclientWithNoScope() throws Exception {
        when(userDao.getClientByRedirectURI(redirectURI)).thenReturn(nonWPGclientWithNoScope);
        when(userDao.getAccessTokenByUserIdAndClientId(userId, clientId)).thenReturn(accessToken_nonWPGclientWithNoScope);
        when(userDao.getUser(eq(userId))).thenReturn(user);
        authenticationServiceImpl.getAuthorizationCode(userId, clientIdentity, redirectURI, null);
    }

    @SuppressWarnings("Duplicates")
    @Test
    public void testGetAuthorizationCode_NonWPGClientWithNonAuthzedScope() throws Exception {
        ArgumentCaptor<AccessToken> tokenArgumentCaptor = ArgumentCaptor.forClass(AccessToken.class);

        when(userDao.getClientByRedirectURI(redirectURI)).thenReturn(nonWPGclientWithNonAuthzedScope);
        when(userDao.getAccessTokenByUserIdAndClientId(eq(userId), eq(clientId))).thenReturn(accessToken_nonWPGclientWithNonAuthzedScope);
        when(userDao.saveAccessToken(any(AccessToken.class))).thenReturn(1L);
        when(userDao.getUser(eq(userId))).thenReturn(user);
        
        String resultCode = authenticationServiceImpl.getAuthorizationCode(userId, clientIdentity, redirectURI, nonAuthzedscopes);

        verify(userDao).saveAccessToken(tokenArgumentCaptor.capture());

        assertNotNull(resultCode);
        assertEquals(tokenArgumentCaptor.getValue().getAuthrizationCode(), resultCode);
    }
    
    @Test(expected = OAuthProblemException.class)
    public void testGetAuthorizationCode_user_disabled() throws Exception {
        when(userDao.getClientByRedirectURI(redirectURI)).thenReturn(nonWPGclientWithNonAuthzedScope);
        when(userDao.getUser(eq(userId))).thenReturn(user_not_enabled);
        
        authenticationServiceImpl.getAuthorizationCode(userId, clientIdentity, redirectURI, nonAuthzedscopes);
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
        when(userDao.getUser(eq(userId))).thenReturn(user);
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

	@Test
	public void testUpdateUserProfile_success() throws AuthenticationServiceException, UserDaoException{
		when(userDao.getUser(userId)).thenReturn(user);
		when(userDao.saveUser(any(User.class))).thenReturn(1);
		
		UserMsgResponse msgResponse = authenticationServiceImpl.updateUserProfile(user_request, userId);
		
		assertEquals(true, msgResponse.getFlag());
	}
	
	@Test
	public void testUpdateUserProfile_pwd() throws AuthenticationServiceException, UserDaoException{
		when(userDao.getUser(userId)).thenReturn(user);
		when(userDao.saveUser(any(User.class))).thenReturn(0);
		
		UserMsgResponse msgResponse = authenticationServiceImpl.updateUserProfile(user_request_pwd, userId);
		
		assertEquals(false, msgResponse.getFlag());
	}
	
	@Test
	public void testUpdateUserProfile_fail() throws AuthenticationServiceException, UserDaoException{
		when(userDao.getUser(userId)).thenReturn(user);
		when(userDao.saveUser(any(User.class))).thenReturn(0);
		
		UserMsgResponse msgResponse = authenticationServiceImpl.updateUserProfile(user_request, userId);
		
		assertEquals(false, msgResponse.getFlag());
	}
	
	@Test(expected = AuthenticationServiceException.class)
	public void testUpdateUserProfile_exception() throws AuthenticationServiceException, UserDaoException{
		when(userDao.getUser(userId)).thenReturn(user);
		when(userDao.saveUser(any(User.class))).thenThrow(UserDaoException.class);
		authenticationServiceImpl.updateUserProfile(user_request, userId);
	}
	
	@Test()
	public void testCreateNewAvatar() throws AuthenticationServiceException, UserDaoException, FileNotFoundException{
		when(userDao.saveImg(any(Image.class))).thenReturn(1);
		when(userDao.getUser(userId)).thenReturn(user);
		when(userDao.saveUser(user)).thenReturn(1);
		InputStream inputStream = new FileInputStream("pom.xml");
		UserMsgResponse msgResponse = authenticationServiceImpl.createNewAvatar(userId, inputStream);
		assertEquals(true, msgResponse.getFlag());
	}
	
	@Test()
	public void testCreateNewAvatar_fail() throws AuthenticationServiceException, UserDaoException, FileNotFoundException{
		when(userDao.saveImg(any(Image.class))).thenReturn(0);
		InputStream inputStream = new FileInputStream("pom.xml");
		UserMsgResponse msgResponse = authenticationServiceImpl.createNewAvatar(userId, inputStream);
		assertEquals(false, msgResponse.getFlag());
	}
	
	@Test(expected = AuthenticationServiceException.class)
	public void testCreateNewAvatar_exception() throws AuthenticationServiceException, UserDaoException, FileNotFoundException{
		when(userDao.saveImg(any(Image.class))).thenThrow(AuthenticationServiceException.class);
		InputStream inputStream = new FileInputStream("pom.xml");
		authenticationServiceImpl.createNewAvatar(userId, inputStream);
	}
	
	@Test
	public void testUpdateUserAvatar() throws AuthenticationServiceException, UserDaoException, FileNotFoundException{
		when(userDao.saveImg(any(Image.class))).thenReturn(1);
		when(userDao.getUser(userId)).thenReturn(user);
		when(userDao.saveUser(user)).thenReturn(1);
		InputStream inputStream = new FileInputStream("pom.xml");
		UserMsgResponse msgResponse = authenticationServiceImpl.updateUserAvatar(userId, inputStream);
		assertEquals(true, msgResponse.getFlag());
	}
	
	@Test
	public void testUpdateUserAvatar_fail() throws AuthenticationServiceException, UserDaoException, FileNotFoundException{
		when(userDao.saveImg(any(Image.class))).thenReturn(0);
		InputStream inputStream = new FileInputStream("pom.xml");
		UserMsgResponse msgResponse = authenticationServiceImpl.updateUserAvatar(userId, inputStream);
		assertEquals(false, msgResponse.getFlag());
	}
	
	@Test(expected = AuthenticationServiceException.class)
	public void testUpdateUserAvatar_exception() throws AuthenticationServiceException, UserDaoException, FileNotFoundException{
		when(userDao.saveImg(any(Image.class))).thenThrow(AuthenticationServiceException.class);
		InputStream inputStream = new FileInputStream("pom.xml");
		authenticationServiceImpl.updateUserAvatar(userId, inputStream);
	}
	
	@Test
	public void testCreateNewUser() throws UserDaoException, AuthenticationServiceException{
		when(userDao.saveUser(user_request)).thenReturn(1);
		UserMsgResponse msgResponse = authenticationServiceImpl.createNewUser(user_request);
		assertEquals(true, msgResponse.getFlag());
	}
	
	@Test
	public void testCreateNewUser_pwd() throws UserDaoException, AuthenticationServiceException{
		when(userDao.saveUser(user_request_pwd)).thenReturn(1);
		UserMsgResponse msgResponse = authenticationServiceImpl.createNewUser(user_request_pwd);
		assertEquals(true, msgResponse.getFlag());
	}
	
	@Test
	public void testCreateNewUser_fail() throws UserDaoException, AuthenticationServiceException{
		when(userDao.saveUser(user_request)).thenReturn(0);
		UserMsgResponse msgResponse = authenticationServiceImpl.createNewUser(user_request);
		assertEquals(false, msgResponse.getFlag());
	}
	
	@Test(expected = AuthenticationServiceException.class)
	public void testCreateNewUser_exception() throws UserDaoException, AuthenticationServiceException{
		when(userDao.saveUser(user_request)).thenThrow(AuthenticationServiceException.class);
		authenticationServiceImpl.createNewUser(user_request);
	}
	
	@Test
	public void testRemoveUserAvatar() throws UserDaoException, AuthenticationServiceException {
		when(userDao.getUser(userId)).thenReturn(user);
		when(userDao.deleteImg(user.getAvartarId())).thenReturn(1);
		
		UserMsgResponse msgResponse = authenticationServiceImpl.removeUserAvatar(userId);
		assertEquals(true, msgResponse.getFlag());
	}
	
	@Test
	public void testRemoveUserAvatar_fail() throws UserDaoException, AuthenticationServiceException {
		when(userDao.getUser(userId)).thenReturn(user);
		when(userDao.deleteImg(user.getAvartarId())).thenReturn(0);
		
		UserMsgResponse msgResponse = authenticationServiceImpl.removeUserAvatar(userId);
		assertEquals(false, msgResponse.getFlag());
	}
	
	@Test(expected = AuthenticationServiceException.class)
	public void testRemoveUserAvatar_exception() throws UserDaoException, AuthenticationServiceException {
		when(userDao.getUser(userId)).thenReturn(user);
		when(userDao.deleteImg(user.getAvartarId())).thenThrow(UserDaoException.class);
		
		authenticationServiceImpl.removeUserAvatar(userId);
	}
	
	@Test
	public void testRemoveUserProfile() throws UserDaoException, AuthenticationServiceException{
		when(userDao.removeUser(userId)).thenReturn(1);
		UserMsgResponse msgResponse = authenticationServiceImpl.removeUserProfile(userId);
		assertEquals(true, msgResponse.getFlag());
	}
	
	@Test
	public void testRemoveUserProfile_fail() throws UserDaoException, AuthenticationServiceException{
		when(userDao.removeUser(userId)).thenReturn(0);
		UserMsgResponse msgResponse = authenticationServiceImpl.removeUserProfile(userId);
		assertEquals(false, msgResponse.getFlag());
	}
	
	@Test(expected = AuthenticationServiceException.class)
	public void testRemoveUserProfile_exception() throws UserDaoException, AuthenticationServiceException{
		when(userDao.removeUser(userId)).thenThrow(UserDaoException.class);
		authenticationServiceImpl.removeUserProfile(userId);
	}
	
	@Test
	public void testGetUserAvatar() throws UserDaoException, AuthenticationServiceException{
		when(userDao.getUser(userId)).thenReturn(user);
		when(userDao.getAvatar(user.getAvartarId())).thenReturn(image);
		ImageResponse imageResponse = authenticationServiceImpl.getUserAvatar(userId);
		assertNotNull(imageResponse);
	}
	
	@Test(expected = AuthenticationServiceException.class)
	public void testGetUserAvatar_user_null() throws UserDaoException, AuthenticationServiceException{
		when(userDao.getUser(userId)).thenReturn(null);
		authenticationServiceImpl.getUserAvatar(userId);
	}
	
	@Test(expected = AuthenticationServiceException.class)
	public void testGetUserAvatar_image_null() throws UserDaoException, AuthenticationServiceException{
		when(userDao.getUser(userId)).thenReturn(user);
		when(userDao.getAvatar(user.getAvartarId())).thenReturn(null);
		authenticationServiceImpl.getUserAvatar(userId);
	}
	
}
