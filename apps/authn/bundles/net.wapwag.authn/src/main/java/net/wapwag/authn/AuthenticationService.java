package net.wapwag.authn;

import net.wapwag.authn.Ids.UserId;
import net.wapwag.authn.model.AccessToken;
import net.wapwag.authn.model.UserProfile;

import java.util.Set;

public interface AuthenticationService {

    /**
     * Get authorization code if the user has not been authorization
     * @param clientId The client ID you registered in the authorize system.
     * @param redirectURI The URL in your application where users will be sent after authorization.
     * @param scope For users who have authorized scopes for the application.
     * @return Return authorization code.
     * @throws AuthenticationServiceException
     */
	String getAuthorizationCode(String clientId, String redirectURI, Set<String> scope) throws AuthenticationServiceException;

    /**
     * Get access token if the user has been authorization
     * @param clientId The client ID you registered in the authorize system.
     * @param clientSecret The client secret you received from authorization system.
     * @param code The code you received from authorize response.
     * @param redirectURI The URL in your application where users will be sent after authorization.
     * @return Return accessToken.
     * @throws AuthenticationServiceException
     */
    String getAccessToken(String clientId, String clientSecret, String code, String redirectURI) throws AuthenticationServiceException;

	UserProfile getUserProfile(UserId uid) throws AuthenticationServiceException;		
	
	AccessToken lookupToken(String handle) throws AuthenticationServiceException;

}