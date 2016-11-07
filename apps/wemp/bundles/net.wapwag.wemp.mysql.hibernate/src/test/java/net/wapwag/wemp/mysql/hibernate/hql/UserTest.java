package net.wapwag.wemp.mysql.hibernate.hql;

import net.wapwag.wemp.dao.model.ObjectData;
import net.wapwag.wemp.dao.model.permission.AccessToken;
import net.wapwag.wemp.dao.model.permission.AccessTokenId;
import net.wapwag.wemp.dao.model.permission.RegisteredClient;
import net.wapwag.wemp.dao.model.permission.User;
import net.wapwag.wemp.mysql.hibernate.BaseTestConfig;
import org.junit.Ignore;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertTrue;

/**
 * Test object
 * Created by Administrator on 2016/10/27 0027.
 */
@SuppressWarnings("Duplicates")
public class UserTest extends BaseTestConfig {

    private static final long userId = 1L;

    @Test
    public void testCheckPermission() {
        long objId = 11L;

        long havePermission;

        String userObjectHql = "select count(userObject) from UserObject userObject " +
                "where userObject.userObjectId.user.id = :userId " +
                "and userObject.userObjectId.objectData.id = :objId";
        havePermission = em.createQuery(userObjectHql, Long.class)
                .setParameter("userId", userId)
                .setParameter("objId", objId).getSingleResult();

        if (havePermission == 0) {
            String userGroupHql = "select count(userGroup) from UserGroup userGroup,GroupObject groupObject " +
                    "where userGroup.userGroupId.user.id = :userId " +
                    "and userGroup.userGroupId.group = groupObject.groupObjectId.group " +
                    "and groupObject.groupObjectId.objectData.id = :objId";
            havePermission = em.createQuery(userGroupHql, Long.class)
                    .setParameter("userId", userId)
                    .setParameter("objId", objId).getSingleResult();

            if (havePermission == 0) {
                String userOrgHql = "select count(userOrg) from UserOrg userOrg,GroupObject groupObject " +
                        "where userOrg.userOrgId.user.id = :userId " +
                        "and userOrg.userOrgId.organization = groupObject.groupObjectId.group.organization " +
                        "and groupObject.groupObjectId.objectData.id = :objId";
                havePermission = em.createQuery(userOrgHql, Long.class)
                        .setParameter("userId", userId)
                        .setParameter("objId", objId).getSingleResult();
            }
        }

        assertTrue(havePermission > 0);
    }

    @Test
    public void testGetObjectsByUser() {
        long userId = 1L;
        String action = "read";
        List<ObjectData> objList = new ArrayList<>();

        String userObjectHql = "select userObject.userObjectId.objectData " +
                "from UserObject userObject " +
                "where userObject.userObjectId.user.id = :userId " +
                "and userObject.actionId = :action";

        objList.addAll(em.createQuery(userObjectHql, ObjectData.class)
                .setParameter("userId", userId)
                .setParameter("action", action).getResultList());

        String userGroupHql = "select groupObject.groupObjectId.objectData from UserGroup userGroup,GroupObject groupObject " +
                "where userGroup.userGroupId.user.id = :userId " +
                "and userGroup.userGroupId.group = groupObject.groupObjectId.group " +
                "and groupObject.actionId = :action";

        objList.addAll(em.createQuery(userGroupHql, ObjectData.class)
                .setParameter("userId", userId)
                .setParameter("action", action).getResultList());

        String userOrgHql = "select groupObject.groupObjectId.objectData from UserOrg userOrg,GroupObject groupObject " +
                "where userOrg.userOrgId.user.id = :userId " +
                "and userOrg.userOrgId.organization = groupObject.groupObjectId.group.organization " +
                "and groupObject.actionId = :action";

        objList.addAll(em.createQuery(userOrgHql, ObjectData.class)
                .setParameter("userId", userId)
                .setParameter("action", action).getResultList());

        Set<ObjectData> result = new HashSet<>(objList);

        assertTrue(result.size() > 0);
    }

    @Test
    @Ignore("is not ready yet")
    public void isAuthorized() throws Exception {

    }

    @Test
    public void lookupAccessToken() throws Exception {
        final String handle = "0d1b320c48044ba2a1a377445b00e601";
        final String hql = "select token from AccessToken token where token.handle = :handle";


        AccessToken accessToken = em.createQuery(hql, AccessToken.class)
                .setParameter("handle", handle)
                .getSingleResult();
        
        assertTrue(accessToken != null);
    }

    @Test
    public void getClientByRedirectURI() throws Exception {
        String redirectURI = "http://www.baidu.com?q=0";

        final String hql = "select client from RegisteredClient client where client.redirectURI = :redirectURI";
        RegisteredClient client = em.createQuery(hql, RegisteredClient.class)
                .setParameter("redirectURI", redirectURI)
                .getSingleResult();

        assertTrue(client != null);
    }

    @Test
    public void getAccessTokenByUserIdAndClientId() throws Exception {
        long clientId = 1L;

        final String hql = "select token from AccessToken token " +
                "where token.accessTokenId.user.id = :userId " +
                "and token.accessTokenId.registeredClient.id = :clientId";

        AccessToken accessToken = em.createQuery(hql, AccessToken.class)
                .setParameter("userId", userId)
                .setParameter("clientId", clientId)
                .getSingleResult();

        assertTrue(accessToken != null);
    }

    @Test
    public void getAccessTokenByCode() throws Exception {
        final String code = "338f3d17cdc04685b3d41f0b7a364fd7";
        final String hql = "select token from AccessToken token where token.authrizationCode = :code";

        AccessToken accessToken = em.createQuery(hql, AccessToken.class)
                .setParameter("code", code)
                .getSingleResult();

        assertTrue(accessToken != null);
    }

    @Test
    public void saveAccessToken() throws Exception {
        AccessToken accessToken = new AccessToken();

        User user = new User();
        user.setId(2L);

        RegisteredClient client = new RegisteredClient();
        client.setId(2L);

        accessToken.setAccessTokenId(new AccessTokenId(user, client));
        accessToken.setExpiration(0L);
        accessToken.setAuthrizationCode(UUID.randomUUID().toString().replace("-", ""));
        accessToken.setHandle(UUID.randomUUID().toString().replace("-", ""));

        long count = em.merge(accessToken) != null ? 1L : 0L;

        assertTrue(count > 0);
    }

    @Test
    public void getUser() throws Exception {
        long userId = 1L;

        User user = em.find(User.class, userId);

        assertTrue(user != null);
    }

}
