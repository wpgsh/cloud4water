package net.wapwag.authn.rest;

import static org.eclipse.jetty.http.HttpStatus.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.ResourceConfig;
import org.junit.Test;

import net.wapwag.authn.AuthenticationServiceException;
import net.wapwag.authn.rest.dto.UserResponse;

import static net.wapwag.authn.rest.MockData.*;
import static net.wapwag.authn.rest.AuthenticationResourceMock.mockService;


public class AuthenticationResourceTest extends BaseResourceTest{

	private String path;
	
	@Override
	ResourceConfig initResource() {
		return new ResourceConfig(AuthenticationResourceMock.class);
	}
	
	@Test
	public void testGetPublicUserProfile() throws AuthenticationServiceException {
		when(mockService.getUser(userId)).thenReturn(user);
		
		path = String.format("/user/%s/public", userId);

        Response response = target(path).request().get();

        UserResponse response2 = getResult(response, UserResponse.class);
        
        assertEquals(OK_200, response.getStatus());
	}

	@Test
	public void testGetPrivateUserProfile() throws AuthenticationServiceException {
		when(mockService.getUser(userId)).thenReturn(user);
		
		path = String.format("/user/%s", userId);

        Response response = target(path).request().get();
        
        assertEquals(OK_200, response.getStatus());
	}

//	@Test
//	public void testCreateNewUser() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testUpdateUserProfile() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testRemoveUserProfile() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetUserAvatar() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testCreateNewAvatar() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testUpdateUserAvatar() {
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

}
