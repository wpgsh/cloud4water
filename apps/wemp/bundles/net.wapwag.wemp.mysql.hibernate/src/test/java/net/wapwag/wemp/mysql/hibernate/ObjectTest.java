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
        long objectId = 12L;
        long userId = 1L;
        String actionId = "read";
        String hql = "";

        ObjectData objectData = em.find(ObjectData.class, objectId);
        User user = em.find(User.class, userId);
        UserObject userObject = em.find(UserObject.class, new UserObjectId(user, objectData));

        TestCase.assertTrue(userObject != null && actionId.equals(userObject.getActionId()));
    }

    @Test
    public void testAddObjectByUser() {
        long objectId = 12L;
        long userId = 2L;
        String actionId = "write";
        int transitive = 0;
        ObjectData objectData = em.find(ObjectData.class, objectId);
        User user = em.find(User.class, userId);

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
        String actionId = "write";

        ObjectData objectData = new ObjectData();
        objectData.setId(objectId);
        User user = new User();
        user.setId(userId);

        UserObject userObject = new UserObject();
        userObject.setUserObjectId(new UserObjectId(user, objectData));
        userObject.setActionId(actionId);

        userObject = em.find(UserObject.class, new UserObjectId(user, objectData));

        em.remove(userObject);

        userObject = em.find(UserObject.class, new UserObjectId(user, objectData));

        TestCase.assertTrue(userObject == null);
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
        long objId = 1L;
        long groupId = 13L;

        String hql = "select groupObject from GroupObject groupObject " +
                "where groupObject.groupObjectId.objectData.id = :objId " +
                "AND groupObject.groupObjectId.group.id = :groupId";
        List<GroupObject> permissionList = em.createQuery(hql, GroupObject.class)
                .setParameter("objId", objId)
                .setParameter("groupId", groupId).getResultList();

        TestCase.assertTrue(permissionList.size() > 0);
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
        long groudId = 2L;

        Group group = new Group();
        group.setId(groudId);

        Query query =  em.createQuery("delete from GroupObject groupObject where groupObject.groupObjectId.group = :group")
                .setParameter("group", group);

        int removeCount = query.executeUpdate();


        TestCase.assertTrue(removeCount > 0);

    }

}
