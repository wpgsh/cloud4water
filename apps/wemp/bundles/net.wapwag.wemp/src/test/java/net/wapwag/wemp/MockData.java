package net.wapwag.wemp;

import net.wapwag.wemp.dao.model.ObjectData;
import net.wapwag.wemp.dao.model.ObjectType;
import net.wapwag.wemp.dao.model.org.Organization;
import net.wapwag.wemp.dao.model.permission.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    static final int count = 5;
    static final int addCount = 1;
    static final int removeCount = 1;
    static final int updateCount = 1;

    static final String redirectURI = "http://wwww.baidu.com";
    static final String handle = "testToken";
    //Use base64 encoding handle:testToken
    static final String encodeHandle = "dGVzdFRva2Vu";
    static final String code = "testCode";
    static final String clientIdentity = "clientId";
    static final String clientSecret = "clientSecret";
    static final String scope = "user:name user:avatar";
    static final String nonAuthzedscope = "user:email";
    static final String action = "read";

    static final User user = new User();
    static final RegisteredClient client = new RegisteredClient();
    static final RegisteredClient wpgClient = new RegisteredClient();
    static final RegisteredClient nonWPGclientWithNoScope = new RegisteredClient();
    static final RegisteredClient nonWPGclientWithAuthzedScope = new RegisteredClient();
    static final RegisteredClient nonWPGclientWithNonAuthzedScope = new RegisteredClient();

    static final AccessTokenId accessTokenId = new AccessTokenId(user, client);
    static final AccessToken accessToken = new AccessToken();
    static final AccessToken accessToken_expired = new AccessToken();
    static final AccessToken accessToken_nonWPGclientWithNoScope = new AccessToken();
    static final AccessToken accessToken_nonWPGclientWithAuthzedScope = new AccessToken();
    static final AccessToken accessToken_nonWPGclientWithNonAuthzedScope = new AccessToken();
    static final ObjectData objectData = new ObjectData();

    static final Group group = new Group();
    static final Organization org = new Organization();

    static final Set<String> scopes = new HashSet<>();
    static final Set<String> nonAuthzedscopes = new HashSet<>();

    static final List<User> userList = new ArrayList<>();
    static final List<ObjectData> objectDataList = new ArrayList<>();
    static final List<Group> groupList = new ArrayList<>();



    static {
        client.setId(clientId);
        client.setClientId(clientIdentity);
        client.setClientSecret(clientSecret);

        wpgClient.setId(clientId);
        wpgClient.setClientId(clientIdentity);
        wpgClient.setClientVendor("wapwag");

        nonWPGclientWithNoScope.setId(clientId);
        nonWPGclientWithNoScope.setClientId(clientIdentity);

        nonWPGclientWithAuthzedScope.setId(clientId);
        nonWPGclientWithAuthzedScope.setClientId(clientIdentity);

        nonWPGclientWithNonAuthzedScope.setId(clientId);
        nonWPGclientWithNonAuthzedScope.setClientId(clientIdentity);

        scopes.add("user:name");
        scopes.add("user:avatar");

        nonAuthzedscopes.add(nonAuthzedscope);

        accessToken.setAccessTokenId(accessTokenId);
        accessToken.setHandle(handle);
        accessToken.setAuthrizationCode(code);
        accessToken.setExpiration(Long.MAX_VALUE);
        accessToken.setScope(scope);

        accessToken_expired.setAccessTokenId(accessTokenId);
        accessToken_expired.setHandle(handle);
        accessToken_expired.setAuthrizationCode(code);
        accessToken_expired.setExpiration(0);
        accessToken_expired.setScope(scope);

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

        objectData.setId(objId);
        objectData.setName("objame0");
        objectData.setType(ObjectType.COUNTRY);

        user.setId(groupId);
        user.setName("userName0");

        group.setId(groupId);
        group.setName("groupName0");

        org.setId(orgId);
        org.setName("orgName0");

        int count = 5;

        User user;
        ObjectData objectData;
        Group group;
        for (int i = 0; i < count; i++) {
            user = new User();
            user.setId(i);
            user.setName("userName" + i);
            userList.add(user);

            objectData = new ObjectData();
            objectData.setId(i);
            objectData.setName("objame" + i);
            objectData.setType(ObjectType.COUNTRY);
            objectDataList.add(objectData);


            group = new Group();
            group.setId(i);
            group.setName("groupName" + i);
            groupList.add(group);
        }
    }
}
