package net.wapwag.authn.rest;

import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

import net.wapwag.authn.dao.model.AccessToken;
import net.wapwag.authn.dao.model.AccessTokenId;
import net.wapwag.authn.dao.model.Image;
import net.wapwag.authn.dao.model.RegisteredClient;
import net.wapwag.authn.dao.model.User;
import net.wapwag.authn.rest.dto.UserRequestJson;

/**
 * mock data
 * Created by Administrator on 2016/11/7 0007.
 */
class MockData {

	static final long clientId = 1L;
    static final long objId = 1L;
    static final long userId = 1L;
    static final long groupId = 1L;
    static final long orgId = 1L;
    static final long invalidId = 0L;
    static final String userName = "test1";
    static final int count = 5;
    static final int addCount = 1;
    static final int removeCount = 1;
    static final int updateCount = 1;

    static final String avantarId = "qwe123";
    static final String email = "1163525902@qq.com";
    static final String redirectURI = "http://www.baidu.com3";
    static final String handle = "dfa8aea0a6ee11e68635247703754e80";
    static final String encodeHandle = Base64.getEncoder().encodeToString(handle.getBytes());
    static final String code = "dsfjdjfk23skjdsds3";
    static final String clientIdentity = "client3";
    static final String clientSecret = "dfdjfjkdkj23klaa3";
    static final String scope = "user:name user:avatar";
    static final String nonAuthzedscope = "user:email";
    static final String invalidString = "invalid_string";
    static final String invalidEncodeHanlde = "aW52YWxpZF9zdHJpbmc=";
	
    static Image image_null = null;
    static Image image = new Image();
    static final User user = new User();
    static final User user_null = null;
    static final User user_not_enabled = new User();
    static final User user_password = new User();
    static final User user_null_avatarId = new User();
	static final AccessToken accessToken = new AccessToken();
	static final AccessToken accessToken_expired = new AccessToken();
	static final RegisteredClient wpgClient = new RegisteredClient();
	static final RegisteredClient client = new RegisteredClient();
	static final RegisteredClient nonWPGclientWithNoScope = new RegisteredClient();
    static final RegisteredClient nonWPGclientWithAuthzedScope = new RegisteredClient();
    static final RegisteredClient nonWPGclientWithNonAuthzedScope = new RegisteredClient();
	static final AccessTokenId accessTokenId = new AccessTokenId(user, client);
	
	static final AccessToken accessToken_nonWPGclientWithNoScope = new AccessToken();
    static final AccessToken accessToken_nonWPGclientWithAuthzedScope = new AccessToken();
    static final AccessToken accessToken_nonWPGclientWithNonAuthzedScope = new AccessToken();
	
    static UserRequestJson userRequestJson = new UserRequestJson();
    
	static final Set<String> scopes = new HashSet<>();
    static final Set<String> nonAuthzedscopes = new HashSet<>();
	
	static{
		image.setId(avantarId);
		image.setImage("testImage".getBytes());
		
		user.setId(userId);
		user.setEnabled("1");
        user.setName("test1");
        user.setUsername("test1");
        user.setPhone2("15850817392");
		user.setEmail(email);
		user.setAvartarId(avantarId);
		user.setAvatar("avatar");
		user.setEmail("jiangzehu@163.com");
		user.setHomepage("http://www.baidu.com");
		user.setPhone1("15850817392");
		
		user_null_avatarId.setId(userId);
		user_null_avatarId.setEnabled("1");
		user_null_avatarId.setName("test1");
		user_null_avatarId.setUsername("test1");
		user_null_avatarId.setPhone2("15850817392");
		user_null_avatarId.setEmail(email);
		user_null_avatarId.setAvatar("avatar");
		user_null_avatarId.setEmail("jiangzehu@163.com");
		user_null_avatarId.setHomepage("http://www.baidu.com");
		user_null_avatarId.setPhone1("15850817392");
		
		user_not_enabled.setId(userId);
		user_not_enabled.setName("test1");
		user_not_enabled.setEmail(email);
		user_not_enabled.setAvartarId(avantarId);
		user_not_enabled.setEmail("jiangzehu@163.com");
		user_not_enabled.setHomepage("http://www.baidu.com");
		user_not_enabled.setPhone1("15850817392");
		
		userRequestJson.setId(userId);
		userRequestJson.setEnabled("1");
		userRequestJson.setName("test1");
		userRequestJson.setEmail(email);
		userRequestJson.setAvartarId(avantarId);
		userRequestJson.setEmail("jiangzehu@163.com");
		userRequestJson.setHomepage("http://www.baidu.com");
		userRequestJson.setPhone1("15850817392");
		
		
		user_password.setEnabled("1");
		user_password.setPasswordHash("dfdf");
		user_password.setName("test1");
		user_password.setUsername("test1");
		user_password.setPhone2("15850817392");
		user_password.setEmail(email);
		user_password.setAvartarId(avantarId);
		user_password.setAvatar("avatar");
		user_password.setEmail("jiangzehu@163.com");
		user_password.setHomepage("http://www.baidu.com");
		user_password.setPhone1("15850817392");
		
        wpgClient.setId(clientId);
        wpgClient.setClientId(clientIdentity);
        wpgClient.setClientVendor("wapwag");
        
		client.setId(3l);
        client.setClientId(clientIdentity);
        client.setClientSecret(clientSecret);
		
		accessToken.setAccessTokenId(accessTokenId);
        accessToken.setHandle(handle);
        accessToken.setAuthrizationCode(code);
        accessToken.setExpiration(Long.MAX_VALUE);
        accessToken.setScope(scope);
        
        nonWPGclientWithNoScope.setId(clientId);
        nonWPGclientWithNoScope.setClientId(clientIdentity);

        nonWPGclientWithAuthzedScope.setId(clientId);
        nonWPGclientWithAuthzedScope.setClientId(clientIdentity);

        nonWPGclientWithNonAuthzedScope.setId(clientId);
        nonWPGclientWithNonAuthzedScope.setClientId(clientIdentity);
        
        accessToken_expired.setAccessTokenId(accessTokenId);
        accessToken_expired.setHandle(handle);
        accessToken_expired.setAuthrizationCode(code);
        accessToken_expired.setExpiration(0);
        accessToken_expired.setScope(scope);
        
        scopes.add("user:name");
        scopes.add("user:avatar");

        nonAuthzedscopes.add(nonAuthzedscope);
        
        accessToken_nonWPGclientWithNoScope.setAccessTokenId(accessTokenId);
        accessToken_nonWPGclientWithNoScope.setHandle(handle);
        accessToken_nonWPGclientWithNoScope.setAuthrizationCode(code);
        accessToken_nonWPGclientWithNoScope.setExpiration(Long.MAX_VALUE);

        accessToken_nonWPGclientWithAuthzedScope.setAccessTokenId(accessTokenId);
        accessToken_nonWPGclientWithAuthzedScope.setHandle(handle);
        accessToken_nonWPGclientWithAuthzedScope.setAuthrizationCode(code);
        accessToken_nonWPGclientWithAuthzedScope.setExpiration(Long.MAX_VALUE);
        accessToken_nonWPGclientWithAuthzedScope.setScope(scope);

        accessToken_nonWPGclientWithNonAuthzedScope.setAccessTokenId(accessTokenId);
        accessToken_nonWPGclientWithNonAuthzedScope.setHandle(handle);
        accessToken_nonWPGclientWithNonAuthzedScope.setAuthrizationCode(code);
        accessToken_nonWPGclientWithNonAuthzedScope.setExpiration(Long.MAX_VALUE);

        accessToken_nonWPGclientWithNonAuthzedScope.setScope(nonAuthzedscope);

	}
    
}
