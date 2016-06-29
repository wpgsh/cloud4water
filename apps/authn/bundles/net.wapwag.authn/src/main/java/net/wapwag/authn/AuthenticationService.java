package net.wapwag.authn;

import net.wapwag.authn.Ids.UserId;
import net.wapwag.authn.model.AccessToken;
import net.wapwag.authn.model.UserProfile;

public interface AuthenticationService {

	UserProfile getUserProfile(UserId uid) throws AuthenticationServiceException;		
	
	AccessToken lookupToken(String handle) throws AuthenticationServiceException;

}