package net.wapwag.wemp.rest;

import com.google.gson.reflect.TypeToken;
import net.wapwag.wemp.WaterEquipmentServiceException;
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

/**
 * Object resource test
 * Created by Administrator on 2016/11/7 0007.
 */
public class ObjectResourceTest extends BaseResourceTest {

    private String path;

    @Override
    ResourceConfig initResource() {
        return new ResourceConfig(ObjectResourceMock.class);
    }

    @Test
    public void testGetObject() throws WaterEquipmentServiceException {
        path = String.format("/object/%s", objId);

        Response response = target(path).request().get();

        ObjectView objectView = getResult(response, ObjectView.class);

        assertEquals(OK_200, response.getStatus());
        assertNotNull(objectView);
        assertEquals(objectData.getId(), objectView.id);
        assertEquals(objectData.getName(), objectView.name);
        assertEquals(objectData.getType(), objectView.objectType);
    }

    @Test
    public void testGetObject_InvalidObjectId_NonNumber() throws Exception {
        path = String.format("/object/%s", "invalid_objId");

        Response response = target(path).request().get();

        assertEquals(NOT_FOUND_404, response.getStatus());
    }

    @Test
    public void testGetObject_InvalidObjectIdValue() throws Exception {
        path = String.format("/object/%s", "99999");

        Response response = target(path).request().get();

        assertEquals(NO_CONTENT_204, response.getStatus());
    }

    @Test
    public void testGetUsersByObject() throws Exception {
        path = String.format("/object/%s/users", objId);

        Response response = target(path).queryParam("action", action).request().get();

        Type listType = new TypeToken<List<UserView>>(){}.getType();
        List<UserView> userViewList = getResult(response, listType);

        assertNotNull(userViewList);
        assertEquals(userList.size(), userViewList.size());
    }

    @Test
    public void testGetObjectByUser() throws Exception {
        path = String.format("/object/%s/user/%s", objId, userId);

        Response response = target(path).request().get();

        ObjectView objectView = getResult(response, ObjectView.class);

        assertNotNull(objectView);
        assertEquals(objectData.getId(), objId);
        assertEquals(objectData.getName(), objectView.name);
    }

    @Test
    public void testAddObjectByUser() throws Exception {
        path = String.format("/object/%s/user/%s", objId, userId);

        Response response = target(path).request().post(null);

        ResultView resultView = getResult(response, ResultView.class);

        assertNotNull(resultView);
        assertEquals(addCount, resultView.count);
    }

    @Test
    public void testRemoveObjectByUser() throws Exception {
        path = String.format("/object/%s/user/%s", objId, userId);

        Response response = target(path).queryParam("action", action).request().delete();

        ResultView resultView = getResult(response, ResultView.class);

        assertNotNull(resultView);
        assertEquals(removeCount, resultView.count);
    }

    @Test
    public void testGetGroupsByObject() throws Exception {
        path = String.format("/object/%s/groups", objId);

        Response response = target(path).queryParam("action", action).request().get();

        Type listType = new TypeToken<List<GroupView>>(){}.getType();
        List<GroupView> groupViews = getResult(response, listType);

        assertNotNull(groupViews);
        assertEquals(count, groupViews.size());

    }

    @Test
    public void testGetObjectByGroup() throws Exception {
        path = String.format("/object/%s/group/%s", objId, groupId);

        Response response = target(path).request().get();

        ObjectView objectView = getResult(response, ObjectView.class);

        assertEquals(OK_200, response.getStatus());
        assertNotNull(objectView);
        assertEquals(objectData.getId(), objectView.id);
        assertEquals(objectData.getName(), objectView.name);
        assertEquals(objectData.getType(), objectView.objectType);
    }

    @Test
    public void testAddObjectByGroup() throws Exception {
        path = String.format("/object/%s/group/%s", objId, groupId);

        Response response = target(path).request().post(null);

        ResultView resultView = getResult(response, ResultView.class);

        assertNotNull(resultView);
        assertEquals(addCount, resultView.count);

    }

    @Test
    public void testRemoveObjectByGroup() throws Exception {
        path = String.format("/object/%s/group/%s", objId, groupId);

        Response response = target(path).queryParam("action", action).request().delete();

        ResultView resultView = getResult(response, ResultView.class);

        assertNotNull(resultView);
        assertEquals(removeCount, resultView.count);
    }

}