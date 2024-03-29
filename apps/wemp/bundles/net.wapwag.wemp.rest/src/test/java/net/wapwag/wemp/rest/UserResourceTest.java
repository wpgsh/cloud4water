package net.wapwag.wemp.rest;

import com.google.common.collect.Maps;
import com.google.gson.reflect.TypeToken;
import net.wapwag.wemp.WaterEquipmentServiceException;
import net.wapwag.wemp.dao.model.ObjectData;
import net.wapwag.wemp.model.ResultView;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static net.wapwag.wemp.rest.MockData.*;
import static net.wapwag.wemp.rest.UserResourceMock.mockService;
import static org.eclipse.jetty.http.HttpStatus.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

/**
 * Object resource test
 * Created by Administrator on 2016/11/7 0007.
 */
public class UserResourceTest extends BaseResourceTest {

    private String path;

    @Test
    public void testCheckPermission_InvalidToken() throws Exception {
        path = String.format("/wemp/user/%s/checkPermissions", userId);

        Response response = invalidToken(target(path)).post(null);

        assertEquals(FORBIDDEN_403, response.getStatus());
    }

    @Test
    public void testCheckPermission_NotAuthorized() throws Exception {
        path = String.format("/wemp/user/%s/checkPermissions", invalidId);

        Response response = validToken(target(path)).post(null);

        assertEquals(FORBIDDEN_403, response.getStatus());
    }

    @Test
    public void testCheckPermission_True() throws Exception {
        Map<String, Boolean> mockMap = Maps.newHashMap();
        mockMap.put("result", true);
        when(mockService.checkPermission(eq(userId), any(ObjectData.class))).thenReturn(mockMap);

        path = String.format("/wemp/user/%s/checkPermissions", userId);

        Entity<ObjectData> entity = Entity.entity(objectData, MediaType.APPLICATION_JSON);

        Response response = validToken(target(path)).post(entity);

        Type resultType = new TypeToken<Map<String, Boolean>>() {}.getType();
        Map<String, Boolean> resultMap = getResult(response, resultType);

        assertEquals(OK_200, response.getStatus());
        assertEquals(mockMap, resultMap);
    }

    @Test
    public void testCheckPermission_False() throws Exception {
        Map<String, Boolean> mockMap = Maps.newHashMap();
        mockMap.put("result", false);
        when(mockService.checkPermission(eq(userId), any(ObjectData.class))).thenReturn(mockMap);

        path = String.format("/wemp/user/%s/checkPermissions", userId);

        Entity<ObjectData> entity = Entity.entity(objectData, MediaType.APPLICATION_JSON);

        Response response = validToken(target(path)).post(entity);

        ResultView resultView = getResult(response, ResultView.class);

        assertEquals(OK_200, response.getStatus());
        assertNotNull(resultView);
        assertEquals(0, resultView.count);
    }

    @Test
    public void testCheckPermission_Exception() throws Exception {
        when(mockService.checkPermission(eq(exceptionId), any(ObjectData.class))).thenThrow(WaterEquipmentServiceException.class);

        path = String.format("/wemp/user/%s/checkPermissions", exceptionId);

        Entity<ObjectData> entity = Entity.entity(objectData, MediaType.APPLICATION_JSON);

        Response response = validToken(target(path)).post(entity);

        assertEquals(INTERNAL_SERVER_ERROR_500, response.getStatus());
    }

    @Test
    public void testGetObjectsByUser_InvalidToken() throws Exception {
        path = String.format("/wemp/user/%s/objects", userId);

        Response response = invalidToken(target(path).queryParam("action", action)).get();

        assertEquals(FORBIDDEN_403, response.getStatus());
    }

    @Test
    public void testGetObjectsByUser_NotAuthorized() throws Exception {
        path = String.format("/wemp/user/%s/objects", invalidId);

        Response response = validToken(target(path).queryParam("action", action)).get();

        assertEquals(FORBIDDEN_403, response.getStatus());
    }

    @Test
    public void testGetObjectsByUser() throws Exception {
        Set<Long> mockSet = new HashSet<>();
        Collections.addAll(mockSet, 1L, 2L, 3L, 4L, 5L);
        when(mockService.getObjectsByUser(userId, "read")).thenReturn(mockSet);

        path = String.format("/wemp/user/%s/objects", userId);

        Response response = validToken(target(path).queryParam("action", action)).get();
        Type type = new TypeToken<Set<Long>>(){}.getType();
        Set<Long> objectViews = getResult(response, type);

        assertEquals(OK_200, response.getStatus());
        assertEquals(mockSet, objectViews);
    }

    @Test
    public void testGetObjectsByUser_Exception() throws Exception {
        when(mockService.getObjectsByUser(exceptionId, action)).thenThrow(WaterEquipmentServiceException.class);

        path = String.format("/wemp/user/%s/objects", exceptionId);

        Response response = validToken(target(path).queryParam("action", action)).get();

        assertEquals(INTERNAL_SERVER_ERROR_500, response.getStatus());
    }
}
