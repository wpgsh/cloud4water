package net.wapwag.authn;

import java.io.InputStream;
import java.util.Set;

import org.apache.oltu.oauth2.common.exception.OAuthProblemException;

import net.wapwag.authn.Ids.UserId;
import net.wapwag.authn.dao.model.Image;
import net.wapwag.authn.dao.model.RegisteredClient;
import net.wapwag.authn.dao.model.User;
import net.wapwag.authn.model.AccessTokenMapper;
import net.wapwag.authn.model.ImageResponse;
import net.wapwag.authn.model.UserMsgResponse;
import net.wapwag.authn.model.UserProfile;
import net.wapwag.authn.model.UserView;

public interface AuthenticationService {

	/**
	 * Get authorization code if the user has not been authorization
	 *
	 * @param userId      The user ID who has logined
	 * @param cliendId The client id registered in the resource server
	 * @param redirectURI The URL in your application where users will be sent after authorization.
	 * @param scope       For users who have authorized scopes for the application.
	 * @return Return authorization code.
	 * @throws OAuthProblemException
	 */
	String getAuthorizationCode(long userId, String cliendId, String redirectURI, Set<String> scope)
            throws OAuthProblemException;

	/**
	 * Get access token if the user has been authorization
	 *
	 * @param clientId     The client id registered in the resource server
	 * @param clientSecret The client secret you received from authorization system.
	 * @param code         The code you received from authorize response.
	 * @param redirectURI  The URL in your application where users will be sent after
	 *                     authorization.
	 * @return Return accessToken.
	 * @throws OAuthProblemException
	 */
	String getAccessToken(String clientId, String clientSecret, String code, String redirectURI)
            throws OAuthProblemException;

	/**
	 * Check the client is a valid wpg client.
     * <b>NOTE:</b>some client frameworks don't supply client_id and reply on redirect_uri
     * as the key,so use redirect_uri as a client identifier.
	 * @param redirectURI client identifier.
	 * @return return the registered client model.
	 * @throws AuthenticationServiceException
     */
	RegisteredClient getClient(String redirectURI) throws AuthenticationServiceException;

    /**
     *
     * @param token
     * @return
     * @throws OAuthProblemException
     */
    UserView getUserInfo(String token) throws OAuthProblemException;

	UserProfile getUserProfile(UserId uid) throws AuthenticationServiceException;

	AccessTokenMapper lookupToken(String handle) throws AuthenticationServiceException;

	User getUser(long uid) throws AuthenticationServiceException;

	int saveUser(User user) throws AuthenticationServiceException;
	
	int removeUser(long uid) throws AuthenticationServiceException;

    User getUserByName(String userName) throws AuthenticationServiceException;
    
    User getUserByEmail(String email) throws AuthenticationServiceException;
    
    User updateUserPwd(User user) throws AuthenticationServiceException;

	int saveImg(Image image) throws AuthenticationServiceException;

	int deleteImg(String avartarId) throws AuthenticationServiceException;

	Image getAvatar(String avartarId) throws AuthenticationServiceException;
	
	UserMsgResponse updateUserProfile(User user, long userId) throws AuthenticationServiceException;
	
	UserMsgResponse createNewAvatar(long userId, InputStream inputStream) throws AuthenticationServiceException;

	UserMsgResponse createNewUser(User user) throws AuthenticationServiceException;
	
	UserMsgResponse updateUserAvatar(long userId, InputStream inputStream) throws AuthenticationServiceException;
	
	UserMsgResponse removeUserAvatar(long userId) throws AuthenticationServiceException;
	
	UserMsgResponse removeUserProfile(long userId) throws AuthenticationServiceException;
	
	ImageResponse getUserAvatar(long userId) throws AuthenticationServiceException;
}
