package net.wapwag.wemp.rest;

import com.google.gson.reflect.TypeToken;
import net.wapwag.wemp.WaterEquipmentServiceException;
import net.wapwag.wemp.dao.model.ObjectData;
import net.wapwag.wemp.model.ObjectView;
import net.wapwag.wemp.model.ResultView;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.lang.reflect.Type;
import java.util.Set;
import java.util.stream.Collectors;

import static net.wapwag.wemp.rest.MockData.*;
import static net.wapwag.wemp.rest.UserResourceMock.mockService;
import static org.eclipse.jetty.http.HttpStatus.INTERNAL_SERVER_ERROR_500;
import static org.eclipse.jetty.http.HttpStatus.OK_200;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

/**
 * Object resource test
 * Created by Administrator on 2016/11/7 0007.
 */
public class UserResourceTest extends BaseResourceTest {

    private String path;

    @Override
    ResourceConfig initResource() {
        return new ResourceConfig(UserResourceMock.class);
    }

    @Test
    public void testCheckPermission_True() throws Exception {
        when(mockService.checkPermission(anyLong(), any(ObjectData.class)))
                .thenReturn(ResultView.newInstance(true));

        path = String.format("/user/%s/checkPermissions", userId);

        Entity<ObjectData> entity = Entity.entity(objectData, MediaType.APPLICATION_JSON);

        Response response = target(path).request().post(entity);

        ResultView resultView = getResult(response, ResultView.class);

        assertEquals(OK_200, response.getStatus());
        assertNotNull(resultView);
        assertEquals(1, resultView.count);
    }

    @Test
    public void testCheckPermission_False() throws Exception {
        when(mockService.checkPermission(anyLong(), any(ObjectData.class)))
                .thenReturn(ResultView.newInstance(false));

        path = String.format("/user/%s/checkPermissions", userId);

        Entity<ObjectData> entity = Entity.entity(objectData, MediaType.APPLICATION_JSON);

        Response response = target(path).request().post(entity);

        ResultView resultView = getResult(response, ResultView.class);

        assertEquals(OK_200, response.getStatus());
        assertNotNull(resultView);
        assertEquals(0, resultView.count);
    }

    @Test
    public void testCheckPermission_Exception() throws Exception {
        when(mockService.checkPermission(anyLong(), any(ObjectData.class))).thenThrow(WaterEquipmentServiceException.class);

        path = String.format("/user/%s/checkPermissions", userId);

        Entity<ObjectData> entity = Entity.entity(objectData, MediaType.APPLICATION_JSON);

        Response response = target(path).request().post(entity);

        assertEquals(INTERNAL_SERVER_ERROR_500, response.getStatus());
    }

    @Test
    public void testGetObjectsByUser() throws Exception {
        when(mockService.getObjectsByUser(userId, "read"))
                .thenReturn(objectDataList.stream().map(ObjectView::newInstance).collect(Collectors.toSet()));

        path = String.format("/user/%s/objects", userId);

        Response response = target(path).queryParam("action", action).request().get();
        Type type = new TypeToken<Set<ObjectView>>(){}.getType();
        Set<ObjectView> objectViews = getResult(response, type);

        assertEquals(OK_200, response.getStatus());
        assertNotNull(objectViews);
        assertEquals(objectDataList.size(), objectViews.size());
    }

    @Test
    public void testGetObjectsByUser_Exception() throws Exception {
        when(mockService.getObjectsByUser(userId, "read")).thenThrow(WaterEquipmentServiceException.class);

        path = String.format("/user/%s/objects", userId);

        Response response = target(path).queryParam("action", action).request().get();

        assertEquals(INTERNAL_SERVER_ERROR_500, response.getStatus());
    }
}
