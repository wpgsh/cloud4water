package net.wapwag.authn.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Test;

import net.wapwag.authn.dao.model.AccessToken;

public class UserDaoTest extends net.wapwag.authn.mysql.hibernate.BaseTestConfig {

//	@Test
//	public void testGetUser() {
//		fail("Not yet implemented");
//	}

	@Test
	public void testLookupAccessToken() throws UserDaoException {
		final String handle = "authz_handle";

        AccessToken accessToken = userDao.lookupAccessToken(handle);

        assertNotNull(accessToken);
	}

//	@Test
//	public void testGetClientByRedirectURI() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetClientByClientId() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testSaveAccessToken() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetAccessToken() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetAccessTokenByCode() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetAccessTokenByUserIdAndClientId() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testSaveUser() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testRemoveUser() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetUserAvatar() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testSaveUserAvatar() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testRemoveUserAvatar() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetUserByName() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetUserByEmail() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testUpdateUserPwd() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testSaveImg() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testDeleteImg() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetAvatar() {
//		fail("Not yet implemented");
//	}

}
