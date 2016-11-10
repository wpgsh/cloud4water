package net.wapwag.authn.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

import net.wapwag.authn.dao.model.AccessToken;
import net.wapwag.authn.dao.model.AccessTokenId;
import net.wapwag.authn.dao.model.Image;
import net.wapwag.authn.dao.model.RegisteredClient;
import net.wapwag.authn.dao.model.User;

public class UserDaoTest extends net.wapwag.authn.mysql.hibernate.BaseTestConfig {

	private static final long userId = 1L;
	
	private static final long clientId = 1L;
	
	private static final int addCount = 1;
	
	@Test
	public void testGetUser() throws UserDaoException {
		User user = userDao.getUser(userId);
		Assert.assertNotNull(user);
	}

	@Test
	public void testLookupAccessToken() throws UserDaoException {
		final String handle = "token2";
        AccessToken accessToken = userDao.lookupAccessToken(handle);
        Assert.assertNotNull(accessToken);
	}

	@Test
	public void testGetClientByRedirectURI() throws UserDaoException {
		String redirectURI = "http://www.baidu.com";
		RegisteredClient client = userDao.getClientByRedirectURI(redirectURI);
		Assert.assertNotNull(client);
		Assert.assertEquals("wapwag", client.getClientVendor());
	}

	@Test
	public void testGetClientByClientId() throws UserDaoException {
		RegisteredClient registeredClient = userDao.getClientByClientId(clientId);
		Assert.assertNotNull(registeredClient);
	}
	
	@Test
	public void testSaveAccessToken() throws UserDaoException {
		AccessToken accessToken = new AccessToken();

        User user = new User();
        user.setId(1L);

        RegisteredClient client = new RegisteredClient();
        client.setId(2L);

        accessToken.setExpiration(0L);
        accessToken.setAuthrizationCode(UUID.randomUUID().toString().replace("-", ""));
        accessToken.setHandle(UUID.randomUUID().toString().replace("-", ""));
        accessToken.setAccessTokenId(new AccessTokenId(user, client));
        accessToken.setScope("user:*");
        
        long count = userDao.saveAccessToken(accessToken);
        
        assertTrue(count >= addCount);
	}

	@Test
	public void testGetAccessTokenByCode() throws UserDaoException {
		final String code = "dsfjdjfk23skjdsds2";

        AccessToken accessToken = userDao.getAccessTokenByCode(code);

        assertNotNull(accessToken);
	}

	@Test
	public void testGetAccessTokenByUserIdAndClientId() throws UserDaoException {
		long clientId = 4L;

        AccessToken accessToken = userDao.getAccessTokenByUserIdAndClientId(userId, clientId);

        assertNotNull(accessToken);
	}

	@Test
	public void testSaveUser() throws UserDaoException {
		User user = new User();
		user.setId(10l);
		user.setName("test10");
		user.setUsername("test10");
		
		int count = userDao.saveUser(user);
		Assert.assertTrue(count >= addCount);
	}

	@Test
	public void testRemoveUser() throws UserDaoException {
		int count = userDao.removeUser(userId);
		Assert.assertTrue(count >= addCount);
	}

	@Test
	public void testGetUserByName() throws UserDaoException {
		final String userName = "test1";
		User user = userDao.getUserByName(userName);
		Assert.assertNotNull(user);
	}

	@Test
	public void testGetUserByEmail() throws UserDaoException {
		String email = "1163525902@qq.com";
		User user = userDao.getUserByEmail(email);
		Assert.assertNotNull(user);
	}

	@Test
	public void testUpdateUserPwd() throws UserDaoException {
		User user = new User();
		user.setId(1l);
		user.setPasswordHash("123123");
		user.setPasswordSalt("qweq11");
		User newUser = userDao.updateUserPwd(user);
		Assert.assertEquals("123123", newUser.getPasswordHash());
		Assert.assertEquals("qweq11", newUser.getPasswordSalt());
	}

	@Test
	public void testSaveImg() throws UserDaoException {
		byte[] img = "testimg".getBytes();
		Image image = new Image();
		image.setId("1");
		image.setImage(img);
		int count = userDao.saveImg(image);
		Assert.assertTrue(count >= addCount);
	}

	@Test
	public void testDeleteImg() throws UserDaoException {
		byte[] img = "testimg".getBytes();
		Image image = new Image();
		image.setId("1");
		image.setImage(img);
		userDao.saveImg(image);
		
		String avartarId = "1";
		int result = userDao.deleteImg(avartarId);
		Assert.assertTrue(result >= addCount);
	}

	@Test
	public void testGetAvatar() throws UserDaoException {
		byte[] img = "testimg".getBytes();
		Image image = new Image();
		image.setId("1");
		image.setImage(img);
		userDao.saveImg(image);
		
		String avartarId = "1";
		
		Image image1 = userDao.getAvatar(avartarId);
		Assert.assertNotNull(image1);
	}

}
