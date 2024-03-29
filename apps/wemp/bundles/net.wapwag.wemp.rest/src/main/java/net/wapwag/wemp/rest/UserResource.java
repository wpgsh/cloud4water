package net.wapwag.wemp.rest;

import com.thingswise.appframework.jaxrs.utils.Authorization;
import com.thingswise.appframework.jaxrs.utils.OAuth2;
import net.wapwag.wemp.WaterEquipmentService;
import net.wapwag.wemp.dao.model.ObjectData;
import net.wapwag.wemp.model.ObjectView;
import net.wapwag.wemp.model.ResultView;
import net.wapwag.wemp.rest.authz.FineGrainedAuthorization;
import net.wapwag.wemp.rest.authz.Permission;
import net.wapwag.wemp.rest.oauth2.WempTokenHandler;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Map;
import java.util.Set;

@Component(service = UserResource.class)
@Path("/wemp/user/{uid}")
@OAuth2(tokenHandler = WempTokenHandler.NAME)
public class UserResource {

    @Reference
    protected WaterEquipmentService waterEquipmentService;

    @Path("/checkPermissions")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Authorization
    @FineGrainedAuthorization(permission = Permission.READ, target = "{uid}")
    public Map<String, Boolean> checkPermission(@PathParam("uid") long userId, ObjectData objectData) throws Exception {
        return waterEquipmentService.checkPermission(userId, objectData);
    }

    @Path("/objects")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Authorization
    @FineGrainedAuthorization(permission = Permission.READ, target = "{uid}")
    public Set<Long> getObjectsByUser(@PathParam("uid") long userId, @QueryParam("action") String action) throws Exception {
        return waterEquipmentService.getObjectsByUser(userId, action);
    }

}
