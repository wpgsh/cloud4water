package net.wapwag.authn.rest;

import static net.wapwag.authn.rest.MockData.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.thingswise.appframework.jaxrs.utils.OAuth2;

import net.wapwag.authn.AuthenticationService;
import net.wapwag.authn.AuthenticationServiceException;
import net.wapwag.authn.rest.oauth2.UsersTokenHandler;

@OAuth2(tokenHandler=UsersTokenHandler.NAME)
public class AuthenticationResourceMock extends AuthenticationResource{

	static AuthenticationService mockService = mock(AuthenticationService.class);

    public AuthenticationResourceMock() {
    	authnService = mockService;

//        try {
//			when(authnService.getUser(userId))
//			        .thenReturn(user);
//			when(authnService.saveUser(user)).thenReturn(1);
//			when(authnService.removeUser(userId)).thenReturn(1);
//			when(authnService.getAvatar(avantarId)).thenReturn(image);
//			when(authnService.saveImg(image)).thenReturn(1);
//			when(authnService.deleteImg(avantarId)).thenReturn(1);
//			
//		} catch (AuthenticationServiceException e) {
//			e.printStackTrace();
//		}
    }
}
