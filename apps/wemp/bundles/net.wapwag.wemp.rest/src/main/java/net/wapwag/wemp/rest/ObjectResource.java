package net.wapwag.wemp.rest;

import com.thingswise.appframework.jaxrs.utils.Authorization;
import com.thingswise.appframework.jaxrs.utils.OAuth2;
import net.wapwag.wemp.WaterEquipmentService;
import net.wapwag.wemp.model.GroupView;
import net.wapwag.wemp.model.ObjectView;
import net.wapwag.wemp.model.ResultView;
import net.wapwag.wemp.model.UserView;
import net.wapwag.wemp.rest.authz.FineGrainedAuthorization;
import net.wapwag.wemp.rest.authz.Permission;
import net.wapwag.wemp.rest.oauth2.WempTokenHandler;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

@Component(service = ObjectResource.class)
@Path("/wemp/object/{oid}")
@OAuth2(tokenHandler = WempTokenHandler.NAME)
public class ObjectResource {

    @Reference
    protected WaterEquipmentService waterEquipmentService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Authorization
    @FineGrainedAuthorization(permission = Permission.READ, target = "{oid}")
    public ObjectView getObject(@PathParam("oid") long objId) throws Exception {
        return waterEquipmentService.getObject(objId);
    }

    @Path("/users")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Authorization
    @FineGrainedAuthorization(permission = Permission.READ, target = "{oid}")
    public List<UserView> getUsersByObject(@PathParam("oid") long objId, @QueryParam("action") String action) throws Exception {
        return waterEquipmentService.getUsersByObject(objId, action);
    }

    @Path("/user/{uid}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Authorization
    @FineGrainedAuthorization(permission = Permission.READ, target = "{oid}")
    public Map<String, String> getUserPermissionByObject(@PathParam("oid") long objId, @PathParam("uid") long userId) throws Exception {
        return waterEquipmentService.getUserPermissionByObject(objId, userId);
    }

    @Path("/user/{uid}")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Authorization
    @FineGrainedAuthorization(permission = Permission.WRITE, target = "{oid}")
    public ResultView addObjectByUser(@PathParam("oid") long objId, @PathParam("uid") long userId, String action) throws Exception {
        return waterEquipmentService.addObjectByUser(objId, userId, action);
    }

    @Path("/user/{uid}")
    @DELETE
    @Authorization
    @FineGrainedAuthorization(permission = Permission.WRITE, target = "{oid}")
    public void removeObjectByUser(@PathParam("oid") long objId, @PathParam("uid") long userId, @QueryParam("action") String action) throws Exception {
        waterEquipmentService.removeObjectByUser(objId, userId, action);
    }

    @Path("/groups")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Authorization
    @FineGrainedAuthorization(permission = Permission.READ, target = "{oid}")
    public List<GroupView> getGroupsByObject(@PathParam("oid") long objId, @QueryParam("action") String action) throws Exception {
        return waterEquipmentService.getGroupsByObject(objId, action);
    }

    @Path("/group/{gid}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Authorization
    @FineGrainedAuthorization(permission = Permission.READ, target = "{oid}")
    public ObjectView getObjectByGroup(@PathParam("oid") long objId, @PathParam("gid") long gid) throws Exception {
        return waterEquipmentService.getObjectByGroup(objId, gid);
    }

    @Path("/group/{gid}")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Authorization
    @FineGrainedAuthorization(permission = Permission.WRITE, target = "{oid}")
    public ResultView addObjectByGroup(@PathParam("oid") long objId, @PathParam("gid") long gid) throws Exception {
        return waterEquipmentService.addObjectByGroup(objId, gid);
    }

    @Path("/group/{gid}")
    @DELETE
    @Authorization
    @FineGrainedAuthorization(permission = Permission.WRITE, target = "{oid}")
    public void removeObjectByGroup(@PathParam("oid") long objId, @PathParam("gid") long gid, @QueryParam("action") String action) throws Exception {
        waterEquipmentService.removeObjectByGroup(objId, gid, action);
    }

}
