package net.wapwag.wemp.rest;

import com.google.gson.reflect.TypeToken;
import net.wapwag.wemp.WaterEquipmentServiceException;
import net.wapwag.wemp.dao.WaterEquipmentDaoException;
import net.wapwag.wemp.model.GroupView;
import net.wapwag.wemp.model.ObjectView;
import net.wapwag.wemp.model.ResultView;
import net.wapwag.wemp.model.UserView;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.lang.reflect.Type;
import java.util.List;

import static net.wapwag.wemp.rest.MockData.*;
import static org.eclipse.jetty.http.HttpStatus.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

/**
 * Object resource test
 * Created by Administrator on 2016/11/7 0007.
 */
public class OrgGroupResourceTest extends BaseResourceTest {

    private String path;

    @Override
    ResourceConfig initResource() {
        return new ResourceConfig(OrgGroupResourceMock.class);
    }

//    @Test
//    public void testGetGroupsByOrg() throws Exception {
//        when(waterEquipmentDao.getGroupsByOrg(orgId)).thenReturn(groupList);
//
//        List<GroupView> groupViewList = waterEquipmentService.getGroupsByOrg(orgId);
//
//        assertNotNull(groupViewList);
//        assertEquals(count, groupViewList.size());
//    }
//
//    @Test
//    public void testGetGroupsByOrg_Exception() throws Exception {
//        when(waterEquipmentDao.getGroupsByOrg(orgId)).thenThrow(WaterEquipmentDaoException.class);
//
//        List<GroupView> groupViewList = waterEquipmentService.getGroupsByOrg(orgId);
//
//        assertNull(groupViewList);
//    }
//
//    @Test
//    public void testAddGroupByOrg() throws Exception {
//        when(waterEquipmentDao.addGroupByOrg(orgId, group)).thenReturn(addCount);
//
//        ResultView resultView = waterEquipmentService.addGroupByOrg(orgId, group);
//
//        assertNotNull(resultView);
//        assertEquals(addCount, resultView.count);
//    }
//
//    @Test
//    public void testAddGroupByOrg_Exception() throws Exception {
//        when(waterEquipmentDao.addGroupByOrg(orgId, group)).thenThrow(WaterEquipmentDaoException.class);
//
//        ResultView resultView = waterEquipmentService.addGroupByOrg(orgId, group);
//
//        assertNull(resultView);
//    }
//
//    @Test
//    public void testGetGroupByOrg() throws Exception {
//        when(waterEquipmentDao.getGroupByOrg(orgId, groupId)).thenReturn(group);
//
//        GroupView groupView = waterEquipmentService.getGroupByOrg(orgId, groupId);
//
//        assertNotNull(groupView);
//        assertEquals(group.getId(), groupView.id);
//        assertEquals(group.getName(), groupView.name);
//    }
//
//    @Test
//    public void testGetGroupByOrg_Exception() throws Exception {
//        when(waterEquipmentDao.getGroupByOrg(orgId, groupId)).thenThrow(WaterEquipmentDaoException.class);
//
//        GroupView groupView = waterEquipmentService.getGroupByOrg(orgId, groupId);
//
//        assertNull(groupView);
//    }
//
//    @Test
//    public void testUpdateGroupByOrg() throws Exception {
//        when(waterEquipmentDao.updateGroupByOrg(orgId, groupId, group)).thenReturn(updateCount);
//
//        ResultView resultView = waterEquipmentService.updateGroupByOrg(orgId, groupId, group);
//
//        assertNotNull(resultView);
//        assertEquals(updateCount, resultView.count);
//    }
//
//    @Test
//    public void testUpdateGroupByOrg_Exception() throws Exception {
//        when(waterEquipmentDao.updateGroupByOrg(orgId, groupId, group)).thenThrow(WaterEquipmentDaoException.class);
//
//        ResultView resultView = waterEquipmentService.updateGroupByOrg(orgId, groupId, group);
//
//        assertNull(resultView);
//    }
//
//    @Test
//    public void removeGroupByOrg() throws Exception {
//        when(waterEquipmentDao.removeGroupByOrg(orgId, groupId)).thenReturn(removeCount);
//
//        ResultView resultView = waterEquipmentService.removeGroupByOrg(orgId, groupId);
//
//        assertNotNull(resultView);
//        assertEquals(removeCount, resultView.count);
//    }
//
//    @Test
//    public void removeGroupByOrg_Exception() throws Exception {
//        when(waterEquipmentDao.removeGroupByOrg(orgId, groupId)).thenThrow(WaterEquipmentDaoException.class);
//
//        ResultView resultView = waterEquipmentService.removeGroupByOrg(orgId, groupId);
//
//        assertNull(resultView);
//    }
//
//    @Test
//    public void testGetUsersByGroup() throws Exception {
//        when(waterEquipmentDao.getUsersByGroup(objId, groupId)).thenReturn(userList);
//
//        List<UserView> userViewList = waterEquipmentService.getUsersByGroup(orgId, groupId);
//
//        assertNotNull(userViewList);
//        assertEquals(userList.size(), userViewList.size());
//    }
//
//    @Test
//    public void testGetUsersByGroup_Exception() throws Exception {
//        when(waterEquipmentDao.getUsersByGroup(objId, groupId)).thenThrow(WaterEquipmentDaoException.class);
//
//        List<UserView> userViewList = waterEquipmentService.getUsersByGroup(orgId, groupId);
//
//        assertNull(userViewList);
//    }
//
//    @Test
//    public void testAddUserByGroup() throws Exception {
//        when(waterEquipmentDao.addUserByGroup(orgId, groupId, userId)).thenReturn(addCount);
//
//        ResultView resultView = waterEquipmentService.addUserByGroup(orgId, groupId, userId);
//
//        assertNotNull(resultView);
//        assertEquals(addCount, resultView.count);
//    }
//
//    @Test
//    public void testAddUserByGroup_Exception() throws Exception {
//        when(waterEquipmentDao.addUserByGroup(orgId, groupId, userId)).thenThrow(WaterEquipmentDaoException.class);
//
//        ResultView resultView = waterEquipmentService.addUserByGroup(orgId, groupId, userId);
//
//        assertNull(resultView);
//    }
//
//    @Test
//    public void testRemoveUserByGroup() throws Exception {
//        when(waterEquipmentDao.removeUserByGroup(orgId, groupId, userId)).thenReturn(removeCount);
//
//        ResultView resultView = waterEquipmentService.removeUserByGroup(orgId, groupId, userId);
//
//        assertNotNull(resultView);
//        assertEquals(removeCount, resultView.count);
//    }
//
//    @Test
//    public void testRemoveUserByGroup_Exception() throws Exception {
//        when(waterEquipmentDao.removeUserByGroup(orgId, groupId, userId)).thenThrow(WaterEquipmentDaoException.class);
//
//        ResultView resultView = waterEquipmentService.removeUserByGroup(orgId, groupId, userId);
//
//        assertNull(resultView);
//    }
//
//    @Test
//    public void testGetObjectsByGroup() throws Exception {
//        when(waterEquipmentDao.getObjectsByGroup(orgId, groupId)).thenReturn(objectDataList);
//
//        List<ObjectView> objectViews = waterEquipmentService.getObjectsByGroup(orgId, groupId);
//
//        assertNotNull(objectViews);
//        assertEquals(objectDataList.size(), objectViews.size());
//    }
//
//    @Test
//    public void testGetObjectsByGroup_Exception() throws Exception {
//        when(waterEquipmentDao.getObjectsByGroup(orgId, groupId)).thenThrow(WaterEquipmentDaoException.class);
//
//        List<ObjectView> objectViews = waterEquipmentService.getObjectsByGroup(orgId, groupId);
//
//        assertNull(objectViews);
//    }
//
//    @Test
//    public void testGetObjectByGroupWithAction() throws Exception {
//        when(waterEquipmentDao.getObjectByGroup(orgId, groupId, objId, action)).thenReturn(objectData);
//
//        ObjectView objectView = waterEquipmentService.getObjectByGroup(orgId, groupId, objId, action);
//
//        assertNotNull(objectView);
//        assertEquals(objectData.getId(), objectView.id);
//        assertEquals(objectData.getName(), objectView.name);
//        assertEquals(objectData.getType(), objectView.objectType);
//    }
//
//    @Test
//    public void testGetObjectByGroupWithAction_Exception() throws Exception {
//        when(waterEquipmentDao.getObjectByGroup(orgId, groupId, objId, action)).thenThrow(WaterEquipmentDaoException.class);
//
//        ObjectView objectView = waterEquipmentService.getObjectByGroup(orgId, groupId, objId, action);
//
//        assertNull(objectView);
//    }
//
//    @Test
//    public void testGetUsersByOrg() throws Exception {
//        when(waterEquipmentDao.getUsersByOrg(orgId)).thenReturn(userList);
//
//        List<UserView> userViews = waterEquipmentService.getUsersByOrg(orgId);
//
//        assertNotNull(userViews);
//        assertEquals(userList.size(), userViews.size());
//    }
//
//    @Test
//    public void testGetUsersByOrg_Exception() throws Exception {
//        when(waterEquipmentDao.getUsersByOrg(orgId)).thenThrow(WaterEquipmentDaoException.class);
//
//        List<UserView> userViews = waterEquipmentService.getUsersByOrg(orgId);
//
//        assertNull(userViews);
//    }
//
//    @Test
//    public void testAddUserByOrg() throws Exception {
//        when(waterEquipmentDao.addUserByOrg(orgId, user)).thenReturn(addCount);
//
//        ResultView resultView = waterEquipmentService.addUserByOrg(orgId, user);
//
//        assertNotNull(resultView);
//        assertEquals(addCount, resultView.count);
//    }
//
//    @Test
//    public void testAddUserByOrg_Exception() throws Exception {
//        when(waterEquipmentDao.addUserByOrg(orgId, user)).thenThrow(WaterEquipmentDaoException.class);
//
//        ResultView resultView = waterEquipmentService.addUserByOrg(orgId, user);
//
//        assertNull(resultView);
//    }
//
//    @Test
//    public void testRemoveUserByOrg() throws Exception {
//        when(waterEquipmentDao.removeUserByOrg(orgId, userId)).thenReturn(removeCount);
//
//        ResultView resultView = waterEquipmentService.removeUserByOrg(orgId, userId);
//
//        assertNotNull(resultView);
//        assertEquals(addCount, resultView.count);
//    }
//
//    @Test
//    public void testRemoveUserByOrg_Exception() throws Exception {
//        when(waterEquipmentDao.removeUserByOrg(orgId, userId)).thenThrow(WaterEquipmentDaoException.class);
//
//        ResultView resultView = waterEquipmentService.removeUserByOrg(orgId, userId);
//
//        assertNull(resultView);
//    }
//
//    @Test
//    public void testGetObjectsByOrg() throws Exception {
//        when(waterEquipmentDao.getObjectsByOrg(orgId)).thenReturn(objectDataList);
//
//        List<ObjectView> objectViews = waterEquipmentService.getObjectsByOrg(orgId);
//
//        assertNotNull(objectViews);
//        assertEquals(objectDataList.size(), objectViews.size());
//    }
//
//    @Test
//    public void testGetObjectsByOrg_Exception() throws Exception {
//        when(waterEquipmentDao.getObjectsByOrg(orgId)).thenThrow(WaterEquipmentDaoException.class);
//
//        List<ObjectView> objectViews = waterEquipmentService.getObjectsByOrg(orgId);
//
//        assertNull(objectViews);
//    }
//
//    @Test
//    public void testAddObjectByOrg() throws Exception {
//        when(waterEquipmentDao.addObjectByOrg(orgId, objectData)).thenReturn(addCount);
//
//        ResultView resultView = waterEquipmentService.addObjectByOrg(orgId, objectData);
//
//        assertNotNull(resultView);
//        assertEquals(addCount, resultView.count);
//    }
//
//    @Test
//    public void testAddObjectByOrg_Exception() throws Exception {
//        when(waterEquipmentDao.addObjectByOrg(orgId, objectData)).thenThrow(WaterEquipmentDaoException.class);
//
//        ResultView resultView = waterEquipmentService.addObjectByOrg(orgId, objectData);
//
//        assertNull(resultView);
//    }
//
//    @Test
//    public void testRemoveObjectByOrg() throws Exception {
//        when(waterEquipmentDao.removeObjectByOrg(orgId, objId)).thenReturn(removeCount);
//
//        ResultView resultView = waterEquipmentService.removeObjectByOrg(orgId, objId);
//
//        assertNotNull(resultView);
//        assertEquals(removeCount, resultView.count);
//    }
//
//    @Test
//    public void testRemoveObjectByOrg_Exception() throws Exception {
//        when(waterEquipmentDao.removeObjectByOrg(orgId, objId)).thenThrow(WaterEquipmentDaoException.class);
//
//        ResultView resultView = waterEquipmentService.removeObjectByOrg(orgId, objId);
//
//        assertNull(resultView);
//    }
}
