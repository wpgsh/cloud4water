package net.wapwag.authn.rest;

import static net.wapwag.authn.rest.AuthenticationResourceMock.mockService;
import static net.wapwag.authn.rest.MockData.avantarId;
import static net.wapwag.authn.rest.MockData.imageResponse;
import static net.wapwag.authn.rest.MockData.user;
import static net.wapwag.authn.rest.MockData.userId;
import static net.wapwag.authn.rest.MockData.user_null;
import static net.wapwag.authn.rest.MockData.user_password;
import static org.eclipse.jetty.http.HttpStatus.INTERNAL_SERVER_ERROR_500;
import static org.eclipse.jetty.http.HttpStatus.OK_200;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.InputStream;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.jetty.http.HttpStatus;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import org.junit.Test;

import net.wapwag.authn.AuthenticationServiceException;
import net.wapwag.authn.dao.model.User;
import net.wapwag.authn.model.UserMsgResponse;
import net.wapwag.authn.rest.dto.UserResponse;


public class AuthenticationResourceAnnotationTest extends BaseResourceTest{

	private String path;
	
	@Test
	public void testGetPublicUserProfile() throws AuthenticationServiceException {
		when(mockService.getUser(userId)).thenReturn(user);
		
		path = String.format("/user/%s/public", userId);

        Response response = validToken(target(path)).get();

        UserResponse response2 = getResult(response, UserResponse.class);
        
        assertEquals(OK_200, response.getStatus());
        
        assertEquals("jiangzehu@163.com", response2.getEmail());
	}
	
	@Test
	public void testGetPublicUserProfile_invalidToken() throws AuthenticationServiceException {
		when(mockService.getUser(userId)).thenReturn(user);
		
		path = String.format("/user/%s/public", userId);

        Response response = invalidToken(target(path)).get();

        assertEquals(HttpStatus.FORBIDDEN_403, response.getStatus());
	}
	
	@Test
	public void testGetPublicUserProfile_UserNull_Exception() throws AuthenticationServiceException {
		when(mockService.getUser(userId)).thenReturn(user_null);
		
		path = String.format("/user/%s/public", userId);

        Response response = validToken(target(path)).get();
        
        UserResponse response2 = getResult(response, UserResponse.class);

        assertEquals(OK_200, response.getStatus());
        assertEquals(null, response2.getAvartarId());
	}
	
	@Test
	public void testGetPrivateUserProfile() throws AuthenticationServiceException {
		when(mockService.getUser(eq(userId))).thenReturn(user);
		
		path = String.format("/user/%s", userId);

        Response response = validToken(target(path)).get();
        
        UserResponse response2 = getResult(response, UserResponse.class);
        
        assertEquals(OK_200, response.getStatus());
        
        assertEquals(1l, response2.getId());
	}
	
	@Test
	public void testGetPrivateUserProfile_invalidToken() throws AuthenticationServiceException {
		when(mockService.getUser(eq(userId))).thenReturn(user);
		
		path = String.format("/user/%s", userId);

        Response response = invalidToken(target(path)).get();
        
        assertEquals(HttpStatus.FORBIDDEN_403, response.getStatus());
	}
	
	@Test
	public void testGetPrivateUserProfile_UserNull_Exception() throws AuthenticationServiceException {
		when(mockService.getUser(userId)).thenReturn(user_null);
		
		path = String.format("/user/%s", userId);

        Response response = validToken(target(path)).get();
        
        UserResponse response2 = getResult(response, UserResponse.class);

        assertEquals(OK_200, response.getStatus());
        assertEquals(null, response2.getAvartarId());
	}
	
	@Test
	public void testCreateNewUser_true() throws AuthenticationServiceException {
//		when(mockService.saveUser(any(User.class))).thenReturn(1);
		
		when(mockService.createNewUser(any(User.class))).thenReturn(new UserMsgResponse(true, "add success"));
		
		Entity<User> entity = Entity.entity(user, MediaType.APPLICATION_JSON);
		
		Response response = validToken(target("/user")).post(entity);
		
		UserMsgResponse response2 = getResult(response, UserMsgResponse.class);
		
		assertEquals(OK_200, response.getStatus());
		
		assertEquals("add success", response2.getMsg());
	}
	
	@Test
	public void testCreateNewUser_invalidToken() throws AuthenticationServiceException {
		when(mockService.saveUser(any(User.class))).thenReturn(1);
		
		Entity<User> entity = Entity.entity(user, MediaType.APPLICATION_JSON);
		
		Response response = invalidToken(target("/user")).post(entity);
		
		assertEquals(HttpStatus.FORBIDDEN_403, response.getStatus());
	}
	
	@Test
	public void testCreateNewUser_false() throws AuthenticationServiceException {
//		when(mockService.saveUser(any(User.class))).thenReturn(0);
		
		when(mockService.createNewUser(any(User.class))).thenReturn(new UserMsgResponse(false, "add fail"));
		
		Entity<User> entity = Entity.entity(user, MediaType.APPLICATION_JSON);
		
		Response response = validToken(target("/user/")).post(entity);
		
		UserMsgResponse response2 = getResult(response, UserMsgResponse.class);
		
		assertEquals(OK_200, response.getStatus());
		
		assertEquals("add fail", response2.getMsg());
	}
	
	@Test
	public void testCreateNewUser_password_fail() throws AuthenticationServiceException {
//		when(mockService.saveUser(any(User.class))).thenReturn(0);
		when(mockService.createNewUser(any(User.class))).thenReturn(new UserMsgResponse(false, "add fail"));
		
		Entity<User> entity = Entity.entity(user_password, MediaType.APPLICATION_JSON);
		
		Response response = validToken(target("/user")).post(entity);
		
		UserMsgResponse response2 = getResult(response, UserMsgResponse.class);
		
		assertEquals(OK_200, response.getStatus());
		
		assertEquals("add fail", response2.getMsg());
	}
	
	@Test
	public void testUpdateUserProfile() throws AuthenticationServiceException {
//		when(mockService.getUser(userId)).thenReturn(user);
//		when(mockService.saveUser(user)).thenReturn(1);
		when(mockService.updateUserProfile(any(User.class), eq(userId))).thenReturn(new UserMsgResponse(true, "update success"));
		
		String path = String.format("/user/%s", userId);
		
		Entity<User> entity = Entity.entity(user, MediaType.APPLICATION_JSON);
		
		Response response = validToken(target(path)).put(entity);
		
		UserMsgResponse response2 = getResult(response, UserMsgResponse.class);
		
		assertEquals(OK_200, response.getStatus());
		
		assertEquals("update success", response2.getMsg());
	}
	
	@Test
	public void testUpdateUserProfile_invalidToken() throws AuthenticationServiceException {
		when(mockService.getUser(userId)).thenReturn(user);
		when(mockService.saveUser(user)).thenReturn(1);
		
		String path = String.format("/user/%s", userId);
		
		Entity<User> entity = Entity.entity(user, MediaType.APPLICATION_JSON);
		
		Response response = invalidToken(target(path)).put(entity);
		
		assertEquals(HttpStatus.FORBIDDEN_403, response.getStatus());
	}
	
	@Test
	public void testUpdateUserProfile_fail() throws AuthenticationServiceException {
//		when(mockService.getUser(userId)).thenReturn(user);
//		when(mockService.saveUser(user)).thenReturn(0);
		
		when(mockService.updateUserProfile(any(User.class), eq(userId))).thenReturn(new UserMsgResponse(false, "update fail"));
		
		String path = String.format("/user/%s", userId);
		
		Entity<User> entity = Entity.entity(user, MediaType.APPLICATION_JSON);
		
		Response response = validToken(target(path)).put(entity);
		
		UserMsgResponse response2 = getResult(response, UserMsgResponse.class);
		
		assertEquals(OK_200, response.getStatus());
		
		assertEquals("update fail", response2.getMsg());
	}
	
	@Test
	public void testUpdateUserProfile_password() throws AuthenticationServiceException {
//		when(mockService.getUser(userId)).thenReturn(user);
//		when(mockService.saveUser(user)).thenReturn(1);
		
		when(mockService.updateUserProfile(any(User.class), eq(userId))).thenReturn(new UserMsgResponse(true,"update success"));
		
		String path = String.format("/user/%s", userId);
		
		Entity<User> entity = Entity.entity(user_password, MediaType.APPLICATION_JSON);
		
		Response response = validToken(target(path)).put(entity);
		
		UserMsgResponse response2 = getResult(response, UserMsgResponse.class);
		
		assertEquals(OK_200, response.getStatus());
		
		assertEquals("update success", response2.getMsg());
	}

	@Test
	public void testRemoveUserProfile() throws AuthenticationServiceException {
//		when(mockService.removeUser(userId)).thenReturn(1);
		
		when(mockService.removeUserProfile(eq(userId))).thenReturn(new UserMsgResponse(true, "remove success"));
		
		String path = String.format("/user/%s", userId);
		
		Response response = validToken(target(path)).delete();
		
		UserMsgResponse response2 = getResult(response, UserMsgResponse.class);
		
		assertEquals(OK_200, response.getStatus());
		
		assertEquals("remove success", response2.getMsg());
	}
	
	@Test
	public void testRemoveUserProfile_invalidToken() throws AuthenticationServiceException {
//		when(mockService.removeUser(userId)).thenReturn(1);
		when(mockService.removeUserProfile(eq(userId))).thenReturn(new UserMsgResponse(true, "remove success"));
		
		String path = String.format("/user/%s", userId);
		
		Response response = invalidToken(target(path)).delete();
		
		assertEquals(HttpStatus.FORBIDDEN_403, response.getStatus());
	}
	
	@Test
	public void testRemoveUserProfile_fail() throws AuthenticationServiceException {
//		when(mockService.removeUser(userId)).thenReturn(0);
		
		when(mockService.removeUserProfile(eq(userId))).thenReturn(new UserMsgResponse(false, "remove fail"));
		
		String path = String.format("/user/%s", userId);
		
		Response response = validToken(target(path)).delete();
		
		UserMsgResponse response2 = getResult(response, UserMsgResponse.class);
		
		assertEquals(OK_200, response.getStatus());
		
		assertEquals("remove fail", response2.getMsg());
	}
	
	@Test
	public void testGetUserAvatar() throws AuthenticationServiceException {
//		when(mockService.getUser(userId)).thenReturn(user);
//		when(mockService.getAvatar(user.getAvartarId())).thenReturn(image);
		when(mockService.getUserAvatar(eq(userId))).thenReturn(imageResponse);
		
		String path = String.format("/user/%s/image", userId);
		Response response = validToken(target(path)).get();
		
		assertEquals(OK_200, response.getStatus());
		assertEquals(avantarId, imageResponse.getId());
	}
	
	@Test
	public void testGetUserAvatar_invalidToken() throws AuthenticationServiceException {
//		when(mockService.getUser(userId)).thenReturn(user);
//		when(mockService.getAvatar(user.getAvartarId())).thenReturn(image);
		
		when(mockService.getUserAvatar(eq(userId))).thenReturn(imageResponse);
		String path = String.format("/user/%s/image", userId);
		Response response = invalidToken(target(path)).get();
		assertEquals(HttpStatus.FORBIDDEN_403, response.getStatus());
	}
	
	@Test
	public void testGetUserAvatar_false() throws AuthenticationServiceException {
//		when(mockService.getUser(userId)).thenReturn(user_null_avatarId);
//		when(mockService.getAvatar(avantarId)).thenReturn(image);
		
		when(mockService.getUserAvatar(eq(userId))).thenThrow(AuthenticationServiceException.class);
		String path = String.format("/user/%s/image", userId);
		Response response = validToken(target(path)).get();
		assertEquals(INTERNAL_SERVER_ERROR_500, response.getStatus());
	}
	
//	@Test
//	public void testGetUserAvatar_user_null() throws AuthenticationServiceException {
//		when(mockService.getUser(userId)).thenReturn(user_null);
//		when(mockService.getAvatar(avantarId)).thenReturn(image);
//		String path = String.format("/user/%s/image", userId);
//		Response response = validToken(target(path)).get();
//		assertEquals(INTERNAL_SERVER_ERROR_500, response.getStatus());
//	}
//	
//	@Test
//	public void testGetUserAvatar_image_null() throws AuthenticationServiceException {
//		when(mockService.getUser(userId)).thenReturn(user);
//		when(mockService.getAvatar(avantarId)).thenReturn(image_null);
//		String path = String.format("/user/%s/image", userId);
//		Response response = validToken(target(path)).get();
//		assertEquals(INTERNAL_SERVER_ERROR_500, response.getStatus());
//	}
	

	@SuppressWarnings("resource")
	@Test
	public void testCreateNewAvatar() throws AuthenticationServiceException {
//		when(mockService.saveImg(any(Image.class))).thenReturn(1);
//		when(mockService.getUser(eq(userId))).thenReturn(user);
//		when(mockService.saveUser(any(User.class))).thenReturn(1);
		
		when(mockService.createNewAvatar(eq(userId), any(InputStream.class))).thenReturn(new UserMsgResponse(true, "add success"));
		
		String path = String.format("/user/%s/image", userId);
		final FileDataBodyPart filePart = new FileDataBodyPart("file", new File("pom.xml"));
		 
		final MultiPart multipart = new FormDataMultiPart().bodyPart(filePart);
		 
		Response response = validToken(target(path))
//				.request()
		    .post(Entity.entity(multipart, MediaType.MULTIPART_FORM_DATA_TYPE));
		
		UserMsgResponse response2 = getResult(response, UserMsgResponse.class);
		
		assertEquals(OK_200, response.getStatus());
		assertEquals("add success", response2.getMsg());
	}
	
	@SuppressWarnings("resource")
	@Test
	public void testCreateNewAvatar_invalidToken() throws AuthenticationServiceException {
//		when(mockService.saveImg(any(Image.class))).thenReturn(1);
//		when(mockService.getUser(eq(userId))).thenReturn(user);
//		when(mockService.saveUser(any(User.class))).thenReturn(1);
		
		when(mockService.createNewAvatar(eq(userId), any(InputStream.class))).thenReturn(new UserMsgResponse(true, "add success"));
		
		String path = String.format("/user/%s/image", userId);
		final FileDataBodyPart filePart = new FileDataBodyPart("file", new File("pom.xml"));
		 
		final MultiPart multipart = new FormDataMultiPart().bodyPart(filePart);
		 
		Response response = invalidToken(target(path))
//				.request()
		    .post(Entity.entity(multipart, MediaType.MULTIPART_FORM_DATA_TYPE));
		
		assertEquals(HttpStatus.FORBIDDEN_403, response.getStatus());
	}
	
	@SuppressWarnings("resource")
	@Test
	public void testCreateNewAvatar_fail() throws AuthenticationServiceException {
//		when(mockService.saveImg(any(Image.class))).thenReturn(0);
//		when(mockService.getUser(userId)).thenReturn(user);
//		when(mockService.saveUser(any(User.class))).thenReturn(1);
		UserMsgResponse msgResponse = new UserMsgResponse(true, "add fail");
		when(mockService.createNewAvatar(eq(userId), any(InputStream.class))).thenReturn(msgResponse);
		
		String path = String.format("/user/%s/image", userId);
		final FileDataBodyPart filePart = new FileDataBodyPart("file", new File("pom.xml"));

		final MultiPart multipart = new FormDataMultiPart().bodyPart(filePart);
		 
		Response response = validToken(target(path))
		    .post(Entity.entity(multipart, MediaType.MULTIPART_FORM_DATA_TYPE));
		
		UserMsgResponse response2 = getResult(response, UserMsgResponse.class);
		
		assertEquals(OK_200, response.getStatus());
		assertEquals("add fail", response2.getMsg());
	}
	
	@SuppressWarnings("resource")
	@Test
	public void testUpdateUserAvatar() throws AuthenticationServiceException {
//		when(mockService.saveImg(any(Image.class))).thenReturn(1);
//		when(mockService.getUser(userId)).thenReturn(user);
//		when(mockService.saveUser(any(User.class))).thenReturn(1);
		
		when(mockService.updateUserAvatar(eq(userId), any(InputStream.class))).thenReturn(new UserMsgResponse(true, "update success"));
		
		String path = String.format("/user/%s/image", userId);
		final FileDataBodyPart filePart = new FileDataBodyPart("file", new File("pom.xml"));
		 
		final MultiPart multipart = new FormDataMultiPart().bodyPart(filePart);
		 
		Response response = validToken(target(path))
		    .put(Entity.entity(multipart, MediaType.MULTIPART_FORM_DATA_TYPE));
		
		UserMsgResponse response2 = getResult(response, UserMsgResponse.class);
		
		assertEquals(OK_200, response.getStatus());
		assertEquals("update success", response2.getMsg());
	}
	
	@SuppressWarnings("resource")
	@Test
	public void testUpdateUserAvatar_invalidToken() throws AuthenticationServiceException {
//		when(mockService.saveImg(any(Image.class))).thenReturn(1);
//		when(mockService.getUser(userId)).thenReturn(user);
//		when(mockService.saveUser(any(User.class))).thenReturn(1);
		
		when(mockService.updateUserAvatar(eq(userId), any(InputStream.class))).thenReturn(new UserMsgResponse(true, "update success"));
		
		String path = String.format("/user/%s/image", userId);
		final FileDataBodyPart filePart = new FileDataBodyPart("file", new File("pom.xml"));
		 
		final MultiPart multipart = new FormDataMultiPart().bodyPart(filePart);
		 
		Response response = invalidToken(target(path))
		    .put(Entity.entity(multipart, MediaType.MULTIPART_FORM_DATA_TYPE));
		
		assertEquals(HttpStatus.FORBIDDEN_403, response.getStatus());
	}
	
	@SuppressWarnings("resource")
	@Test
	public void testUpdateUserAvatar_fail() throws AuthenticationServiceException {
//		when(mockService.saveImg(any(Image.class))).thenReturn(0);
//		when(mockService.getUser(userId)).thenReturn(user);
//		when(mockService.saveUser(any(User.class))).thenReturn(1);
		
		when(mockService.updateUserAvatar(eq(userId), any(InputStream.class))).thenReturn(new UserMsgResponse(false, "update fail"));
		
		String path = String.format("/user/%s/image", userId);
		final FileDataBodyPart filePart = new FileDataBodyPart("file", new File("pom.xml"));
		 
		final MultiPart multipart = new FormDataMultiPart().bodyPart(filePart);
		 
		Response response = validToken(target(path))
		    .put(Entity.entity(multipart, MediaType.MULTIPART_FORM_DATA_TYPE));
		
		UserMsgResponse response2 = getResult(response, UserMsgResponse.class);
		
		assertEquals(OK_200, response.getStatus());
		assertEquals("update fail", response2.getMsg());
	}

	@Test
	public void testRemoveUserAvatar() throws AuthenticationServiceException {
//		when(mockService.getUser(userId)).thenReturn(user);
//		when(mockService.deleteImg(user.getAvartarId())).thenReturn(1);
//		when(mockService.saveUser(user)).thenReturn(1);
		
		when(mockService.removeUserAvatar(eq(userId))).thenReturn(new UserMsgResponse(true, "remove success"));
		
		String path = String.format("/user/%s/image", userId);
		Response response = validToken(target(path)).delete();
		UserMsgResponse response2 = getResult(response, UserMsgResponse.class);
		assertEquals(OK_200, response.getStatus());
		assertEquals("remove success", response2.getMsg());
	}
	
	@Test
	public void testRemoveUserAvatar_invalidToken() throws AuthenticationServiceException {
//		when(mockService.getUser(userId)).thenReturn(user);
//		when(mockService.deleteImg(user.getAvartarId())).thenReturn(1);
//		when(mockService.saveUser(user)).thenReturn(1);
		
		when(mockService.removeUserAvatar(eq(userId))).thenReturn(new UserMsgResponse(true, "remove success"));
		
		String path = String.format("/user/%s/image", userId);
		Response response = invalidToken(target(path)).delete();
		assertEquals(HttpStatus.FORBIDDEN_403, response.getStatus());
	}
	
	@Test
	public void testRemoveUserAvatar_fail() throws AuthenticationServiceException {
//		when(mockService.getUser(userId)).thenReturn(user);
//		when(mockService.deleteImg(user.getAvartarId())).thenReturn(0);
//		when(mockService.saveUser(user)).thenReturn(1);
		
		when(mockService.removeUserAvatar(eq(userId))).thenReturn(new UserMsgResponse(false, "remove fail"));
		
		String path = String.format("/user/%s/image", userId);
		Response response = validToken(target(path)).delete();
		UserMsgResponse response2 = getResult(response, UserMsgResponse.class);
		assertEquals(OK_200, response.getStatus());
		assertEquals("remove fail", response2.getMsg());
	}

}
