package net.wapwag.wemp.h2.hibernate;

import net.wapwag.wemp.dao.WaterEquipmentDaoException;
import net.wapwag.wemp.dao.model.ObjectData;
import net.wapwag.wemp.dao.model.ObjectType;
import net.wapwag.wemp.dao.model.permission.*;
import org.junit.Test;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * Water equipment dao test
 * Created by Administrator on 2016/11/7 0007.
 */
public class WaterEquipmentDaoTest extends BaseTestConfig {

    private static final long orgId = 4L;
    private static final long userId = 1L;

    private static final long removeOrgId = 5L;

    private static final int addCount = 1;
    private static final int removeCount = 1;
    private static final int updateCount = 1;

    private int count = 1;


    @Test
    public void testIsAuthorized() throws Exception {
        String action = "read";
        long objectId = 12L;

        boolean isAuthorized = waterEquipmentDao.isAuthorized(userId, action, objectId);

        assertTrue(isAuthorized);
    }

    @Test
    public void testLookupAccessToken() throws Exception {
        final String handle = "authz_handle";

        AccessToken accessToken = waterEquipmentDao.lookupAccessToken(handle);

        assertNotNull(accessToken);
    }

    @Test
    public void testGetClientByRedirectURI() throws Exception {
        String redirectURI = "http://www.baidu.com?q=0";

        RegisteredClient client = waterEquipmentDao.getClientByRedirectURI(redirectURI);

        assertNotNull(client);
    }

    @Test
    public void testGetAccessTokenByUserIdAndClientId() throws Exception {
        long clientId = 1L;

        AccessToken accessToken = waterEquipmentDao.getAccessTokenByUserIdAndClientId(userId, clientId);

        assertNotNull(accessToken);
    }

    @Test
    public void testGetAccessTokenByCode() throws Exception {
        final String code = "authz_code";

        AccessToken accessToken = waterEquipmentDao.getAccessTokenByCode(code);

        assertNotNull(accessToken);
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

        long count = waterEquipmentDao.saveAccessToken(accessToken);

        assertTrue(count >= addCount);
    }

    @Test
    public void getUser() throws Exception {
        User user = waterEquipmentDao.getUser(userId);

        assertNotNull(user);
    }

    @Test
    public void testGetUsersByObject() throws WaterEquipmentDaoException {
        long objId = 12L;
        String action = "read";

        List<User> userList = waterEquipmentDao.getUsersByObject(objId, action);

        assertTrue(userList != null && userList.size() > 0);
    }

    @Test
    public void testGetObjectByUser() throws WaterEquipmentDaoException {
        long objectId = 1L;
        long userId = 1L;

        ObjectData objectData = waterEquipmentDao.getObjectByUser(objectId, userId);

        assertTrue(objectData != null);
    }

    @Test
    public void testAddObjectByUser() throws WaterEquipmentDaoException {
        long objectId = 12L;
        long userId = 2L;

        count = waterEquipmentDao.addObjectByUser(objectId, userId);

        assertEquals(addCount, count);
    }

    @Test
    public void testRemoveObjectByUser() throws WaterEquipmentDaoException {
        long objectId = 12L;
        long userId = 2L;
        String action = "write";

        count = waterEquipmentDao.removeObjectByUser(objectId, userId, action);

        assertEquals(removeCount, count);
    }

    @Test
    public void testGetGroupsByObject() throws WaterEquipmentDaoException {
        long objId = 1L;
        String action = "read";

        List<Group> groupList = waterEquipmentDao.getGroupsByObject(objId, action);

        assertNotNull(groupList);
        assertTrue(groupList.size() > 0);
    }

    @Test
    public void testGetObjectByGroup_WithOutAction() throws WaterEquipmentDaoException {
        long objId = 2L;
        long groupId = 11L;

        ObjectData objectData = waterEquipmentDao.getObjectByGroup(objId, groupId);

        assertNotNull(objectData);
    }

    @Test
    public void testAddObjectByGroup() throws WaterEquipmentDaoException {
        long groupId = 11L;

        count = waterEquipmentDao.addObjectByGroup(orgId, groupId);

        assertEquals(addCount, count);
    }

    @Test
    public void testRemoveObjectByGroup() throws WaterEquipmentDaoException {
        long objId = 1L;
        long groupId = 2L;
        String action = "read";

        count = waterEquipmentDao.removeObjectByGroup(objId, groupId, action);

        assertEquals(count, removeCount);
    }

    @Test
    public void testGetGroupsByOrg() throws WaterEquipmentDaoException {
        List<Group> groupList = waterEquipmentDao.getGroupsByOrg(orgId);

        assertTrue(groupList != null);
    }

    @Test
    public void testAddGroupByOrg() throws WaterEquipmentDaoException {

    }

    @Test
    public void testGetGroupByOrg() throws WaterEquipmentDaoException {
        long groupId = 12L;

        Group group = waterEquipmentDao.getGroupByOrg(orgId, groupId);

        assertTrue(group != null);
    }

    @Test
    public void testUpdateGroupByOrg() throws WaterEquipmentDaoException {
        long groupId = 12L;

        Group group = new Group();
        group.setName("添加组到研发部");

        count = waterEquipmentDao.updateGroupByOrg(orgId, groupId , group);

        assertEquals(updateCount, count);
    }

    @Test
    public void testRemoveGroupByOrg() throws WaterEquipmentDaoException {
        long groupId = 10L;

        count = waterEquipmentDao.removeGroupByOrg(removeOrgId, groupId);

        assertEquals(removeCount, count);
    }

    @Test
    public void testGetUsersByGroup() throws WaterEquipmentDaoException {
        long groupId = 12L;

        List<User> userList = waterEquipmentDao.getUsersByGroup(orgId, groupId);

        assertNotNull(userList);
        assertTrue(userList.size() > 0);
    }

    @Test
    public void testAddUserByGroup() throws WaterEquipmentDaoException {
        long groupId = 12L;

        count = waterEquipmentDao.addUserByGroup(orgId, groupId, userId);

        assertEquals(addCount, count);
    }

    @Test
    public void testRemoveUserByGroup() throws WaterEquipmentDaoException {
        long groupId = 2L;
        long userId = 1L;
        long orgId = 5L;

        count = waterEquipmentDao.removeUserByGroup(orgId, groupId, userId);

        assertEquals(removeCount, count);
    }

    @Test
    public void testGetObjectsByGroup() throws WaterEquipmentDaoException {
        long groupId = 12L;

        List<ObjectData> objList = waterEquipmentDao.getObjectsByGroup(orgId, groupId);

        assertNotNull(objList);
        assertTrue(objList.size() > 0);
    }

    @Test
    public void testGetObjectByGroup() throws WaterEquipmentDaoException {
        long groupId = 12L;
        long objId = 1L;
        String action = "read";

        ObjectData objectData = waterEquipmentDao.getObjectByGroup(orgId, groupId, objId, action);

        assertTrue(objectData != null);
    }

    @Test
    public void testGetUsersByOrg() throws WaterEquipmentDaoException {
        List<User> userList = waterEquipmentDao.getUsersByOrg(orgId);

        assertNotNull(userList);
        assertTrue(userList.size() > 0);
    }

    @Test
    public void testAddUserByOrg() throws WaterEquipmentDaoException {
        long groupId = 12L;

        count = waterEquipmentDao.addUserByGroup(orgId, userId, groupId);

        assertEquals(addCount, count);
    }

    @Test
    public void testRemoveUserByOrg() throws WaterEquipmentDaoException {
        long userId = 1L;

        count = waterEquipmentDao.removeUserByOrg(removeOrgId, userId);

        assertEquals(removeCount, count);
    }

    @Test
    public void testGetObjectsByOrg() throws WaterEquipmentDaoException {
        List<ObjectData> objList = waterEquipmentDao.getObjectsByOrg(orgId);

        assertNotNull(objList);
        assertTrue(objList.size() > 0);
    }

    @Test
    public void testAddObjectByOrg() throws WaterEquipmentDaoException {
        ObjectData objectData = new ObjectData();
        objectData.setName("ShangHai");
        objectData.setType(ObjectType.PROVINCE);

        count = waterEquipmentDao.addObjectByOrg(orgId, objectData);

        assertEquals(addCount, count);
    }

    @Test
    public void testRemoveObjectByOrg() throws WaterEquipmentDaoException {
        long objId = 1L;

        count = waterEquipmentDao.removeObjectByOrg(orgId, objId);

        assertEquals(removeCount, count);
    }

    @Test
    public void testCheckPermission() throws WaterEquipmentDaoException {
        long objId = 11L;
        ObjectData objectData = new ObjectData();
        objectData.setId(objId);

        boolean havePermission = waterEquipmentDao.checkPermission(userId, objectData);

        assertTrue(havePermission);
    }

    @Test
    public void testGetObjectsByUser() throws WaterEquipmentDaoException {
        long userId = 1L;
        String action = "read";

        Set<ObjectData> objectDataSet = waterEquipmentDao.getObjectsByUser(userId, action);

        assertNotNull(objectDataSet);
        assertTrue(objectDataSet.size() > 0);
    }
}
