package net.wapwag.authn.rest;

import static net.wapwag.authn.rest.AuthenticationResourceMock.mockService;
import static net.wapwag.authn.rest.MockData.*;
import static org.eclipse.jetty.http.HttpStatus.INTERNAL_SERVER_ERROR_500;
import static org.eclipse.jetty.http.HttpStatus.OK_200;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.io.File;

import static org.mockito.Mockito.*;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.Test;

import net.wapwag.authn.AuthenticationServiceException;
import net.wapwag.authn.dao.model.Image;
import net.wapwag.authn.dao.model.User;
import net.wapwag.authn.rest.dto.UserMsgResponse;
import net.wapwag.authn.rest.dto.UserResponse;


public class AuthenticationResourceTest extends BaseResourceTest{

	private String path;
	
	@Override
	ResourceConfig initResource() {
		return new ResourceConfig(AuthenticationResourceMock.class).register(MultiPartFeature.class);
	}
	
	@Test
	public void testGetPublicUserProfile() throws AuthenticationServiceException {
		when(mockService.getUser(userId)).thenReturn(user);
		
		path = String.format("/user/%s/public", userId);

        Response response = target(path).request().get();

        UserResponse response2 = getResult(response, UserResponse.class);
        
        assertEquals(OK_200, response.getStatus());
        
        assertEquals("jiangzehu@163.com", response2.getEmail());
	}
	
	@Test
	public void testGetPublicUserProfile_UserNull_Exception() throws AuthenticationServiceException {
		when(mockService.getUser(userId)).thenReturn(user_null);
		
		path = String.format("/user/%s/public", userId);

        Response response = target(path).request().get();

        assertEquals(INTERNAL_SERVER_ERROR_500, response.getStatus());
	}
	
	@Test
	public void testGetPublicUserProfile_UserNotEnabled_Exception() throws AuthenticationServiceException {
		when(mockService.getUser(userId)).thenReturn(user_not_enabled);
		
		path = String.format("/user/%s/public", userId);

        Response response = target(path).request().get();

        assertEquals(INTERNAL_SERVER_ERROR_500, response.getStatus());
	}

	@Test
	public void testGetPrivateUserProfile() throws AuthenticationServiceException {
		when(mockService.getUser(userId)).thenReturn(user);
		
		path = String.format("/user/%s", userId);

        Response response = target(path).request().get();
        
        UserResponse response2 = getResult(response, UserResponse.class);
        
        assertEquals(OK_200, response.getStatus());
        
        assertEquals(1l, response2.getId());
	}
	
	@Test
	public void testGetPrivateUserProfile_UserNull_Exception() throws AuthenticationServiceException {
		when(mockService.getUser(userId)).thenReturn(user_null);
		
		path = String.format("/user/%s", userId);

        Response response = target(path).request().get();
        
        assertEquals(INTERNAL_SERVER_ERROR_500, response.getStatus());
	}
	
	@Test
	public void testGetPrivateUserProfile_UserNotEnabled_Exception() throws AuthenticationServiceException {
		when(mockService.getUser(userId)).thenReturn(user_not_enabled);
		
		path = String.format("/user/%s", userId);

        Response response = target(path).request().get();
        
        assertEquals(INTERNAL_SERVER_ERROR_500, response.getStatus());
	}

	@Test
	public void testCreateNewUser() throws AuthenticationServiceException {
		when(mockService.saveUser(any(User.class))).thenReturn(1);
		
		Entity<User> entity = Entity.entity(user, MediaType.APPLICATION_JSON);
		
		Response response = target("/user").request().post(entity);
		
		UserMsgResponse response2 = getResult(response, UserMsgResponse.class);
		
		assertEquals(OK_200, response.getStatus());
		
		assertEquals("add success", response2.getMsg());
	}
	
	@Test
	public void testCreateNewUser_password() throws AuthenticationServiceException {
		when(mockService.saveUser(any(User.class))).thenReturn(1);
		
		Entity<User> entity = Entity.entity(user_password, MediaType.APPLICATION_JSON);
		
		Response response = target("/user").request().post(entity);
		
		UserMsgResponse response2 = getResult(response, UserMsgResponse.class);
		
		assertEquals(OK_200, response.getStatus());
		
		assertEquals("add success", response2.getMsg());
	}
	
	@Test
	public void testUpdateUserProfile() throws AuthenticationServiceException {
		when(mockService.getUser(userId)).thenReturn(user);
		when(mockService.saveUser(user)).thenReturn(1);
		
		String path = String.format("/user/%s", userId);
		
		Entity<User> entity = Entity.entity(user, MediaType.APPLICATION_JSON);
		
		Response response = target(path).request().put(entity);
		
		UserMsgResponse response2 = getResult(response, UserMsgResponse.class);
		
		assertEquals(OK_200, response.getStatus());
		
		assertEquals("update success", response2.getMsg());
	}
	
	@Test
	public void testUpdateUserProfile_password() throws AuthenticationServiceException {
		when(mockService.getUser(userId)).thenReturn(user);
		when(mockService.saveUser(user)).thenReturn(1);
		
		String path = String.format("/user/%s", userId);
		
		Entity<User> entity = Entity.entity(user_password, MediaType.APPLICATION_JSON);
		
		Response response = target(path).request().put(entity);
		
		UserMsgResponse response2 = getResult(response, UserMsgResponse.class);
		
		assertEquals(OK_200, response.getStatus());
		
		assertEquals("update success", response2.getMsg());
	}

	@Test
	public void testRemoveUserProfile() throws AuthenticationServiceException {
		when(mockService.removeUser(userId)).thenReturn(1);
		
		String path = String.format("/user/%s", userId);
		
		Response response = target(path).request().delete();
		
		UserMsgResponse response2 = getResult(response, UserMsgResponse.class);
		
		assertEquals(OK_200, response.getStatus());
		
		assertEquals("remove success", response2.getMsg());
	}

	@Test
	public void testGetUserAvatar() throws AuthenticationServiceException {
		when(mockService.getUser(userId)).thenReturn(user);
		when(mockService.getAvatar(avantarId)).thenReturn(image);
		String path = String.format("/user/%s/image", userId);
		Response response = target(path).request().get();
		assertEquals(OK_200, response.getStatus());
	}
	
	@Test
	public void testGetUserAvatar_user_null() throws AuthenticationServiceException {
		when(mockService.getUser(userId)).thenReturn(user_null);
		when(mockService.getAvatar(avantarId)).thenReturn(image);
		String path = String.format("/user/%s/image", userId);
		Response response = target(path).request().get();
		assertEquals(INTERNAL_SERVER_ERROR_500, response.getStatus());
	}
	
	@Test
	public void testGetUserAvatar_image_null() throws AuthenticationServiceException {
		when(mockService.getUser(userId)).thenReturn(user);
		when(mockService.getAvatar(avantarId)).thenReturn(image_null);
		String path = String.format("/user/%s/image", userId);
		Response response = target(path).request().get();
		assertEquals(INTERNAL_SERVER_ERROR_500, response.getStatus());
	}
	

	@SuppressWarnings("resource")
	@Test
	public void testCreateNewAvatar() throws AuthenticationServiceException {
		when(mockService.saveImg(any(Image.class))).thenReturn(1);
		when(mockService.getUser(userId)).thenReturn(user);
		when(mockService.saveUser(any(User.class))).thenReturn(1);
		
		String path = String.format("/user/%s/image", userId);
		final FileDataBodyPart filePart = new FileDataBodyPart("file", new File("pom.xml"));
		 
		final MultiPart multipart = new FormDataMultiPart().bodyPart(filePart);
		 
		Response response = target(path).request()
		    .post(Entity.entity(multipart, MediaType.MULTIPART_FORM_DATA_TYPE));
		
		UserMsgResponse response2 = getResult(response, UserMsgResponse.class);
		
		assertEquals(OK_200, response.getStatus());
		assertEquals("add success", response2.getMsg());
	}
	
	@SuppressWarnings("resource")
	@Test
	public void testUpdateUserAvatar() throws AuthenticationServiceException {
		when(mockService.saveImg(any(Image.class))).thenReturn(1);
		when(mockService.getUser(userId)).thenReturn(user);
		when(mockService.saveUser(any(User.class))).thenReturn(1);
		
		String path = String.format("/user/%s/image", userId);
		final FileDataBodyPart filePart = new FileDataBodyPart("file", new File("pom.xml"));
		 
		final MultiPart multipart = new FormDataMultiPart().bodyPart(filePart);
		 
		Response response = target(path).request()
		    .put(Entity.entity(multipart, MediaType.MULTIPART_FORM_DATA_TYPE));
		
		UserMsgResponse response2 = getResult(response, UserMsgResponse.class);
		
		assertEquals(OK_200, response.getStatus());
		assertEquals("update success", response2.getMsg());
	}

	@Test
	public void testRemoveUserAvatar() throws AuthenticationServiceException {
		when(mockService.getUser(userId)).thenReturn(user);
		when(mockService.deleteImg(user.getAvartarId())).thenReturn(1);
		when(mockService.saveUser(user)).thenReturn(1);
		
		String path = String.format("/user/%s/image", userId);
		Response response = target(path).request().delete();
		UserMsgResponse response2 = getResult(response, UserMsgResponse.class);
		assertEquals(OK_200, response.getStatus());
		assertEquals("remove success", response2.getMsg());
	}

	@Test
	public void testGetUserByName() throws AuthenticationServiceException {
		when(mockService.getUserByName(userName)).thenReturn(user);
		String path = String.format("/user/getUserByName/%s", userName);
		Response response = target(path).request().get();
		UserResponse response2 = getResult(response, UserResponse.class);
		assertEquals(OK_200, response.getStatus());
		assertEquals(userName, response2.getUsername());
	}

}
