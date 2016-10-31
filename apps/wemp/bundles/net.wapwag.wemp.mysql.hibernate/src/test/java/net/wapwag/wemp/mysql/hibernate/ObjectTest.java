package net.wapwag.wemp.mysql.hibernate;

import junit.framework.TestCase;
import net.wapwag.wemp.dao.model.ObjectData;
import net.wapwag.wemp.dao.model.link.GroupObject;
import net.wapwag.wemp.dao.model.link.GroupObjectId;
import net.wapwag.wemp.dao.model.link.UserObject;
import net.wapwag.wemp.dao.model.link.UserObjectId;
import net.wapwag.wemp.dao.model.permission.Group;
import net.wapwag.wemp.dao.model.permission.User;
import org.junit.Test;

import javax.persistence.Query;
import java.util.List;

/**
 * Test object
 * Created by Administrator on 2016/10/27 0027.
 */
public class ObjectTest extends BaseTestConfig {

    @Test
    public void addUserObject() {
        User user = em.find(User.class, 1L);
        List<ObjectData> objSet = em.createQuery("select obj from ObjectData obj", ObjectData.class).getResultList();
        UserObject userObject = null;

        for (ObjectData obj : objSet) {
            userObject = new UserObject();
            userObject.setUserObjectId(new UserObjectId(user, obj));
            userObject.setActionId("read");
            userObject.setTransitive(0);
            em.persist(userObject);
        }
    }

    @Test
    public void testGetUsersByObject() {
        ObjectData objectData = em.find(ObjectData.class, 12L);
        String sql = "select uo.userObjectId.user from UserObject uo " +
                "where uo.userObjectId.objectData = :obj and uo.actionId = :actionId";
        List<User> userList = em.createQuery(sql, User.class)
                .setParameter("obj", objectData)
                .setParameter("actionId", "read").getResultList();
        TestCase.assertTrue(userList != null && userList.size() > 0);
    }

    @Test
    public void testGetObjectByUser() {
        long objectId = 1L;
        long userId = 1L;

        String hql = "select uo.userObjectId.objectData from UserObject uo " +
                "where uo.userObjectId.objectData.id = :objId and uo.userObjectId.user.id = :userId";

        ObjectData objectData = em.createQuery(hql, ObjectData.class)
                .setParameter("objId", objectId)
                .setParameter("userId", userId).getSingleResult();

        TestCase.assertTrue(objectData != null);
    }

    @Test
    public void testAddObjectByUser() {
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

        TestCase.assertTrue(userObject != null);
    }

    @Test
    public void testRemoveObjectByUser() {
        long objectId = 12L;
        long userId = 2L;
        String action = "write";

        String hql = "delete from UserObject userObject where " +
                "userObject.userObjectId.user.id = :userId " +
                "and userObject.userObjectId.objectData.id = :objId";
        if (!action.isEmpty()) {
            hql  += " and userObject.actionId = :action";
        }

        Query query = em.createQuery(hql).setParameter("objId", objectId)
                .setParameter("userId", userId);

        if (!action.isEmpty()) {
            query.setParameter("action", action);
        }

        TestCase.assertTrue(query.executeUpdate() > 0);
    }

    @Test
    public void testGetGroupsByObject() {
        long objId = 1L;

        String hql = "select groupObject.groupObjectId.group from GroupObject groupObject where groupObject.groupObjectId.objectData.id = :id";
        List<Group> groupList = em.createQuery(hql, Group.class).setParameter("id", objId).getResultList();

        TestCase.assertTrue(groupList.size() > 0);
    }

    @Test
    public void testGetObjectByGroup() {
        long objId = 2L;
        long groupId = 11L;

        String hql = "select groupObject.groupObjectId.objectData from GroupObject groupObject " +
                "where groupObject.groupObjectId.objectData.id = :objId " +
                "AND groupObject.groupObjectId.group.id = :groupId";
        ObjectData objectData = em.createQuery(hql, ObjectData.class)
                .setParameter("objId", objId)
                .setParameter("groupId", groupId).getSingleResult();

        TestCase.assertTrue(objectData != null);
    }

    @Test
    public void testAddObjectByGroup() {

        List<Group> groupList = em.createQuery("select group from Group group", Group.class).getResultList();

        GroupObject groupObject = null;
        List<ObjectData> objList = em.createQuery("select obj from ObjectData obj", ObjectData.class)
                .setMaxResults(5).getResultList();


        for (Group group : groupList) {
            for (ObjectData objectData : objList) {
                groupObject = new GroupObject();
                groupObject.setGroupObjectId(new GroupObjectId(group, objectData));
                em.persist(groupObject);
            }
        }
    }

    @Test
    public void testRemoveObjectByGroup() {
        long groupId = 2L;

        Group group = new Group();
        group.setId(groupId);

        Query query =  em.createQuery("delete from GroupObject groupObject where groupObject.groupObjectId.group = :group")
                .setParameter("group", group);

        int removeCount = query.executeUpdate();


        TestCase.assertTrue(removeCount > 0);

    }

}
