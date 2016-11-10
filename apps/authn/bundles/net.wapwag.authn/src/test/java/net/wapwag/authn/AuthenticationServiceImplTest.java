package net.wapwag.authn;

import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import net.wapwag.authn.dao.UserDao;
import net.wapwag.authn.dao.model.User;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationServiceImplTest {

	private static AuthenticationServiceImpl authenticationServiceImpl;
	
	private final long userId = 1L;
	
	private User user = new User();

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
	public void testGetUserProfile() {
//		when(userDao.getUser(userId)).thenReturn(user);
//		authenticationServiceImpl.getUserProfile(user);
		
	}

//	@Test
//	public void testLookupToken() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetAccessToken() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetAuthorizationCode() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetClient() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetUser() {
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
