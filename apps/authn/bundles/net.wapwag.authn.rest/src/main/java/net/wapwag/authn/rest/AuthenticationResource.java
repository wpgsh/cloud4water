package net.wapwag.authn.rest;

import javax.ws.rs.BeanParam;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import net.wapwag.authn.AuthenticationService;
import net.wapwag.authn.Ids;
import net.wapwag.authn.dao.model.User;
import net.wapwag.authn.model.UserProfile;
import net.wapwag.authn.rest.authz.AnyAuthenticatedUser;
import net.wapwag.authn.rest.dto.ImageRequest;
import net.wapwag.authn.rest.dto.ImageResponse;
import net.wapwag.authn.rest.dto.UserMsgResponse;
import net.wapwag.authn.rest.dto.UserRequest;
import net.wapwag.authn.rest.dto.UserResponse;
import net.wapwag.authn.rest.oauth2.UsersTokenHandler;

import org.apache.commons.lang3.StringUtils;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.thingswise.appframework.jaxrs.utils.Authorization;
import com.thingswise.appframework.jaxrs.utils.OAuth2;

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
	public GetPublicUserProfileResponse getPublicUserProfile(@PathParam("userId") String uid) throws Exception {
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
    public UserMsgResponse createNewUser(@BeanParam UserRequest userRequest) throws Exception {
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
            if(StringUtils.isNotBlank(user.getPasswordHash()) &&
            		user.getPasswordHash().equals(user.getPasswordSalt())&&
            		user.getPasswordHash().length()== user.getPasswordSalt().length() ){
            	user.setPasswordSalt(null); 
            	int result = authnService.saveUser(user);
            	 String msg = (result == 1 ? "add success" : "add fail");
                 return new UserMsgResponse(msg);
            }else{
                return new UserMsgResponse("two inputting password is not the same,fail");
            }
            
        } catch (Exception e) {
            throw new ResourceNotFoundException("Can not add user: " + userRequest.toString());
        }

    }
    public static void main(String[] args) {
    	if(StringUtils.isNotBlank("123   ") && "123 ".equals("123 ")){
    		System.out.print("sucess");
       }
    	else{

        	System.out.print("fail");
    		
    	}
    	
		
	}
    
    @GET
	@Path("/{userId}")
	@Produces(MediaType.APPLICATION_JSON)	
	public UserResponse getPrivateUserProfile(@PathParam("userId") long uid) throws Exception {
		User user = authnService.getUser(uid);
		if (user == null) {
			throw new ResourceNotFoundException("User not found: "+uid);
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
    
    @PUT
    @Path("/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Authorization @AnyAuthenticatedUser
    public UserMsgResponse updateUserProfile(@BeanParam UserRequest userRequest,@PathParam("userId") long uid) throws Exception {
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

            int result = authnService.saveUser(user);
            String msg = (result == 1 ? "update success" : "update fail");
            return new UserMsgResponse(msg);

        } catch (Exception e) {
            throw new ResourceNotFoundException("Can not add user: " + userRequest.toString());
        }

    }
    
    @DELETE
    @Path("/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Authorization @AnyAuthenticatedUser
    public UserMsgResponse removeUserProfile(@PathParam("userId") long uid) throws Exception {
        try {
            //Get the userRequest and convert it to User so the service layer could operate it.
        	int result = authnService.removeUser(uid);
        	String msg = (result == 1 ? "remove success" : "remove fail");
            return new UserMsgResponse(msg);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Can not remove user " );
        }

    }
    
   /* @Path("/{userId}/image")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Authorization @AnyAuthenticatedUser
    public UserMsgResponse createNewAvatar(@BeanParam ImageRequest imageRequest) throws Exception {
        try {
            //Get the userRequest and convert it to User so the service layer could operate it.
            User user = new User();
            user.setId(imageRequest.getId());
            user.set(imageRequest.getImage());

            int result = authnService.saveUser(user);
            
            String msg = (result == 1 ? "add success" : "add fail");
            return new UserMsgResponse(msg);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Can not add user: " + imageRequest.toString());
        }

    }
    
    @GET
	@Path("/{userId}/image")
	@Produces(MediaType.APPLICATION_JSON)	
	public UserResponse getUserAvatar(@PathParam("userId") long uid) throws Exception {
		User user = authnService.getUser(uid);
		if (user == null) {
			throw new ResourceNotFoundException("User not found: "+uid);
		}
		ImageResponse imageResponse = new ImageResponse();
		UserResponse userResponse = new UserResponse();
        //Get the user from service and convert it to UserResponse so the browser could read the json response.
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
    
    @PUT
    @Path("/{userId}/image")
    @Produces(MediaType.APPLICATION_JSON)
    @Authorization @AnyAuthenticatedUser
    public UserMsgResponse updateUserAvatar(@BeanParam ImageRequest imageRequest,@PathParam("userId") long uid) throws Exception {
        try {
            //Get the userRequest and convert it to User so the service layer could operate it.
            User user = new User();
            user.setId(imageRequest.getId());
            user.setUsername(imageRequest.getImage());

            int result = authnService.saveUser(user);
            String msg = (result == 1 ? "update success" : "update fail");
            return new UserMsgResponse(msg);

        } catch (Exception e) {
            throw new ResourceNotFoundException("Can not add user: " + imageRequest.toString());
        }

    }
    
    
    @DELETE
    @Path("/{userId}/image")
    @Produces(MediaType.APPLICATION_JSON)
    @Authorization @AnyAuthenticatedUser
    public UserMsgResponse removeUserAvatar(@PathParam("userId") long uid) throws Exception {
        try {
            //Get the userRequest and convert it to User so the service layer could operate it.
        	int result = authnService.removeUser(uid);
        	String msg = (result == 1 ? "remove success" : "remove fail");
            return new UserMsgResponse(msg);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Can not remove user " );
        }

    }*/
  
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
