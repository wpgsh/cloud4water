package net.wapwag.authn.rest;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

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
		
		if (user == null) {
			throw new ResourceNotFoundException("User not found: " + uid);
		}
		
		if(user != null && ("1").equals(user.getEnabled())){
			UserResponse userResponse = new UserResponse();
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
		}else{
			throw new ResourceNotFoundException("Enabled User not found: " + uid);
		}
	}

	@GET
	@Path("/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Authorization @AuthorizationOnlyUserId
	public UserResponse getPrivateUserProfile(@PathParam("userId") long uid) throws Exception {
		User user = authnService.getUser(uid);
		if (user == null) {
			throw new ResourceNotFoundException("User not found: "+uid);
		}
		
		if(user != null && ("1").equals(user.getEnabled())){
			UserResponse userResponse = new UserResponse();
	        //Get the user from service and convert it to UserResponse so the browser could read the json response.
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
		}else{
			throw new ResourceNotFoundException("Enabled User not found: " + uid);
		}
	}
	
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Authorization @AuthorizationOnlyUserId
    public UserMsgResponse createNewUser(User userRequest) throws Exception {
        //Get the userRequest and convert it to User so the service layer could operate it.
        if(userRequest.getPasswordHash() != null){
            long pwdSalt = System.currentTimeMillis();
            //save passwordHash,rule -> SHA1(pwd + pwdSalt)
            userRequest.setPasswordHash(StringUtil.strSHA1(StringUtil.strMd5(userRequest.getPasswordHash()) + pwdSalt));
            //save pwdSalt
            userRequest.setPasswordSalt(pwdSalt + "");
        }
    	int result = authnService.saveUser(userRequest);
    	String msg = (result == 1 ? "add success" : "add fail");
        return new UserMsgResponse(msg);
    }
    
    @PUT
    @Path("/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Authorization @AuthorizationOnlyUserId
    public UserMsgResponse updateUserProfile(User userRequest, @PathParam("userId") long uid) throws Exception {
    	User user = authnService.getUser(uid);
        //Get the userRequest and convert it to User so the service layer could operate it.
        if(userRequest.getUsername() != null){
        	user.setUsername(userRequest.getUsername());
        }
        if(userRequest.getPasswordHash() != null){
        	long pwdSalt = System.currentTimeMillis();
            //save passwordHash,rule -> SHA1(pwd + pwdSalt)
            user.setPasswordHash(StringUtil.strSHA1(StringUtil.strMd5(userRequest.getPasswordHash()) + pwdSalt));
            //save pwdSalt
            user.setPasswordSalt(pwdSalt + "");
        }
        if(userRequest.getEnabled() != null){
        	user.setEnabled(userRequest.getEnabled());
        }
        if(userRequest.getAvartarId() != null){
        	user.setAvartarId(userRequest.getAvartarId());
        }
        if(userRequest.getAvatar() != null){
        	user.setAvatar(userRequest.getAvatar());
        }
        if(userRequest.getEmail() != null){
        	user.setEmail(userRequest.getEmail());
        }
        if(userRequest.getHomepage() != null){
        	user.setHomepage(userRequest.getHomepage());
        }
        if(userRequest.getName() != null){
        	user.setName(userRequest.getName());
        }
        if(userRequest.getPhone1() != null){
        	user.setPhone1(userRequest.getPhone1());
        }
        if(userRequest.getPhone2() != null){
        	user.setPhone2(userRequest.getPhone2());
        }
        if(userRequest.getEmail() != null){
        	user.setEmail(userRequest.getEmail());
        }

        int result = authnService.saveUser(user);
        String msg = (result == 1 ? "update success" : "update fail");
        return new UserMsgResponse(msg);
    }
    
    @DELETE
    @Path("/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Authorization @AuthorizationOnlyUserId
    public UserMsgResponse removeUserProfile(@PathParam("userId") long uid) throws Exception {
        //Get the userRequest and convert it to User so the service layer could operate it.
    	int result = authnService.removeUser(uid);
    	String msg = (result == 1 ? "remove success" : "remove fail");
        return new UserMsgResponse(msg);
    }
    
    @GET
	@Path("/{userId}/image")
	@Produces(MediaType.APPLICATION_JSON)
    @Authorization @AnyAuthenticatedUser
	public ImageResponse getUserAvatar(@PathParam("userId") long uid) throws Exception {
		User user = authnService.getUser(uid);
		if (user == null) {
			throw new ResourceNotFoundException("User not found: "+uid);
		}
		ImageResponse imageResponse = new ImageResponse();
		if(user.getAvartarId() != null){
			Image image = authnService.getAvatar(user.getAvartarId());
			if(image != null){
				imageResponse.setId(image.getId() + "");
				imageResponse.setImage(image.getImage());
			}else{
				throw new ResourceNotFoundException("Image not found: " + uid);
			}
		}
        return imageResponse;
	}
    
    @POST
    @Path("/{userId}/image")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Authorization @AuthorizationOnlyUserId
    public UserMsgResponse createNewAvatar(@PathParam("userId") long uid, FormDataMultiPart input) throws Exception {
        User user = new User();
        
        FormDataBodyPart filePart = input.getField("file");
        InputStream inputStream = filePart.getValueAs(InputStream.class);

    	ByteArrayOutputStream output = new ByteArrayOutputStream();  
        byte[] buf = new byte[1024];  
        int numBytesRead = 0;  
        while ((numBytesRead = inputStream.read(buf)) != -1) {  
            output.write(buf, 0, numBytesRead);  
        }
        byte[] photo = output.toByteArray();
    	
    	String avartarId = StringUtil.getUUID();
    	Image image = new Image();
    	image.setId(avartarId);
    	image.setImage(photo);
    	
        int result = authnService.saveImg(image);
        
        if(result > 0){
        	user = authnService.getUser(uid);
        	user.setAvartarId(avartarId);
        	authnService.saveUser(user);
        }
        
        String msg = (result == 1 ? "add success" : "add fail");
        return new UserMsgResponse(msg);
    }
    
    @PUT
    @Path("/{userId}/image")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @Authorization @AuthorizationOnlyUserId
    public UserMsgResponse updateUserAvatar(@PathParam("userId") long uid, 
    		@FormDataParam("file") InputStream inputStream) throws Exception {
        try {
            //Get the userRequest and convert it to User so the service layer could operate it.
        	User user = new User();
        	    
        	ByteArrayOutputStream output = new ByteArrayOutputStream();  
	        byte[] buf = new byte[1024];  
	        int numBytesRead = 0;  
	        while ((numBytesRead = inputStream.read(buf)) != -1) {  
	            output.write(buf, 0, numBytesRead);  
	        }
	        byte[] photo = output.toByteArray();
            
        	String avartarId = StringUtil.getUUID();
        	Image image = new Image();
        	image.setId(avartarId);
        	image.setImage(photo);
        	
            int result = authnService.saveImg(image);
            
            if(result > 0){
            	user = authnService.getUser(uid);
            	user.setAvartarId(avartarId);
            	authnService.saveUser(user);
            }
            
            String msg = (result == 1 ? "update success" : "update fail");
            return new UserMsgResponse(msg);

        } catch (Exception e) {
            throw new ResourceNotFoundException("Can not add Image: " + uid);
        }
    }
    
    @DELETE
    @Path("/{userId}/image")
    @Produces(MediaType.APPLICATION_JSON)
    @Authorization @AuthorizationOnlyUserId
    public UserMsgResponse removeUserAvatar(@PathParam("userId") long uid) throws Exception {
        //Get the userRequest and convert it to User so the service layer could operate it.
    	User user = authnService.getUser(uid);
    	
    	int result = authnService.deleteImg(user.getAvartarId());
    	if(result > 0){
    		user.setAvartarId("");
        	authnService.saveUser(user);
    	}
    	String msg = (result == 1 ? "remove success" : "remove fail");
        return new UserMsgResponse(msg);
    }
  
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

}
