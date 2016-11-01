package net.wapwag.wemp.rest;

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
import java.util.Set;

@Component(service = UserResource.class)
@Path("/user/{uid}")
@OAuth2(tokenHandler = WempTokenHandler.NAME)
public class UserResource {

    @Reference
    private WaterEquipmentService waterEquipmentService;

    @Path("/checkPermissions")
    @POST
    @FineGrainedAuthorization(permission = Permission.READ, target = "{uid}")
    public ResultView checkPermission(@PathParam("uid") long userId, ObjectData objectData) throws Exception {
        return waterEquipmentService.checkPermission(userId, objectData);
    }

    @Path("/objects")
    @GET
    @FineGrainedAuthorization(permission = Permission.READ, target = "{oid}")
    public Set<ObjectView> getObjectsByUser(@PathParam("uid") long userId, @QueryParam("action") String action) throws Exception {
        return waterEquipmentService.getObjectsByUser(userId, action);
    }

}
