package net.wapwag.wemp.rest;

import com.google.common.collect.Lists;
import com.google.gson.reflect.TypeToken;
import net.wapwag.wemp.WaterEquipmentServiceException;
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

import static net.wapwag.wemp.rest.MockData.*;
import static org.eclipse.jetty.http.HttpStatus.FORBIDDEN_403;
import static org.eclipse.jetty.http.HttpStatus.OK_200;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Object resource test
 * Created by Administrator on 2016/11/7 0007.
 */
public class ObjectResourceTest extends BaseResourceTest {

    private String path;

    @Test
    public void testGetObject_InvalidToken() throws WaterEquipmentServiceException {
        path = String.format("/wemp/object/%s", objId);

        Response response = invalidToken(target(path)).get();

        assertEquals(FORBIDDEN_403, response.getStatus());
    }    
    
    @Test
    public void testGetObject_NotAuthorized() throws WaterEquipmentServiceException {
        path = String.format("/wemp/object/%s", invalidId);

        Response response = validToken(target(path)).get();

        assertEquals(FORBIDDEN_403, response.getStatus());
    }
    
    @Test
    public void testGetObject() throws WaterEquipmentServiceException {
        path = String.format("/wemp/object/%s", objId);

        Response response = validToken(target(path)).get();

        ObjectView objectView = getResult(response, ObjectView.class);

        assertEquals(OK_200, response.getStatus());
        assertNotNull(objectView);
        assertEquals(objectData.getId(), objectView.id);
        assertEquals(objectData.getName(), objectView.name);
        assertEquals(objectData.getType(), objectView.objectType);
    }

    @Test
    public void testGetObject_InvalidObjectId_NonNumber() throws Exception {
        path = String.format("/wemp/object/%s", "invalid_objId");

        Response response = validToken(target(path)).get();

        assertEquals(FORBIDDEN_403, response.getStatus());
    }

    @Test
    public void testGetObject_InvalidObjectIdValue() throws Exception {
        path = String.format("/wemp/object/%s", "99999");

        Response response = validToken(target(path)).get();

        assertEquals(FORBIDDEN_403, response.getStatus());
    }

    @Test
    public void testGetUsersByObject_InvalidToken() throws Exception {
        path = String.format("/wemp/object/%s/users", objId);

        Response response = invalidToken(target(path).queryParam("action", action)).get();

        assertEquals(FORBIDDEN_403, response.getStatus());
    }

    @Test
    public void testGetUsersByObject_NotAuthorized() throws Exception {
        path = String.format("/wemp/object/%s/users", invalidId);

        Response response = validToken(target(path).queryParam("action", action)).get();

        assertEquals(FORBIDDEN_403, response.getStatus());
    }

    @Test
    public void testGetUsersByObject() throws Exception {
        path = String.format("/wemp/object/%s/users", objId);

        Response response = validToken(target(path).queryParam("action", action)).get();

        Type listType = new TypeToken<List<UserView>>(){}.getType();
        List<UserView> userViewList = getResult(response, listType);

        assertNotNull(userViewList);
        assertEquals(userList.size(), userViewList.size());
    }

    @Test
    public void testGetObjectByUser_InvalidToken() throws Exception {
        path = String.format("/wemp/object/%s/user/%s", objId, userId);

        Response response = invalidToken(target(path)).get();

        assertEquals(FORBIDDEN_403, response.getStatus());
    }

    @Test
    public void testGetObjectByUser_NotAuthorized() throws Exception {
        path = String.format("/wemp/object/%s/user/%s", invalidId, invalidId);

        Response response = validToken(target(path)).get();

        assertEquals(FORBIDDEN_403, response.getStatus());
    }

    @Test
    public void testGetObjectByUser() throws Exception {
        List<String> mockList = Lists.newArrayList();
        mockList.add(action);
        path = String.format("/wemp/object/%s/user/%s", objId, userId);

        Response response = validToken(target(path)).get();
        Type type = new TypeToken<List<String>>(){}.getType();

        List<String> resultList = getResult(response, type);

        assertNotNull(resultList);
        assertEquals(mockList, resultList);
    }

    @Test
    public void testAddObjectByUser_InvalidToken() throws Exception {
        path = String.format("/wemp/object/%s/user/%s", objId, userId);

        Response response = invalidToken(target(path)).post(null);

        assertEquals(FORBIDDEN_403, response.getStatus());
    }

    @Test
    public void testAddObjectByUser_NotAuthorized() throws Exception {
        path = String.format("/wemp/object/%s/user/%s", invalidId, invalidId);

        Response response = validToken(target(path)).post(null);

        assertEquals(FORBIDDEN_403, response.getStatus());
    }

    @Test
    public void testAddObjectByUser() throws Exception {
        path = String.format("/wemp/object/%s/user/%s", objId, userId);

        Response response = validToken(target(path)).post(Entity.entity(action, MediaType.APPLICATION_JSON));

        ResultView resultView = getResult(response, ResultView.class);

        assertEquals(OK_200, response.getStatus());
        assertNotNull(resultView);
        assertEquals(addCount, resultView.count);
    }

    @Test
    public void testRemoveObjectByUser_InvalidToken() throws Exception {
        path = String.format("/wemp/object/%s/user/%s", objId, userId);

        Response response = invalidToken(target(path).queryParam("action", action)).delete();

        assertEquals(FORBIDDEN_403, response.getStatus());
    }

    @Test
    public void testRemoveObjectByUser_NotAuthorized() throws Exception {
        path = String.format("/wemp/object/%s/user/%s", invalidId, invalidId);

        Response response = validToken(target(path).queryParam("action", action)).delete();

        assertEquals(FORBIDDEN_403, response.getStatus());
    }

    @Test
    public void testRemoveObjectByUser() throws Exception {
        path = String.format("/wemp/object/%s/user/%s", objId, userId);

        Response response = validToken(target(path).queryParam("action", action)).delete();

        assertEquals(OK_200, response.getStatus());
    }

    @Test
    public void testGetGroupsByObject_InvalidToken() throws Exception {
        path = String.format("/wemp/object/%s/groups", objId);

        Response response = invalidToken(target(path).queryParam("action", action)).get();

        assertEquals(FORBIDDEN_403, response.getStatus());

    }

    @Test
    public void testGetGroupsByObject_NotAuthorized() throws Exception {
        path = String.format("/wemp/object/%s/groups", invalidId);

        Response response = validToken(target(path).queryParam("action", action)).get();

        assertEquals(FORBIDDEN_403, response.getStatus());

    }

    @Test
    public void testGetGroupsByObject() throws Exception {
        path = String.format("/wemp/object/%s/groups", objId);

        Response response = validToken(target(path).queryParam("action", action)).get();

        Type listType = new TypeToken<List<GroupView>>(){}.getType();
        List<GroupView> groupViews = getResult(response, listType);

        assertNotNull(groupViews);
        assertEquals(count, groupViews.size());

    }

    @Test
    public void testGetObjectByGroup_InvalidToken() throws Exception {
        path = String.format("/wemp/object/%s/group/%s", objId, groupId);

        Response response = invalidToken(target(path)).get();

        assertEquals(FORBIDDEN_403, response.getStatus());
    }

    @Test
    public void testGetObjectByGroup_NotAuthorized() throws Exception {
        path = String.format("/wemp/object/%s/group/%s", invalidId, invalidId);

        Response response = validToken(target(path)).get();

        assertEquals(FORBIDDEN_403, response.getStatus());
    }

    @Test
    public void testGetObjectByGroup() throws Exception {
        path = String.format("/wemp/object/%s/group/%s", objId, groupId);

        Response response = validToken(target(path)).get();

        ObjectView objectView = getResult(response, ObjectView.class);

        assertEquals(OK_200, response.getStatus());
        assertNotNull(objectView);
        assertEquals(objectData.getId(), objectView.id);
        assertEquals(objectData.getName(), objectView.name);
        assertEquals(objectData.getType(), objectView.objectType);
    }

    @Test
    public void testAddObjectByGroup_InvalidToken() throws Exception {
        path = String.format("/wemp/object/%s/group/%s", objId, groupId);

        Response response = invalidToken(target(path)).post(null);

        assertEquals(FORBIDDEN_403, response.getStatus());

    }

    @Test
    public void testAddObjectByGroup_NotAuthorized() throws Exception {
        path = String.format("/wemp/object/%s/group/%s", invalidId, invalidId);

        Response response = validToken(target(path)).post(null);

        assertEquals(FORBIDDEN_403, response.getStatus());

    }

    @Test
    public void testAddObjectByGroup() throws Exception {
        path = String.format("/wemp/object/%s/group/%s", objId, groupId);

        Response response = validToken(target(path)).post(null);

        ResultView resultView = getResult(response, ResultView.class);

        assertEquals(OK_200, response.getStatus());
        assertNotNull(resultView);
        assertEquals(addCount, resultView.count);

    }

    @Test
    public void testRemoveObjectByGroup_InvalidToken() throws Exception {
        path = String.format("/wemp/object/%s/group/%s", objId, groupId);

        Response response = invalidToken(target(path).queryParam("action", action)).delete();

        assertEquals(FORBIDDEN_403, response.getStatus());
    }

    @Test
    public void testRemoveObjectByGroup_NotAuthorized() throws Exception {
        path = String.format("/wemp/object/%s/group/%s", invalidId, invalidId);

        Response response = validToken(target(path).queryParam("action", action)).delete();

        assertEquals(FORBIDDEN_403, response.getStatus());
    }

    @Test
    public void testRemoveObjectByGroup() throws Exception {
        path = String.format("/wemp/object/%s/group/%s", objId, groupId);

        Response response = validToken(target(path).queryParam("action", action)).delete();

        assertEquals(OK_200, response.getStatus());
    }

}
