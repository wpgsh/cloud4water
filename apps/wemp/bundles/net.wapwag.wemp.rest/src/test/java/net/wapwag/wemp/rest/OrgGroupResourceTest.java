package net.wapwag.wemp.rest;

import com.google.common.collect.Maps;
import com.google.gson.reflect.TypeToken;
import net.wapwag.wemp.WaterEquipmentServiceException;
import net.wapwag.wemp.dao.model.ObjectData;
import net.wapwag.wemp.dao.model.permission.Group;
import net.wapwag.wemp.dao.model.permission.User;
import net.wapwag.wemp.model.GroupView;
import net.wapwag.wemp.model.ObjectView;
import net.wapwag.wemp.model.ResultView;
import net.wapwag.wemp.model.UserView;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static net.wapwag.wemp.WempUtil.EMPTY_RETURN;
import static net.wapwag.wemp.rest.MockData.*;
import static net.wapwag.wemp.rest.OrgGroupResourceMock.mockService;
import static org.eclipse.jetty.http.HttpStatus.FORBIDDEN_403;
import static org.eclipse.jetty.http.HttpStatus.OK_200;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

/**
 * Object resource test
 * Created by Administrator on 2016/11/7 0007.
 */
public class OrgGroupResourceTest extends BaseResourceTest {

    private String path;

    @Test
    public void testGetGroupsByOrg_InvalidToken() throws Exception {
        path = String.format("/wemp/organization/%s/organizationGroups", orgId);

        Response response = invalidToken(target(path)).get();

        assertEquals(FORBIDDEN_403, response.getStatus());
    }

    @Test
    public void testGetGroupsByOrg_NotAuthorized() throws Exception {
        path = String.format("/wemp/organization/%s/organizationGroups", invalidId);

        Response response = validToken(target(path)).get();

        assertEquals(FORBIDDEN_403, response.getStatus());
    }

    @Test
    public void testGetGroupsByOrg() throws Exception {
        when(mockService.getGroupsByOrg(orgId))
                .thenReturn(groupList.stream().map(GroupView::newInstance).collect(Collectors.toList()));

        path = String.format("/wemp/organization/%s/organizationGroups", orgId);
        Type listType = new TypeToken<List<GroupView>>(){}.getType();

        Response response = validToken(target(path)).get();
        List<GroupView> groupViewList = getResult(response, listType);

        assertNotNull(groupViewList);
        assertEquals(count, groupViewList.size());
    }

    @Test
    public void testGetGroupsByOrg_Exception() throws Exception {
        when(mockService.getGroupsByOrg(invalidId)).thenThrow(WaterEquipmentServiceException.class);

        path = String.format("/wemp/organization/%s/organizationGroups", invalidId);
        Response response = validToken(target(path)).get();

        assertEquals(FORBIDDEN_403, response.getStatus());
    }

    @Test
    public void testAddGroupByOrg_InvalidToken() throws Exception {
        path = String.format("/wemp/organization/%s/organizationGroups", orgId);

        Response response = invalidToken(target(path)).post(null);

        assertEquals(FORBIDDEN_403, response.getStatus());
    }

    @Test
    public void testAddGroupByOrg_NotAuthorized() throws Exception {
        path = String.format("/wemp/organization/%s/organizationGroups", invalidId);

        Response response = validToken(target(path)).post(null);

        assertEquals(FORBIDDEN_403, response.getStatus());
    }

    @Test
    public void testAddGroupByOrg() throws Exception {
        when(mockService.addGroupByOrg(anyLong(), any(Group.class))).thenReturn(ResultView.newInstance(addCount));

        path = String.format("/wemp/organization/%s/organizationGroups", orgId);
        Response response = validToken(target(path)).post(Entity.entity(group, MediaType.APPLICATION_JSON));
        ResultView resultView = getResult(response, ResultView.class);

        assertNotNull(resultView);
        assertEquals(addCount, resultView.count);
    }

    @Test
    public void testAddGroupByOrg_Exception() throws Exception {
        when(mockService.addGroupByOrg(eq(invalidId), any(Group.class))).thenThrow(WaterEquipmentServiceException.class);

        path = String.format("/wemp/organization/%s/organizationGroups", invalidId);
        Response response = validToken(target(path)).post(Entity.entity(group, MediaType.APPLICATION_JSON));

        assertEquals(FORBIDDEN_403, response.getStatus());
    }

    @Test
    public void testGetGroupByOrg_InvalidToken() throws Exception {
        path = String.format("/wemp/organization/%s/organizationGroup/%s", orgId, groupId);

        Response response = invalidToken(target(path)).get();

        assertEquals(FORBIDDEN_403, response.getStatus());
    }

    @Test
    public void testGetGroupByOrg_NotAuthorized() throws Exception {
        path = String.format("/wemp/organization/%s/organizationGroup/%s", invalidId, invalidId);

        Response response = validToken(target(path)).get();

        assertEquals(FORBIDDEN_403, response.getStatus());
    }

    @Test
    public void testGetGroupByOrg() throws Exception {
        when(mockService.getGroupByOrg(orgId, groupId)).thenReturn(GroupView.newInstance(group));

        path = String.format("/wemp/organization/%s/organizationGroup/%s", orgId, groupId);
        Response response = validToken(target(path)).get();
        GroupView groupView = getResult(response, GroupView.class);

        assertNotNull(groupView);
        assertEquals(group.getId(), groupView.id);
        assertEquals(group.getName(), groupView.name);
    }

    @Test
    public void testGetGroupByOrg_Exception() throws Exception {
        when(mockService.getGroupByOrg(invalidId, invalidId)).thenThrow(WaterEquipmentServiceException.class);

        path = String.format("/wemp/organization/%s/organizationGroup/%s", invalidId, invalidId);
        Response response = validToken(target(path)).get();

        assertEquals(FORBIDDEN_403, response.getStatus());
    }

    @Test
    public void testUpdateGroupByOrg_InvalidToken() throws Exception {
        path = String.format("/wemp/organization/%s/organizationGroup/%s", orgId, groupId);

        Response response = invalidToken(target(path)).put(Entity.entity(group, MediaType.APPLICATION_JSON));

        assertEquals(FORBIDDEN_403, response.getStatus());
    }

    @Test
    public void testUpdateGroupByOrg_NotAuthorized() throws Exception {
        path = String.format("/wemp/organization/%s/organizationGroup/%s", invalidId, invalidId);

        Response response = validToken(target(path)).put(Entity.entity(group, MediaType.APPLICATION_JSON));

        assertEquals(FORBIDDEN_403, response.getStatus());
    }

    @Test
    public void testUpdateGroupByOrg() throws Exception {
        when(mockService.updateGroupByOrg(eq(orgId), eq(groupId), any(Group.class))).thenReturn(ResultView.newInstance(updateCount));

        path = String.format("/wemp/organization/%s/organizationGroup/%s", orgId, groupId);
        Response response = validToken(target(path)).put(Entity.entity(group, MediaType.APPLICATION_JSON));
        ResultView resultView = getResult(response, ResultView.class);

        assertNotNull(resultView);
        assertEquals(updateCount, resultView.count);
    }

    @Test
    public void testUpdateGroupByOrg_Exception() throws Exception {
        when(mockService.updateGroupByOrg(eq(invalidId), eq(invalidId), any(Group.class))).thenThrow(WaterEquipmentServiceException.class);

        path = String.format("/wemp/organization/%s/organizationGroup/%s", invalidId, invalidId);
        Response response = validToken(target(path)).put(Entity.entity(group, MediaType.APPLICATION_JSON));

        assertEquals(FORBIDDEN_403, response.getStatus());
    }

    @Test
    public void removeGroupByOrg_InvalidToken() throws Exception {
        path = String.format("/wemp/organization/%s/organizationGroup/%s", orgId, groupId);

        Response response = invalidToken(target(path)).delete();

        assertEquals(FORBIDDEN_403, response.getStatus());
    }

    @Test
    public void removeGroupByOrg_NotAuthorized() throws Exception {
        path = String.format("/wemp/organization/%s/organizationGroup/%s", invalidId, invalidId);

        Response response = validToken(target(path)).delete();

        assertEquals(FORBIDDEN_403, response.getStatus());
    }

    @Test
    public void removeGroupByOrg() throws Exception {
        when(mockService.removeGroupByOrg(orgId, groupId)).thenReturn(EMPTY_RETURN);

        path = String.format("/wemp/organization/%s/organizationGroup/%s", orgId, groupId);
        Response response = validToken(target(path)).delete();

        assertEquals(OK_200, response.getStatus());
    }

    @Test
    public void removeGroupByOrg_Exception() throws Exception {
        when(mockService.removeGroupByOrg(invalidId, invalidId)).thenThrow(WaterEquipmentServiceException.class);

        path = String.format("/wemp/organization/%s/organizationGroup/%s", invalidId, invalidId);
        Response response = validToken(target(path)).delete();

        assertEquals(FORBIDDEN_403, response.getStatus());
    }

    @Test
    public void testGetUsersByGroup_InvalidToken() throws Exception {
        path = String.format("/wemp/organization/%s/organizationGroup/%s/users", orgId, groupId);

        Response response = invalidToken(target(path)).get();

        assertEquals(FORBIDDEN_403, response.getStatus());
    }

    @Test
    public void testGetUsersByGroup_NotAuthorized() throws Exception {
        path = String.format("/wemp/organization/%s/organizationGroup/%s/users", invalidId, invalidId);

        Response response = validToken(target(path)).get();

        assertEquals(FORBIDDEN_403, response.getStatus());
    }

    @Test
    public void testGetUsersByGroup() throws Exception {
        when(mockService.getUsersByGroup(objId, groupId))
                .thenReturn(userList.stream().map(UserView::newInstance).collect(Collectors.toList()));

        path = String.format("/wemp/organization/%s/organizationGroup/%s/users", orgId, groupId);

        Type listType = new TypeToken<List<UserView>>(){}.getType();
        Response response = validToken(target(path)).get();
        List<UserView> userViewList = getResult(response, listType);

        assertNotNull(userViewList);
        assertEquals(userList.size(), userViewList.size());
    }

    @Test
    public void testGetUsersByGroup_Exception() throws Exception {
        when(mockService.getUsersByGroup(invalidId, invalidId)).thenThrow(WaterEquipmentServiceException.class);

        path = String.format("/wemp/organization/%s/organizationGroup/%s/users", invalidId, invalidId);
        Response response = validToken(target(path)).get();

        assertEquals(FORBIDDEN_403, response.getStatus());
    }

    @Test
    public void testAddUserByGroup_InvalidToken() throws Exception {
        path = String.format("/wemp/organization/%s/organizationGroup/%s/users", orgId, groupId);

        Response response = invalidToken(target(path)).post(null);

        assertEquals(FORBIDDEN_403, response.getStatus());
    }

    @Test
    public void testAddUserByGroup_NotAuthorized() throws Exception {
        path = String.format("/wemp/organization/%s/organizationGroup/%s/users", invalidId, invalidId);

        Response response = validToken(target(path)).post(null);

        assertEquals(FORBIDDEN_403, response.getStatus());
    }

    @Test
    public void testAddUserByGroup() throws Exception {
        when(mockService.addUserByGroup(eq(orgId), eq(groupId), any(User.class))).thenReturn(ResultView.newInstance(addCount));

        path = String.format("/wemp/organization/%s/organizationGroup/%s/users", orgId, groupId);
        Response response = validToken(target(path)).post(Entity.entity(user, MediaType.APPLICATION_JSON));
        ResultView resultView = getResult(response, ResultView.class);

        assertNotNull(resultView);
        assertEquals(addCount, resultView.count);
    }

    @Test
    public void testAddUserByGroup_Exception() throws Exception {
        when(mockService.addUserByGroup(eq(invalidId), eq(invalidId), any(User.class))).thenThrow(WaterEquipmentServiceException.class);

        path = String.format("/wemp/organization/%s/organizationGroup/%s/users", invalidId, invalidId);
        Response response = validToken(target(path)).post(Entity.entity(user, MediaType.APPLICATION_JSON));

        assertEquals(FORBIDDEN_403, response.getStatus());
    }

    @Test
    public void testRemoveUserByGroup_InvalidToken() throws Exception {
        path = String.format("/wemp/organization/%s/organizationGroup/%s/user/%s", orgId, groupId, userId);

        Response response = invalidToken(target(path)).delete();

        assertEquals(FORBIDDEN_403, response.getStatus());
    }

    @Test
    public void testRemoveUserByGroup_NotAuthorized() throws Exception {
        path = String.format("/wemp/organization/%s/organizationGroup/%s/user/%s", invalidId, invalidId, invalidId);

        Response response = validToken(target(path)).delete();

        assertEquals(FORBIDDEN_403, response.getStatus());
    }

    @Test
    public void testRemoveUserByGroup() throws Exception {
        when(mockService.removeUserByGroup(orgId, groupId, userId)).thenReturn(EMPTY_RETURN);

        path = String.format("/wemp/organization/%s/organizationGroup/%s/user/%s", orgId, groupId, userId);
        Response response = validToken(target(path)).delete();

        assertEquals(OK_200, response.getStatus());
    }

    @Test
    public void testRemoveUserByGroup_Exception() throws Exception {
        when(mockService.removeUserByGroup(invalidId, invalidId, invalidId)).thenThrow(WaterEquipmentServiceException.class);

        path = String.format("/wemp/organization/%s/organizationGroup/%s/user/%s", invalidId, invalidId, invalidId);
        Response response = validToken(target(path)).delete();

        assertEquals(FORBIDDEN_403, response.getStatus());
    }

    @Test
    public void testGetObjectsByGroup_InvalidToken() throws Exception {
        path = String.format("/wemp/organization/%s/organizationGroup/%s/objects", orgId, groupId);

        Response response = invalidToken(target(path)).get();

        assertEquals(FORBIDDEN_403, response.getStatus());
    }

    @Test
    public void testGetObjectsByGroup_NotAuthorized() throws Exception {
        path = String.format("/wemp/organization/%s/organizationGroup/%s/objects", invalidId, invalidId);

        Response response = validToken(target(path)).get();

        assertEquals(FORBIDDEN_403, response.getStatus());
    }

    @Test
    public void testGetObjectsByGroup() throws Exception {
        when(mockService.getObjectsByGroup(orgId, groupId))
                .thenReturn(objectDataList.stream().map(ObjectView::newInstance).collect(Collectors.toList()));

        path = String.format("/wemp/organization/%s/organizationGroup/%s/objects", orgId, groupId);
        Response response = validToken(target(path)).get();
        Type listType = new TypeToken<List<ObjectView>>(){}.getType();
        List<ObjectView> objectViews = getResult(response, listType);

        assertNotNull(objectViews);
        assertEquals(objectDataList.size(), objectViews.size());
    }

    @Test
    public void testGetObjectsByGroup_Exception() throws Exception {
        when(mockService.getObjectsByGroup(invalidId, invalidId)).thenThrow(WaterEquipmentServiceException.class);

        path = String.format("/wemp/organization/%s/organizationGroup/%s/objects", invalidId, invalidId);
        Response response = validToken(target(path)).get();

        assertEquals(FORBIDDEN_403, response.getStatus());
    }

    @Test
    public void testGetObjectByGroupWithAction() throws Exception {
        Map<String, Boolean> mockMap = Maps.newHashMap();
        mockMap.put("result", true);
        when(mockService.getObjectByGroup(orgId, groupId, objId, action)).thenReturn(mockMap);

        path = String.format("/wemp/organization/%s/organizationGroup/%s/checkPermissions", orgId, groupId);
        Response response = validToken(target(path)
                .queryParam("objId", objId)
                .queryParam("action", action)).get();

        Type resultType = new TypeToken<Map<String, Boolean>>() {}.getType();

        Map<String, Boolean> resultMap = getResult(response, resultType);

        assertNotNull(resultMap);
        assertEquals(mockMap, resultMap);
    }

    @Test
    public void testGetObjectByGroupWithAction_Exception() throws Exception {
        when(mockService.getObjectByGroup(invalidId, invalidId, invalidId, action)).thenThrow(WaterEquipmentServiceException.class);

        path = String.format("/wemp/organization/%s/organizationGroup/%s/checkPermissions", invalidId, invalidId);
        Response response = validToken(target(path)
                .queryParam("objId", invalidId)
                .queryParam("action", action)).get();

        assertEquals(FORBIDDEN_403, response.getStatus());
    }

    @Test
    public void testGetUsersByOrg_InvalidToken() throws Exception {
        path = String.format("/wemp/organization/%s/organizationUsers", orgId);

        Response response = invalidToken(target(path)
                .queryParam("objId", objId)
                .queryParam("action", action)).get();

        assertEquals(FORBIDDEN_403, response.getStatus());
    }

    @Test
    public void testGetUsersByOrg_NotAuthorized() throws Exception {
        path = String.format("/wemp/organization/%s/organizationUsers", invalidId);

        Response response = validToken(target(path)
                .queryParam("objId", objId)
                .queryParam("action", action)).get();

        assertEquals(FORBIDDEN_403, response.getStatus());
    }

    @Test
    public void testGetUsersByOrg() throws Exception {
        when(mockService.getUsersByOrg(orgId))
                .thenReturn(userList.stream().map(UserView::newInstance).collect(Collectors.toList()));

        path = String.format("/wemp/organization/%s/organizationUsers", orgId);
        Response response = validToken(target(path)
                .queryParam("objId", objId)
                .queryParam("action", action)).get();
        Type listType = new TypeToken<List<UserView>>(){}.getType();
        List<UserView> userViews = getResult(response, listType);

        assertNotNull(userViews);
        assertEquals(userList.size(), userViews.size());
    }

    @Test
    public void testGetUsersByOrg_Exception() throws Exception {
        when(mockService.getUsersByOrg(invalidId)).thenThrow(WaterEquipmentServiceException.class);

        path = String.format("/wemp/organization/%s/organizationUsers", invalidId);
        Response response = validToken(target(path)
                .queryParam("objId", objId)
                .queryParam("action", action)).get();

        assertEquals(FORBIDDEN_403, response.getStatus());
    }

    @Test
    public void testAddUserByOrg_InvalidToken() throws Exception {
        path = String.format("/wemp/organization/%s/organizationUsers", orgId);

        Response response = invalidToken(target(path)).post(null);

        assertEquals(FORBIDDEN_403, response.getStatus());
    }

    @Test
    public void testAddUserByOrg_NotAuthorized() throws Exception {
        path = String.format("/wemp/organization/%s/organizationUsers", invalidId);

        Response response = validToken(target(path)).post(null);

        assertEquals(FORBIDDEN_403, response.getStatus());
    }

    @Test
    public void testAddUserByOrg() throws Exception {
        when(mockService.addUserByOrg(eq(orgId), any(User.class))).thenReturn(ResultView.newInstance(addCount));

        path = String.format("/wemp/organization/%s/organizationUsers", orgId);
        Response response = validToken(target(path)).post(Entity.entity(user, MediaType.APPLICATION_JSON));
        ResultView resultView = getResult(response, ResultView.class);

        assertNotNull(resultView);
        assertEquals(addCount, resultView.count);
    }

    @Test
    public void testAddUserByOrg_Exception() throws Exception {
        when(mockService.addUserByOrg(eq(invalidId), any(User.class))).thenThrow(WaterEquipmentServiceException.class);

        path = String.format("/wemp/organization/%s/organizationUsers", invalidId);
        Response response = validToken(target(path)).post(Entity.entity(user, MediaType.APPLICATION_JSON));

        assertEquals(FORBIDDEN_403, response.getStatus());
    }

    @Test
    public void testRemoveUserByOrg_InvalidToken() throws Exception {
        path = String.format("/wemp/organization/%s/organizationUser/%s", orgId, userId);

        Response response = invalidToken(target(path)).delete();

        assertEquals(FORBIDDEN_403, response.getStatus());
    }

    @Test
    public void testRemoveUserByOrg_NotAuthorized() throws Exception {
        path = String.format("/wemp/organization/%s/organizationUser/%s", invalidId, invalidId);

        Response response = validToken(target(path)).delete();

        assertEquals(FORBIDDEN_403, response.getStatus());
    }

    @Test
    public void testRemoveUserByOrg() throws Exception {
        when(mockService.removeUserByOrg(orgId, userId)).thenReturn(EMPTY_RETURN);

        path = String.format("/wemp/organization/%s/organizationUser/%s", orgId, userId);
        Response response = validToken(target(path)).delete();

        assertEquals(OK_200, response.getStatus());
    }

    @Test
    public void testRemoveUserByOrg_Exception() throws Exception {
        when(mockService.removeUserByOrg(invalidId, invalidId)).thenThrow(WaterEquipmentServiceException.class);

        path = String.format("/wemp/organization/%s/organizationUser/%s", invalidId, invalidId);
        Response response = validToken(target(path)).delete();

        assertEquals(FORBIDDEN_403, response.getStatus());
    }

    @Test
    public void testGetObjectsByOrg_InvalidToken() throws Exception {
        path = String.format("/wemp/organization/%s/objects", orgId);

        Response response = invalidToken(target(path)).get();

        assertEquals(FORBIDDEN_403, response.getStatus());
    }

    @Test
    public void testGetObjectsByOrg_NotAuthorized() throws Exception {
        path = String.format("/wemp/organization/%s/objects", invalidId);

        Response response = validToken(target(path)).get();

        assertEquals(FORBIDDEN_403, response.getStatus());
    }

    @Test
    public void testGetObjectsByOrg() throws Exception {
        when(mockService.getObjectsByOrg(orgId))
                .thenReturn(objectDataList.stream().map(ObjectView::newInstance).collect(Collectors.toList()));

        path = String.format("/wemp/organization/%s/objects", orgId);
        Type listType = new TypeToken<List<ObjectView>>(){}.getType();
        Response response = validToken(target(path)).get();
        List<ObjectView> objectViews = getResult(response, listType);

        assertNotNull(objectViews);
        assertEquals(objectDataList.size(), objectViews.size());
    }

    @Test
    public void testGetObjectsByOrg_Exception() throws Exception {
        when(mockService.getObjectsByOrg(invalidId)).thenThrow(WaterEquipmentServiceException.class);

        path = String.format("/wemp/organization/%s/objects", invalidId);
        Response response = validToken(target(path)).get();

        assertEquals(FORBIDDEN_403, response.getStatus());
    }

    @Test
    public void testAddObjectByOrg_InvalidToken() throws Exception {
        path = String.format("/wemp/organization/%s/objects", orgId);

        Response response = invalidToken(target(path)).post(null);

        assertEquals(FORBIDDEN_403, response.getStatus());
    }

    @Test
    public void testAddObjectByOrg_NotAuthorized() throws Exception {
        path = String.format("/wemp/organization/%s/objects", invalidId);

        Response response = validToken(target(path)).post(null);

        assertEquals(FORBIDDEN_403, response.getStatus());
    }

    @Test
    public void testAddObjectByOrg() throws Exception {
        when(mockService.addObjectByOrg(eq(orgId), any(ObjectData.class))).thenReturn(ResultView.newInstance(addCount));

        path = String.format("/wemp/organization/%s/objects", orgId);
        Response response = validToken(target(path)).post(Entity.entity(objectData, MediaType.APPLICATION_JSON));
        ResultView resultView = getResult(response, ResultView.class);

        assertNotNull(resultView);
        assertEquals(addCount, resultView.count);
    }

    @Test
    public void testAddObjectByOrg_Exception() throws Exception {
        when(mockService.addObjectByOrg(eq(invalidId), any(ObjectData.class))).thenThrow(WaterEquipmentServiceException.class);

        path = String.format("/wemp/organization/%s/objects", invalidId);
        Response response = validToken(target(path)).post(Entity.entity(objectData, MediaType.APPLICATION_JSON));

        assertEquals(FORBIDDEN_403, response.getStatus());
    }

    @Test
    public void testRemoveObjectByOrg_InvalidToken() throws Exception {
        path = String.format("/wemp/organization/%s/object/%s", orgId, objId);

        Response response = invalidToken(target(path)).delete();

        assertEquals(FORBIDDEN_403, response.getStatus());
    }

    @Test
    public void testRemoveObjectByOrg_NotAuthorized() throws Exception {
        path = String.format("/wemp/organization/%s/object/%s", invalidId, invalidId);

        Response response = validToken(target(path)).delete();

        assertEquals(FORBIDDEN_403, response.getStatus());
    }

    @Test
    public void testRemoveObjectByOrg() throws Exception {
        when(mockService.removeObjectByOrg(orgId, objId)).thenReturn(EMPTY_RETURN);

        path = String.format("/wemp/organization/%s/object/%s", orgId, objId);
        Response response = validToken(target(path)).delete();

        assertEquals(OK_200, response.getStatus());
    }

    @Test
    public void testRemoveObjectByOrg_Exception() throws Exception {
        when(mockService.removeObjectByOrg(invalidId, invalidId)).thenThrow(WaterEquipmentServiceException.class);

        path = String.format("/wemp/organization/%s/object/%s", invalidId, invalidId);
        Response response = validToken(target(path)).delete();

        assertEquals(FORBIDDEN_403, response.getStatus());
    }
}
