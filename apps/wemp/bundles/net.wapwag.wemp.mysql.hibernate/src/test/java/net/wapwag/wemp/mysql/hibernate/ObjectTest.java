package net.wapwag.wemp.mysql.hibernate;

import net.wapwag.wemp.dao.model.ObjectData;
import net.wapwag.wemp.dao.model.permission.Group;
import net.wapwag.wemp.dao.model.permission.User;
import org.junit.Test;

import javax.persistence.Query;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Test object
 * Created by Administrator on 2016/10/27 0027.
 */
public class ObjectTest extends BaseTestConfig {

    @Test
    public void testGetUsersByObject() {
        long objId = 12L;
        String action = "read";
        
        String hql = "select userObject.userObjectId.user from UserObject userObject " +
                "where userObject.userObjectId.objectData.id = :objId " +
                "and userObject.actionId = :action";
        List<User> userList = em.createQuery(hql, User.class)
                .setParameter("objId", objId)
                .setParameter("action", action).getResultList();
        
        assertTrue(userList != null && userList.size() > 0);
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

        assertTrue(objectData != null);
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

        assertTrue(query.executeUpdate() > 0);
    }

    @Test
    public void testGetGroupsByObject() {
        long objId = 1L;

        String hql = "select groupObject.groupObjectId.group from GroupObject groupObject where groupObject.groupObjectId.objectData.id = :id";
        List<Group> groupList = em.createQuery(hql, Group.class).setParameter("id", objId).getResultList();

        assertTrue(groupList.size() > 0);
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

        assertTrue(objectData != null);
    }



    @Test
    public void testRemoveObjectByGroup() {
        long groupId = 2L;

        Group group = new Group();
        group.setId(groupId);

        Query query =  em.createQuery("delete from GroupObject groupObject where groupObject.groupObjectId.group = :group")
                .setParameter("group", group);

        int removeCount = query.executeUpdate();


        assertTrue(removeCount > 0);

    }

}
