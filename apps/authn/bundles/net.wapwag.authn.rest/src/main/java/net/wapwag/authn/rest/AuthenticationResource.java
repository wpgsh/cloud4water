package net.wapwag.authn.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import net.wapwag.authn.AuthenticationService;
import net.wapwag.authn.dao.model.User;
import net.wapwag.authn.rest.dto.UserResponse;
import net.wapwag.authn.rest.oauth2.UsersTokenHandler;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.thingswise.appframework.jaxrs.utils.OAuth2;

@Component(service=AuthenticationResource.class)
@Path("/oauth")
@OAuth2(tokenHandler=UsersTokenHandler.NAME)
public class AuthenticationResource {

	@Reference
	protected AuthenticationService authnService;
	
	
  
    @GET
	@Path("/getUserByName/{userName}")
	@Produces(MediaType.APPLICATION_JSON)	
	public UserResponse getUserByName(@PathParam("userName") String userName) throws Exception {
		
		
		User user = authnService.getUserByName(userName);
		
		if (user == null) {
			throw new ResourceNotFoundException("User not found: "+userName);
		}
		
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
	}

}
