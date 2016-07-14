package net.wapwag.authn;

import net.wapwag.authn.Ids.UserId;
import net.wapwag.authn.dao.model.RegisteredClient;
import net.wapwag.authn.dao.model.User;
import net.wapwag.authn.model.AccessToken;
import net.wapwag.authn.model.UserProfile;

import java.util.Set;

public interface AuthenticationService {

	/**
	 * Get authorization code if the user has not been authorization
	 *
	 * @param userId      The user ID who has logined
	 * @param clientId    The client ID you registered in the authorize system.
	 * @param redirectURI The URL in your application where users will be sent after authorization.
	 * @param scope       For users who have authorized scopes for the application.
	 * @return Return authorization code.
	 * @throws AuthenticationServiceException
	 */
	String getAuthorizationCode(long userId, long clientId, String redirectURI, Set<String> scope)
            throws AuthenticationServiceException;

	/**
	 * Get access token if the user has been authorization
	 *
     * @param userId      The user ID who has logined
	 * @param clientId     The client ID you registered in the authorize system.
	 * @param clientSecret The client secret you received from authorization system.
	 * @param code         The code you received from authorize response.
	 * @param redirectURI  The URL in your application where users will be sent after
	 *                     authorization.
	 * @return Return accessToken.
	 * @throws AuthenticationServiceException
	 */
	String getAccessToken(long userId, long clientId, String clientSecret, String code, String redirectURI)
            throws AuthenticationServiceException;

	/**
	 * Check the client is a valid wpg client.
     * <b>NOTE:</b>some client frameworks don't supply client_id and reply on redirect_uri
     * as the key,so use redirect_uri as a client identifier.
	 * @param redirectURI client identifier.
	 * @return return the registered client model.
	 * @throws AuthenticationServiceException
     */
	RegisteredClient getClient(String redirectURI) throws AuthenticationServiceException;

	UserProfile getUserProfile(UserId uid) throws AuthenticationServiceException;

	AccessToken lookupToken(String handle) throws AuthenticationServiceException;

	User getUser(long uid) throws AuthenticationServiceException;

	User saveUser(User user) throws AuthenticationServiceException;

	User removeUser(long uid) throws AuthenticationServiceException;

	User updateUser(long uid) throws AuthenticationServiceException;

	User getUserAvatar(long uid) throws AuthenticationServiceException;

	User saveUserAvatar(User user) throws AuthenticationServiceException;

	User removeUserAvatar(long uid) throws AuthenticationServiceException;

	User updateUserAvatar(long uid) throws AuthenticationServiceException;
    
    User getUserByName(String userName) throws AuthenticationServiceException;

}
