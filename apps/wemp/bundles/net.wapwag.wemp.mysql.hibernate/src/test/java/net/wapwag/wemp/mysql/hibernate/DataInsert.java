package net.wapwag.wemp.mysql.hibernate;

import junit.framework.TestCase;
import net.wapwag.wemp.dao.model.ObjectData;
import net.wapwag.wemp.dao.model.ObjectType;
import net.wapwag.wemp.dao.model.geo.Country;
import net.wapwag.wemp.dao.model.link.*;
import net.wapwag.wemp.dao.model.org.Organization;
import net.wapwag.wemp.dao.model.org.WaterManageAuth;
import net.wapwag.wemp.dao.model.permission.*;
import org.junit.Test;

import javax.persistence.Query;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Data insert before other test,
 * <p>Use {@link org.junit.Assert} instead of .{@link junit.framework.TestCase} which suggested for the junit4</p>
 * Created by Administrator on 2016/10/31 0031.
 */
@SuppressWarnings("Duplicates")
public class DataInsert extends BaseTestConfig {

    private static final long orgId = 4L;

    @Test
    public void initialData() {
        testAddObject();
        testAddGroup();
        testAddRegisteredClient();

        testAddUserOrg();
        testAddAccessToken();
        testAddUserObject();
        testAddObjectByUser();
        testAddObjectByGroup();
        testAddGroupByOrg();
        testUpdateGroupsByOrg();
        testAddUsersByGroup();
        testAddUserByOrg();
        testAddObjectByOrg();
    }

    private void testAddUserOrg() {

        int i;
        User user = null;

        for (i = 0; i < 10; i++) {
            user = new User();
            user.setName("admin" + i);
            em.persist(user);
        }

        Organization organization;
        Set<Organization> orgSet = new HashSet<>();
        for (i = 0; i < 10; i++) {
            organization = new WaterManageAuth();
            organization.setName("研发部" + i);
            orgSet.add(organization);
            em.persist(organization);
        }

        UserOrg userOrg;

        for (Organization tmp : orgSet) {
            userOrg = new UserOrg();
            userOrg.setUserOrgId(new UserOrgId(user, tmp));
            em.persist(userOrg);
        }

    }

    private void testAddObject() {

        User user = new User();
        user.setName("管理员");
        em.persist(user);

        ObjectData china = new Country();
        china.setName("中国");

        em.persist(china);

        ObjectData america = new Country();
        america.setName("美国");

        em.persist(america);

        ObjectData japan = new Country();
        japan.setName("日本");

        em.persist(japan);

    }

    private void testAddGroup() {
        Group group;
        int addCount = 11;

        for (int i = 0; i < addCount; i++) {
            group = new Group();
            group.setName("研发组" + i);
            em.persist(group);
        }

        em.flush();

        long count = em.createQuery("select count(g.id) from Group g", Long.class).getSingleResult();

        assertTrue(count == addCount);
    }

    private void testAddRegisteredClient() {
        int addCount = 10;
        RegisteredClient client;
        for (int i = 0; i < addCount; i++) {
            client = new RegisteredClient();
            client.setClientId(UUID.randomUUID().toString().replace("-", ""));
            client.setClientSecret(UUID.randomUUID().toString().replace("-", ""));
            client.setRedirectURI("http://www.baidu.com?q=" + i);
            client.setClientVendor("wapwag");
            
            em.persist(client);
        }
        
        em.flush();
        long addedCount = em.createQuery("select count(client) from RegisteredClient client", Long.class)
                .getSingleResult();
        
        assertTrue("Added client count is not equal to the query client count", addCount == addedCount);
    }

    private void testAddAccessToken() {
        List<User> userList = em.createQuery("select user from User user", User.class)
                .getResultList();

        List<RegisteredClient> clientList = em.createQuery("select client from RegisteredClient client", RegisteredClient.class)
                .getResultList();

        AccessToken accessToken;

        for (User user : userList) {
            for (RegisteredClient client : clientList) {
                accessToken = new AccessToken();
                accessToken.setAccessTokenId(new AccessTokenId(user, client));
                accessToken.setHandle(UUID.randomUUID().toString().replace("-", ""));
                accessToken.setAuthrizationCode(UUID.randomUUID().toString().replace("-", ""));
                accessToken.setExpiration(Long.MAX_VALUE);
                em.persist(accessToken);
            }
        }

        long addedCount = em.createQuery("select count(token) from AccessToken token", Long.class).getSingleResult();

        assertEquals("Added count isn't equals to query count for AccessToken", userList.size() * clientList.size(), addedCount);
    }

    private static void testAddUserObject() {
        User user = em.find(User.class, 1L);
        List<ObjectData> objSet = em.createQuery("select obj from ObjectData obj", ObjectData.class).getResultList();
        UserObject userObject;

        for (ObjectData obj : objSet) {
            userObject = new UserObject();
            userObject.setUserObjectId(new UserObjectId(user, obj));
            userObject.setActionId("read");
            userObject.setTransitive(0);
            em.persist(userObject);
        }
    }

    private static void testAddObjectByUser() {
        long objectId = 12L;
        long userId = 2L;
        String actionId = "write";
        int transitive = 0;
        User user = new User();
        user.setId(userId);

        ObjectData objectData = new ObjectData();
        objectData.setId(objectId);

        UserObject userObject = new UserObject();
        userObject.setUserObjectId(new UserObjectId(user, objectData));
        userObject.setActionId(actionId);
        userObject.setTransitive(transitive);

        em.persist(userObject);
        em.flush();

        userObject = em.find(UserObject.class, new UserObjectId(user, objectData));

        assertTrue(userObject != null);
    }

    private static void testAddObjectByGroup() {

        List<Group> groupList = em.createQuery("select group from Group group", Group.class).getResultList();

        GroupObject groupObject;
        List<ObjectData> objList = em.createQuery("select obj from ObjectData obj where obj.type <> :type", ObjectData.class)
                .setParameter("type", ObjectType.WATER_MANAGE_AUTH)
                .setMaxResults(5).getResultList();


        for (Group group : groupList) {
            for (ObjectData objectData : objList) {
                groupObject = new GroupObject();
                groupObject.setGroupObjectId(new GroupObjectId(group, objectData));
                em.persist(groupObject);
            }
        }
    }

    private static void testAddGroupByOrg() {
        Group group = new Group();
        group.setName("新添加组");

        Organization org = em.find(Organization.class, orgId);
        org.setId(orgId);
        group.setOrganization(org);

        em.persist(group);

    }

    private static void testUpdateGroupsByOrg() {
        Query query = em.createQuery("update Group g set g.organization.id = :orgId")
                .setParameter("orgId", orgId);

        assertTrue(query.executeUpdate() > 0);
    }

    private static void testAddUsersByGroup() {
        long groupId = 2L;
        Group group = new Group();
        group.setId(groupId);

        List<User> userList = em.createQuery("select user from User user", User.class).getResultList();

        UserGroup userGroup;
        for (User user : userList) {
            userGroup = new UserGroup();
            userGroup.setUserGroupId(new UserGroupId(user, group));
            em.persist(userGroup);
        }

    }

    private static void testAddUserByOrg() {
        User user = new User();
        user.setId(1L);

        Organization organization = new Organization();
        organization.setId(orgId);

        UserOrg userOrg = new UserOrg();
        UserOrgId userOrgId = new UserOrgId(user, organization);
        userOrg.setUserOrgId(userOrgId);

        em.persist(userOrg);

        em.flush();

        UserOrg added = em.find(UserOrg.class, userOrgId);

        TestCase.assertTrue(added != null);
    }

    private static void testAddObjectByOrg() {
        Organization organization = new Organization();
        organization.setId(orgId);

        List<ObjectData> objList = em.createQuery("select obj from ObjectData obj where obj.type <> :type", ObjectData.class)
                .setParameter("type", ObjectType.WATER_MANAGE_AUTH)
                .setFirstResult(0).setMaxResults(10).getResultList();

        OrgObject orgObject;
        for (ObjectData objectData : objList) {
            orgObject = new OrgObject();
            orgObject.setOrgObjectId(new OrgObjectId(organization, objectData));
            em.persist(orgObject);
        }
    }

}
