package net.wapwag.authn.rest;

import com.thingswise.appframework.jaxrs.utils.Authorization;
import com.thingswise.appframework.jaxrs.utils.OAuth2;
import net.wapwag.authn.AuthenticationService;
import net.wapwag.authn.dao.model.Image;
import net.wapwag.authn.dao.model.User;
import net.wapwag.authn.rest.authz.AnyAuthenticatedUser;
import net.wapwag.authn.rest.authz.AuthorizationOnlyUserId;
import net.wapwag.authn.rest.dto.ImageResponse;
import net.wapwag.authn.rest.dto.UserMsgResponse;
import net.wapwag.authn.rest.dto.UserResponse;
import net.wapwag.authn.rest.oauth2.UsersTokenHandler;
import net.wapwag.authn.rest.util.StringUtil;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

@Component(service=AuthenticationResource.class)
@Path("/user")
@OAuth2(tokenHandler=UsersTokenHandler.NAME)
public class AuthenticationResource {

	@Reference
	protected AuthenticationService authnService;
	
	@GET
	@Path("/{userId}/public")
	@Produces(MediaType.APPLICATION_JSON)
	@Authorization @AnyAuthenticatedUser
	public UserResponse getPublicUserProfile(@PathParam("userId") String uid) throws Exception {
		User user = authnService.getUser(Long.valueOf(uid));
		UserResponse userResponse = new UserResponse();
		if (user == null) {
			return userResponse;
		}
        userResponse.setId(user.getId());
        userResponse.setUsername(user.getUsername());
        userResponse.setAvartarId(user.getAvartarId());
        userResponse.setAvatar(user.getAvatar());
        userResponse.setEmail(user.getEmail());
        userResponse.setEnabled(user.getEnabled());
        userResponse.setHomepage(user.getHomepage());
        userResponse.setName(user.getName());
        userResponse.setPhone1(user.getPhone1());
        userResponse.setPhone2(user.getPhone2());
        userResponse.setEmail(user.getEmail());
        return userResponse;
	}

	@GET
	@Path("/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Authorization @AuthorizationOnlyUserId(target="{userId}")
	public UserResponse getPrivateUserProfile(@PathParam("userId") long uid) throws Exception {
		User user = authnService.getUser(uid);
		UserResponse userResponse = new UserResponse();
		if (user == null) {
			return userResponse;
		}
        userResponse.setId(user.getId());
        userResponse.setUsername(user.getUsername());
        userResponse.setEnabled(user.getEnabled());
        userResponse.setAvartarId(user.getAvartarId());
        userResponse.setAvatar(user.getAvatar());
        userResponse.setEmail(user.getEmail());
        userResponse.setHomepage(user.getHomepage());
        userResponse.setName(user.getName());
        userResponse.setPhone1(user.getPhone1());
        userResponse.setPhone2(user.getPhone2());
        userResponse.setEmail(user.getEmail());
        return userResponse;
	}
	
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Authorization @AnyAuthenticatedUser
    public UserMsgResponse createNewUser(User userRequest) throws Exception {
        return authnService.createNewUser(userRequest);
    }
    
    @PUT
    @Path("/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Authorization @AuthorizationOnlyUserId(target="{userId}")
    public net.wapwag.authn.model.UserMsgResponse updateUserProfile(User userRequest, @PathParam("userId") long uid) throws Exception {
        return authnService.updateUserProfile(userRequest, uid);
    }
    
    @DELETE
    @Path("/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Authorization @AuthorizationOnlyUserId(target="{userId}")
    public net.wapwag.authn.model.UserMsgResponse removeUserProfile(@PathParam("userId") long uid) throws Exception {
    	return authnService.removeUserProfile(uid);
    }
    
    @GET
	@Path("/{userId}/image")
	@Produces(MediaType.APPLICATION_JSON)
    @Authorization @AnyAuthenticatedUser
	public net.wapwag.authn.model.ImageResponse getUserAvatar(@PathParam("userId") long uid) throws Exception {
		return authnService.getUserAvatar(uid);
	}
    
    @POST
    @Path("/{userId}/image")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Authorization @AuthorizationOnlyUserId(target="{userId}")
    public net.wapwag.authn.model.UserMsgResponse createNewAvatar(@PathParam("userId") long uid,
    		@FormDataParam("file") InputStream inputStream) throws Exception {
        return authnService.createNewAvatar(uid, inputStream);
    }
    
    @PUT
    @Path("/{userId}/image")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @Authorization @AuthorizationOnlyUserId(target="{userId}")
    public net.wapwag.authn.model.UserMsgResponse updateUserAvatar(@PathParam("userId") long uid,
    		@FormDataParam("file") InputStream inputStream) throws Exception {
    	return authnService.updateUserAvatar(uid, inputStream);
    }
    
    @DELETE
    @Path("/{userId}/image")
    @Produces(MediaType.APPLICATION_JSON)
    @Authorization @AuthorizationOnlyUserId(target="{userId}")
    public net.wapwag.authn.model.UserMsgResponse removeUserAvatar(@PathParam("userId") long uid) throws Exception {
        return authnService.removeUserAvatar(uid);
    }

}
