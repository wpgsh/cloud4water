package net.wapwag.wemp.rest;

import com.google.gson.reflect.TypeToken;
import net.wapwag.wemp.WaterEquipmentServiceException;
import net.wapwag.wemp.dao.WaterEquipmentDaoException;
import net.wapwag.wemp.dao.model.ObjectData;
import net.wapwag.wemp.model.GroupView;
import net.wapwag.wemp.model.ObjectView;
import net.wapwag.wemp.model.ResultView;
import net.wapwag.wemp.model.UserView;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static net.wapwag.wemp.rest.MockData.*;
import static net.wapwag.wemp.rest.MockData.objId;
import static org.eclipse.jetty.http.HttpStatus.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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
        path = String.format("/user/%s/checkPermissions", userId);

        Response response = target(path).request().post(Entity.form(new Form().param("objId", objId + "")));

        ResultView resultView = getResult(response, ResultView.class);

        assertNotNull(resultView);
        assertEquals(1, resultView.count);
    }

//    @Test
//    public void testGetObjectsByUser() throws Exception {
//        when(waterEquipmentDao.getObjectsByUser(userId, "read")).thenReturn(new HashSet<>(objectDataList));
//
//        Set<ObjectView> objectViews = waterEquipmentService.getObjectsByUser(userId, "read");
//
//        assertNotNull(objectViews);
//        assertEquals(objectDataList.size(), objectViews.size());
//    }
//
//    @Test
//    public void testGetObjectsByUser_Exception() throws Exception {
//        when(waterEquipmentDao.getObjectsByUser(userId, "read")).thenThrow(WaterEquipmentDaoException.class);
//
//        Set<ObjectView> objectViews = waterEquipmentService.getObjectsByUser(userId, "read");
//
//        assertNull(objectViews);
//    }
}
