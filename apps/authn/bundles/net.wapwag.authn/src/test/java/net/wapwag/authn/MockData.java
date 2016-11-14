package net.wapwag.authn;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.wapwag.authn.dao.model.AccessToken;
import net.wapwag.authn.dao.model.AccessTokenId;
import net.wapwag.authn.dao.model.Image;
import net.wapwag.authn.dao.model.RegisteredClient;
import net.wapwag.authn.dao.model.User;

/**
 * mock data
 * Created by Administrator on 2016/11/7 0007.
 */
@SuppressWarnings("Duplicates")
class MockData {

	static final long clientId = 1L;
    static final long objId = 1L;
    static final long userId = 1L;
    static final long groupId = 1L;
    static final long orgId = 1L;
    static final long invalidId = 0L;

    static final int count = 5;
    static final int addCount = 1;
    static final int removeCount = 1;
    static final int updateCount = 1;

    static final String email = "1163525902@qq.com";
    static final String redirectURI = "http://www.baidu.com3";
    static final String handle = "dfa8aea0a6ee11e68635247703754e80";
    static final String encodeHandle = Base64.getEncoder().encodeToString(handle.getBytes());
    static final String code = "dsfjdjfk23skjdsds3";
    static final String clientIdentity = "client3";
    static final String clientSecret = "dfdjfjkdkj23klaa3";
    static final String scope = "user:name user:avatar";
    static final String nonAuthzedscope = "user:email";
    static final String action = "read";
    static final String invalidString = "invalid_string";
    static final String invalidEncodeHanlde = "aW52YWxpZF9zdHJpbmc=";
	
    static Image image = new Image();
    static final User user = new User();
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
	
	static final Set<String> scopes = new HashSet<>();
    static final Set<String> nonAuthzedscopes = new HashSet<>();
	
	static{
		image.setId("qwe123");
		image.setImage("testImage".getBytes());
		
		
		user.setId(userId);
        user.setName("test1");
		user.setEmail(email);
		
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
