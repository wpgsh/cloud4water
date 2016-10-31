package net.wapwag.wemp.mysql.hibernate;

import junit.framework.TestCase;
import net.wapwag.wemp.dao.model.ObjectData;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Test object
 * Created by Administrator on 2016/10/27 0027.
 */
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

        TestCase.assertTrue(havePermission > 0);
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

        TestCase.assertTrue(result.size() > 0);
    }

}
