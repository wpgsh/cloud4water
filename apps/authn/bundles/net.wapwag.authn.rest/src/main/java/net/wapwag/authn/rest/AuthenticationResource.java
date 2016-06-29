package net.wapwag.authn.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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

}
