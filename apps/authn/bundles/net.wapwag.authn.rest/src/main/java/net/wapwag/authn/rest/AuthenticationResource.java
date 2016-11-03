package net.wapwag.authn.rest;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
//import org.glassfish.jersey.media.multipart.FormDataBodyPart;
//import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.thingswise.appframework.jaxrs.utils.Authorization;

import net.wapwag.authn.AuthenticationService;
import net.wapwag.authn.dao.model.Image;
import net.wapwag.authn.dao.model.User;
import net.wapwag.authn.rest.authz.AnyAuthenticatedUser;
import net.wapwag.authn.rest.dto.ImageResponse;
import net.wapwag.authn.rest.dto.UserMsgResponse;
import net.wapwag.authn.rest.dto.UserRequestJson;
import net.wapwag.authn.rest.dto.UserResponse;
import net.wapwag.authn.rest.util.StringUtil;

@Component(service=AuthenticationResource.class)
@Path("/user")
//@OAuth2(tokenHandler=UsersTokenHandler.NAME)
public class AuthenticationResource {

	@Reference
	protected AuthenticationService authnService;
	
	@GET
	@Path("/{userId}/public")
	@Produces(MediaType.APPLICATION_JSON)
//	@Authorization @AnyAuthenticatedUser
	public UserResponse getPublicUserProfile(@PathParam("userId") String uid) throws Exception {
//		Ids.UserId _uid;
//		try {
//			_uid = Ids.UserId.fromString(uid);
//		} catch (Exception e) {
//			throw new InvalidRequestException("Invalid user id: "+e.getMessage(), e);
//		}
//		
//		UserProfile user = authnService.getUserProfile(_uid);
		
		User user = authnService.getUser(Long.valueOf(uid));
		
		if (user == null) {
			throw new ResourceNotFoundException("User not found: "+uid);
		}
		
//		GetPublicUserProfileResponse response = new GetPublicUserProfileResponse();
//		response.setId(user.id);
		
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
//	@Authorization @AnyAuthenticatedUser
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
//    @Authorization @AnyAuthenticatedUser
    public UserMsgResponse createNewUser(@Context UserRequestJson userRequest) throws Exception {
        try {
            //Get the userRequest and convert it to User so the service layer could operate it.
            User user = new User();
            user.setUsername(userRequest.getUsername());
            if(userRequest.getPasswordHash() != null){
	            long pwdSalt = System.currentTimeMillis();
	            //save passwordHash,rule -> SHA1(pwd By MD5 + pwdSalt)
	            user.setPasswordHash(StringUtil.strSHA1(userRequest.getPasswordHash() + pwdSalt));
	            //save pwdSalt
	            user.setPasswordSalt(pwdSalt + "");
            }
            user.setEnabled(userRequest.getEnabled());
            user.setAvartarId(userRequest.getAvartarId());
            user.setAvatar(userRequest.getAvatar());
            user.setEmail(userRequest.getEmail());
            user.setHomepage(userRequest.getHomepage());
            user.setName(userRequest.getName());
            user.setPhone1(userRequest.getPhone1());
            user.setPhone2(userRequest.getPhone2());
            user.setEmail(userRequest.getEmail());
            
        	int result = authnService.saveUser(user);
        	String msg = (result == 1 ? "add success" : "add fail");
            return new UserMsgResponse(msg);
            
        } catch (Exception e) {
            throw new ResourceNotFoundException("Can not add user: " + userRequest.toString());
        }
    }
    
    @PUT
    @Path("/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
//    @Authorization @AnyAuthenticatedUser
    public UserMsgResponse updateUserProfile(@Context UserRequestJson userRequest,@PathParam("userId") long uid) throws Exception {
        try {
        	User user = authnService.getUser(uid);
            //Get the userRequest and convert it to User so the service layer could operate it.
            if(userRequest.getUsername() != null){
            	user.setUsername(userRequest.getUsername());
            }
            if(userRequest.getPasswordHash() != null){
            	long pwdSalt = System.currentTimeMillis();
	            //save passwordHash,rule -> SHA1(pwd By MD5 + pwdSalt)
	            user.setPasswordHash(StringUtil.strSHA1(userRequest.getPasswordHash() + pwdSalt));
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

        } catch (Exception e) {
            throw new ResourceNotFoundException("Can not add user: " + userRequest.toString());
        }
    }
    
    @DELETE
    @Path("/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
//    @Authorization @AnyAuthenticatedUser
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
    
    @GET
	@Path("/{userId}/image")
	@Produces(MediaType.APPLICATION_JSON)
//    @Authorization @AnyAuthenticatedUser
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
//    @Authorization @AnyAuthenticatedUser
    public UserMsgResponse createNewAvatar(@PathParam("userId") long uid, MultipartFormDataInput input) throws Exception {
        try {
            User user = new User();
            Map<String, InputPart> uploadForm = input.getFormData();  
            InputPart inputPart = uploadForm.get("file");
            
            //convert the uploaded file to inputstream   
            InputStream inputStream = inputPart.getBody(InputStream.class,null);   
    
        	byte[] photo = new byte[inputStream.available()];
        	inputStream.read(photo);
        	inputStream.close();
            
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
        } catch (Exception e) {
            throw new ResourceNotFoundException("Can not add Image: " + uid);
        }

    }
    
    @PUT
    @Path("/{userId}/image")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
//    @Authorization @AnyAuthenticatedUser
    public UserMsgResponse updateUserAvatar(@PathParam("userId") long uid, MultipartFormDataInput input) throws Exception {
        try {
            //Get the userRequest and convert it to User so the service layer could operate it.
        	User user = new User();
        	Map<String, InputPart> uploadForm = input.getFormData();  
            InputPart inputPart = uploadForm.get("file");
            
            //convert the uploaded file to inputstream   
            InputStream inputStream = inputPart.getBody(InputStream.class,null);
        	byte[] photo = new byte[inputStream.available()];
        	inputStream.read(photo);
        	inputStream.close();
            
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
//    @Authorization @AnyAuthenticatedUser
    public UserMsgResponse removeUserAvatar(@PathParam("userId") long uid) throws Exception {
        try {
            //Get the userRequest and convert it to User so the service layer could operate it.
        	User user = authnService.getUser(uid);
        	
        	int result = authnService.deleteImg(user.getAvartarId());
        	if(result > 0){
        		user.setAvartarId("");
            	authnService.saveUser(user);
        	}
        	String msg = (result == 1 ? "remove success" : "remove fail");
            return new UserMsgResponse(msg);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Can not remove user " );
        }

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
