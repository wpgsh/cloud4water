package net.wapwag.authn.rest;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import net.wapwag.authn.dao.model.User;
import net.wapwag.authn.rest.dto.UserRequest;
import net.wapwag.authn.rest.dto.UserResponse;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.thingswise.appframework.jaxrs.utils.Authorization;
import com.thingswise.appframework.jaxrs.utils.OAuth2;

import net.wapwag.authn.AuthenticationService;
import net.wapwag.authn.Ids;
import net.wapwag.authn.model.UserProfile;
import net.wapwag.authn.rest.authz.AnyAuthenticatedUser;
import net.wapwag.authn.rest.oauth2.UsersTokenHandler;

@Component(service=AuthenticationResource.class)
@Path("/user")
@OAuth2(tokenHandler=UsersTokenHandler.NAME)
public class AuthenticationResource {

	@Reference
	protected AuthenticationService authnService;
	
	@GET
	@Path("/{uid}")
	@Produces(MediaType.APPLICATION_JSON)
	@Authorization @AnyAuthenticatedUser 	
	public GetPublicUserProfileResponse getPublicUserProfile(@PathParam("uid") String uid) throws Exception {
		Ids.UserId _uid;
		try {
			_uid = Ids.UserId.fromString(uid);
		} catch (Exception e) {
			throw new InvalidRequestException("Invalid user id: "+e.getMessage(), e);
		}
		
		UserProfile user = authnService.getUserProfile(_uid);
		
		if (user == null) {
			throw new ResourceNotFoundException("User not found: "+uid);
		}
		
		GetPublicUserProfileResponse response = new GetPublicUserProfileResponse();
		response.setId(user.id);
		
		return response;
	}

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Authorization @AnyAuthenticatedUser
    public UserResponse addUser(@BeanParam UserRequest userRequest) throws Exception {
        try {
            //Get the userRequest and convert it to User so the service layer could operate it.
            User user = new User();
            user.setId(userRequest.getId());
            user.setUsername(userRequest.getUsername());
            user.setPasswordHash(userRequest.getPasswordHash());
            user.setPasswordSalt(userRequest.getPasswordSalt());
            user.setEnabled(userRequest.getEnabled());
            user.setAvartarId(userRequest.getAvartarId());
            user.setAvatar(userRequest.getAvatar());
            user.setEmail(userRequest.getEmail());
            user.setEnabled(userRequest.getEnabled());
            user.setHomepage(userRequest.getHomepage());
            user.setName(userRequest.getName());
            user.setPhone1(userRequest.getPhone1());
            user.setPhone2(userRequest.getPhone2());
            user.setEmail(userRequest.getEmail());

            user = authnService.saveUser(user);
            UserResponse userResponse = new UserResponse();

            //Get the user from service and convert it to UserResponse so the browser could read the json response.
            userResponse.setId(user.getId());
            userResponse.setId(user.getId());
            userResponse.setUsername(user.getUsername());
            userResponse.setPasswordHash(user.getPasswordHash());
            userResponse.setPasswordSalt(user.getPasswordSalt());
            userResponse.setEnabled(user.getEnabled());
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
        } catch (Exception e) {
            throw new ResourceNotFoundException("Can not add user: " + userRequest.toString());
        }

    }

}
