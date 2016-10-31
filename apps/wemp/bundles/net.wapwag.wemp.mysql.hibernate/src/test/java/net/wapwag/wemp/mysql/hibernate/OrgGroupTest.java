package net.wapwag.wemp.mysql.hibernate;

import junit.framework.TestCase;
import net.wapwag.wemp.dao.model.ObjectData;
import net.wapwag.wemp.dao.model.link.UserGroup;
import net.wapwag.wemp.dao.model.link.UserGroupId;
import net.wapwag.wemp.dao.model.org.Organization;
import net.wapwag.wemp.dao.model.permission.Group;
import net.wapwag.wemp.dao.model.permission.User;
import org.junit.Test;

import javax.persistence.Query;
import java.util.List;

/**
 * Test organization and group query
 * Created by Administrator on 2016/10/27 0027.
 */
public class OrgGroupTest extends BaseTestConfig {

    private static final long orgId = 1L;

    @Test
    public void testGetGroupsByOrg() {
        final String hql = "select g from Group g where g.organization.id = :orgId";
        List<Group> groupList = em.createQuery(hql, Group.class)
                .setParameter("orgId", orgId).getResultList();

        TestCase.assertTrue(groupList != null);
    }

    @Test
    public void testAddGroupByObject() {
        Group group = em.find(Group.class, 2L);

        Organization org = new Organization();
        org.setId(orgId);
        group.setOrganization(org);

        em.persist(group);
    }

    @Test
    public void testGetGroupByOrg() {
        long groupId = 2L;

        String hql = "select g from Group g where g.id = :groupId and g.organization.id = :orgId";

        Group group = em.createQuery(hql, Group.class)
                .setParameter("groupId", groupId)
                .setParameter("orgId", orgId).getSingleResult();

        TestCase.assertTrue(group != null);
    }

    @Test
    public void testUpdateGroupByOrg() {
        long groupId = 2L;

        String hql = "select g from Group g where g.id = :groupId and g.organization.id = :orgId";

        Group group = em.createQuery(hql, Group.class)
                .setParameter("groupId", groupId)
                .setParameter("orgId", orgId).getSingleResult();

        group.setName("组被修改");

        em.persist(group);

        em.flush();
    }

    @Test
    public void testRemoveGroupByOrg() {
        long groupId = 2L;

        String hql = "delete from Group g where g.id = :groupId and g.organization.id = :orgId";

        Query query = em.createQuery(hql).setParameter("groupId", groupId)
                .setParameter("orgId", orgId);

        query.executeUpdate();
    }

    @Test
    public void testGetUsersByGroup() {
        long groupId = 2L;
        String hql = "select userGroup.userGroupId.user from UserGroup userGroup " +
                "where userGroup.userGroupId.group.id = :groupId " +
                "and userGroup.userGroupId.group.organization.id = :orgId";

        List<User> userList = em.createQuery(hql, User.class)
                .setParameter("orgId", orgId)
                .setParameter("groupId", groupId).getResultList();

        TestCase.assertTrue(userList != null && userList.size() > 0);
    }

    @Test
    public void testAddUserByGroup() {
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

    @Test
    public void testRemoveUserByGroup() {
        long groupId = 2L;
        long userId = 1L;

        String hql = "select userGroup from UserGroup userGroup " +
                "where userGroup.userGroupId.group.id = :groupId " +
                "and userGroup.userGroupId.group.organization.id = :orgId " +
                "and userGroup.userGroupId.user.id = :userId";

        UserGroup userGroup = em.createQuery(hql, UserGroup.class)
                .setParameter("orgId", orgId)
                .setParameter("groupId", groupId)
                .setParameter("userId", userId).getSingleResult();

        em.remove(userGroup);
    }

    @Test
    public void testGetObjectsByGroup() {
        long groupId = 2L;
        String hql = "select groupObject.groupObjectId.objectData from GroupObject  groupObject " +
                "where groupObject.groupObjectId.group.id = :groupId " +
                "and groupObject.groupObjectId.group.organization.id = :orgId";

        Group group = new Group();
        group.setId(groupId);

        List<ObjectData> objList = em.createQuery(hql, ObjectData.class)
                .setParameter("groupId", groupId)
                .setParameter("orgId", orgId).getResultList();

        TestCase.assertTrue(objList != null && objList.size() > 0);
    }

    @Test
    public void testGetObjectByGroup() {
        long groupId = 2L;
        long objId = 1L;

        String hql = "select groupObject.groupObjectId.objectData from GroupObject  groupObject " +
                "where groupObject.groupObjectId.group.id = :groupId " +
                "and groupObject.groupObjectId.group.organization.id = :orgId " +
                "and groupObject.groupObjectId.objectData.id = :objId";

        Group group = new Group();
        group.setId(groupId);

        ObjectData objectData = em.createQuery(hql, ObjectData.class)
                .setParameter("groupId", groupId)
                .setParameter("orgId", orgId)
                .setParameter("objId", objId).getSingleResult();

        TestCase.assertTrue(objectData != null);
    }

    @Test
    public void testGetUsersByOrg() {
        String hql = "select userOrg.userOrgId.user from UserOrg userOrg where userOrg.userOrgId.organization.id = :orgId";
        List<User> userList = em.createQuery(hql, User.class)
                .setParameter("orgId", orgId).getResultList();

        TestCase.assertTrue(userList != null && userList.size() > 0);
    }

    @Test
    public void testAddUserByOrg() {

    }

    @Test
    public void testRemoveUserByOrg() {

    }

    @Test
    public void testGetObjectsByOrg() {

    }

    @Test
    public void testAddObjectByOrg() {

    }

    @Test
    public void testRemoveObjectByOrg() {

    }

}
