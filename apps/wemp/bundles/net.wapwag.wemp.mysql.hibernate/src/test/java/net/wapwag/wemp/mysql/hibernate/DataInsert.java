package net.wapwag.wemp.mysql.hibernate;

import net.wapwag.wemp.dao.model.ObjectData;
import net.wapwag.wemp.dao.model.ObjectType;
import net.wapwag.wemp.dao.model.geo.Country;
import net.wapwag.wemp.dao.model.link.*;
import net.wapwag.wemp.dao.model.org.Organization;
import net.wapwag.wemp.dao.model.org.WaterManageAuth;
import net.wapwag.wemp.dao.model.permission.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Data insert before other test,
 * <p>Use {@link org.junit.Assert} instead of .{@link junit.framework.TestCase} which suggested for the junit4</p>
 * Created by Administrator on 2016/10/31 0031.
 */
class DataInsert {

    private static final long orgId = 4L;

    private static EntityManager em;

    static {
        try {
            em = PrepareContext.createEMF().createEntityManager();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void initialData() {
        EntityTransaction tx = em.getTransaction();
        tx.begin();

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

        tx.commit();
    }

    private static void testAddUserOrg() {

        int i;
        User user;

        for (i = 0; i < 10; i++) {
            user = new User();
            user.setName("admin" + i);
            em.persist(user);
        }

        Organization organization;
        for (i = 0; i < 10; i++) {
            organization = new WaterManageAuth();
            organization.setName("研发部" + i);
            em.persist(organization);
        }

    }

    private static void testAddObject() {

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

    private static void testAddGroup() {
        Group group;
        int addCount = 21;

        for (int i = 0; i < addCount; i++) {
            group = new Group();
            group.setName("研发组" + i);
            em.persist(group);
        }

        em.flush();

        long count = em.createQuery("select count(g.id) from Group g", Long.class).getSingleResult();

        assertTrue(count == addCount);
    }

    private static void testAddRegisteredClient() {
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

    private static void testAddAccessToken() {
        List<User> userList = em.createQuery("select user from User user", User.class)
                .getResultList();

        List<RegisteredClient> clientList = em.createQuery("select client from RegisteredClient client", RegisteredClient.class)
                .getResultList();

        AccessToken accessToken = null;

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

        assert accessToken != null;
        accessToken.setHandle("authz_handle");
        accessToken.setAuthrizationCode("authz_code");

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
                groupObject.setActionId("read");
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
        Query query1 = em.createQuery("update Group g set g.organization.id = :orgId where g.id > 11")
                .setParameter("orgId", orgId);

        Query query2 = em.createQuery("update Group g set g.organization.id = :orgId where g.id <= 11")
                .setParameter("orgId", 5L);

        query1.executeUpdate();
        query2.executeUpdate();
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

        group = new Group();
        group.setId(1L);
        for (User user : userList) {
            userGroup = new UserGroup();
            userGroup.setUserGroupId(new UserGroupId(user, group));
            em.persist(userGroup);
        }

        group = new Group();
        group.setId(12L);
        for (User user : userList) {
            userGroup = new UserGroup();
            userGroup.setUserGroupId(new UserGroupId(user, group));
            em.persist(userGroup);
        }

    }

    private static void testAddUserByOrg() {
        List<User> userList = em.createQuery("select user from User user", User.class).getResultList();

        List<Organization> orgList = em.createQuery("select org from Organization org", Organization.class).getResultList();

        UserOrg userOrg;
        for (User user : userList) {
            for (Organization org : orgList) {
                userOrg = new UserOrg();
                userOrg.setUserOrgId(new UserOrgId(user, org));
                em.persist(userOrg);
            }
        }
    }

    private static void testAddObjectByOrg() {
        List<Organization> orgList = em.createQuery("select org from Organization org", Organization.class).getResultList();

        List<ObjectData> objList = em.createQuery("select obj from ObjectData obj where obj.type <> :type", ObjectData.class)
                .setParameter("type", ObjectType.WATER_MANAGE_AUTH)
                .setFirstResult(0).setMaxResults(10).getResultList();

        OrgObject orgObject;
        for (ObjectData objectData : objList) {
            for (Organization org : orgList) {
                orgObject = new OrgObject();
                orgObject.setOrgObjectId(new OrgObjectId(org, objectData));
                em.persist(orgObject);
            }
        }
    }
}
