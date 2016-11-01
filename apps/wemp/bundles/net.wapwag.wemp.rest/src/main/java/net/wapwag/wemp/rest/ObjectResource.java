package net.wapwag.wemp.rest;

import net.wapwag.wemp.WaterEquipmentService;
import net.wapwag.wemp.dao.model.ObjectData;
import net.wapwag.wemp.model.GroupView;
import net.wapwag.wemp.model.ObjectView;
import net.wapwag.wemp.model.ResultView;
import net.wapwag.wemp.model.UserView;
import net.wapwag.wemp.rest.authz.FineGrainedAuthorization;
import net.wapwag.wemp.rest.authz.Permission;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.ws.rs.*;
import java.util.List;

@Component(service = ObjectResource.class)
@Path("/object/{oid}")
//@OAuth2(tokenHandler = WempTokenHandler.NAME)
public class ObjectResource {

    @Reference
    private WaterEquipmentService waterEquipmentService;

    @GET
    @FineGrainedAuthorization(permission = Permission.READ, target = "{oid}")
    public ObjectView getObject(@PathParam("oid") long objId) throws Exception {
        return waterEquipmentService.getObject(objId);
    }

    @Path("/users")
    @GET
    @FineGrainedAuthorization(permission = Permission.READ, target = "{oid}")
    public List<UserView> getUsersByObject(@PathParam("oid") long objId, @QueryParam("action") String action) throws Exception {
        return waterEquipmentService.getUsersByObject(objId, action);
    }

    @Path("/user/{uid}")
    @GET
    @FineGrainedAuthorization(permission = Permission.READ, target = "{oid}")
    public ObjectView getObjectByUser(@PathParam("oid") long objId, @PathParam("uid") long userId) throws Exception {
        return waterEquipmentService.getObjectByUser(objId, userId);
    }

    @Path("/user/{uid}")
    @POST
    @FineGrainedAuthorization(permission = Permission.WRITE, target = "{oid}")
    public ResultView addObjectByUser(@PathParam("oid") long objId, @PathParam("uid") long userId) throws Exception {
        return waterEquipmentService.addObjectByUser(objId, userId);
    }

    @Path("/user/{uid}")
    @DELETE
    @FineGrainedAuthorization(permission = Permission.WRITE, target = "{oid}")
    public ResultView removeObjectByUser(@PathParam("oid") long objId, @PathParam("uid") long userId, @QueryParam("action") String actionId) throws Exception {
        return waterEquipmentService.removeObjectByUser(objId, userId, actionId);
    }

    @Path("/groups")
    @GET
    @FineGrainedAuthorization(permission = Permission.READ, target = "{oid}")
    public List<GroupView> getGroupsByObject(@PathParam("oid") long objId, @QueryParam("actionId") String actionId) throws Exception {
        return waterEquipmentService.getGroupsByObject(objId, actionId);
    }

    @Path("/group/{gid}")
    @GET
    @FineGrainedAuthorization(permission = Permission.READ, target = "{oid}")
    public ObjectView getObjectByGroup(@PathParam("oid") long objId, @PathParam("gid") long gid) throws Exception {
        return waterEquipmentService.getObjectByGroup(objId, gid);
    }

    @Path("/group/{gid}")
    @POST
    @FineGrainedAuthorization(permission = Permission.WRITE, target = "{oid}")
    public ResultView addObjectByGroup(@PathParam("oid") long objId, @PathParam("gid") long gid) throws Exception {
        return waterEquipmentService.addObjectByGroup(objId, gid);
    }

    @Path("/group/{gid}")
    @DELETE
    @FineGrainedAuthorization(permission = Permission.WRITE, target = "{oid}")
    public ResultView removeObjectByGroup(@PathParam("oid") long objId, @PathParam("gid") long gid, @QueryParam("action") String action) throws Exception {
        return waterEquipmentService.removeObjectByGroup(objId, gid, action);
    }

}
