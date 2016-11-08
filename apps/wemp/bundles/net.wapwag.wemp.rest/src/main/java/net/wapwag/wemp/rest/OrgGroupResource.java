package net.wapwag.wemp.rest;

import com.thingswise.appframework.jaxrs.utils.Authorization;
import net.wapwag.wemp.WaterEquipmentService;
import net.wapwag.wemp.dao.model.ObjectData;
import net.wapwag.wemp.dao.model.permission.Group;
import net.wapwag.wemp.dao.model.permission.User;
import net.wapwag.wemp.model.GroupView;
import net.wapwag.wemp.model.ObjectView;
import net.wapwag.wemp.model.ResultView;
import net.wapwag.wemp.model.UserView;
import net.wapwag.wemp.rest.authz.FineGrainedAuthorization;
import net.wapwag.wemp.rest.authz.Permission;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Component(service = OrgGroupResource.class)
@Path("/organization/{oid}")
//@OAuth2(tokenHandler = WempTokenHandler.NAME)
public class OrgGroupResource {

    @Reference
    protected WaterEquipmentService waterEquipmentService;

    @Path("/organizationGroups")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Authorization
    @FineGrainedAuthorization(permission = Permission.READ, target = "{oid}")
    public List<GroupView> getGroupsByOrg(@PathParam("oid") long orgId) throws Exception {
        return waterEquipmentService.getGroupsByOrg(orgId);
    }

    @Path("/organizationGroups")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Authorization
    @FineGrainedAuthorization(permission = Permission.WRITE, target = "{oid}")
    public ResultView addGroupByOrg(@PathParam("oid") long orgId, Group group) throws Exception {
        return waterEquipmentService.addGroupByOrg(orgId, group);
    }

    @Path("/organizationGroup/{gid}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Authorization
    @FineGrainedAuthorization(permission = Permission.READ, target = "{oid}")
    public GroupView getGroupByOrg(@PathParam("oid") long orgId, @PathParam("gid") long groupId) throws Exception {
        return waterEquipmentService.getGroupByOrg(orgId, groupId);
    }

    @Path("/organizationGroup/{gid}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Authorization
    @FineGrainedAuthorization(permission = Permission.WRITE, target = "{oid}")
    public ResultView updateGroupByOrg(@PathParam("oid") long orgId, @PathParam("gid") long groupId, Group group) throws Exception {
        return waterEquipmentService.updateGroupByOrg(orgId, groupId, group);
    }

    @Path("/organizationGroup/{gid}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Authorization
    @FineGrainedAuthorization(permission = Permission.WRITE, target = "{oid}")
    public ResultView removeGroupByOrg(@PathParam("oid") long orgId, @PathParam("gid") long groupId) throws Exception {
        return waterEquipmentService.removeGroupByOrg(orgId, groupId);
    }

    @Path("/organizationGroup/{gid}/users")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Authorization
    @FineGrainedAuthorization(permission = Permission.READ, target = "{oid}")
    public List<UserView> getUsersByGroup(@PathParam("oid") long orgId, @PathParam("gid") long groupId) throws Exception {
        return waterEquipmentService.getUsersByGroup(orgId, groupId);
    }

    @Path("/organizationGroup/{gid}/user/{uid}")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Authorization
    @FineGrainedAuthorization(permission = Permission.WRITE, target = "{oid}")
    public ResultView addUserByGroup(@PathParam("oid") long orgId, @PathParam("gid") long groupId, @PathParam("uid") long userId) throws Exception {
        return waterEquipmentService.addUserByGroup(orgId, groupId, userId);
    }

    @Path("/organizationGroup/{gid}/user/{uid}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Authorization
    @FineGrainedAuthorization(permission = Permission.WRITE, target = "{oid}")
    public ResultView removeUserByGroup(@PathParam("oid") long orgId, @PathParam("gid") long groupId, @PathParam("uid") long userId) throws Exception {
        return waterEquipmentService.removeUserByGroup(orgId, groupId, userId);
    }

    @Path("/organizationGroup/{gid}/objects")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Authorization
    @FineGrainedAuthorization(permission = Permission.READ, target = "{oid}")
    public List<ObjectView> getObjectsByGroup(@PathParam("oid") long orgId, @PathParam("gid") long groupId) throws Exception {
        return waterEquipmentService.getObjectsByGroup(orgId, groupId);
    }

    @Path("/organizationGroup/{gid}/checkPermissions")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Authorization
    @FineGrainedAuthorization(permission = Permission.READ, target = "{oid}")
    public ObjectView getObjectByGroup(@PathParam("oid") long orgId, @PathParam("gid") long groupId, @QueryParam("objId") long objId, @QueryParam("action") String action) throws Exception {
        return waterEquipmentService.getObjectByGroup(orgId, groupId, objId, action);
    }

    @Path("/organizationUsers")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Authorization
    @FineGrainedAuthorization(permission = Permission.READ, target = "{oid}")
    public List<UserView> getUsersByOrg(@PathParam("oid") long orgId) throws Exception {
        return waterEquipmentService.getUsersByOrg(orgId);
    }

    @Path("/organizationUsers")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Authorization
    @FineGrainedAuthorization(permission = Permission.WRITE, target = "{oid}")
    public ResultView addUserByOrg(@PathParam("oid") long orgId, User user) throws Exception {
        return waterEquipmentService.addUserByOrg(orgId, user);
    }

    @Path("/organizationUser/{uid}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Authorization
    @FineGrainedAuthorization(permission = Permission.WRITE, target = "{oid}")
    public ResultView removeUserByOrg(@PathParam("oid") long orgId, @PathParam("uid") long uid) throws Exception {
        return waterEquipmentService.removeUserByOrg(orgId, uid);
    }

    @Path("/objects")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Authorization
    @FineGrainedAuthorization(permission = Permission.READ, target = "{oid}")
    public List<ObjectView> getObjectsByOrg(@PathParam("oid") long orgId) throws Exception {
        return waterEquipmentService.getObjectsByOrg(orgId);
    }

    @Path("/objects")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Authorization
    @FineGrainedAuthorization(permission = Permission.WRITE, target = "{oid}")
    public ResultView addObjectByOrg(@PathParam("oid") long orgId, ObjectData objectData) throws Exception {
        return waterEquipmentService.addObjectByOrg(orgId, objectData);
    }

    @Path("/object/{objId}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Authorization
    @FineGrainedAuthorization(permission = Permission.WRITE, target = "{oid}")
    public ResultView removeObjectByOrg(@PathParam("oid") long orgId, @PathParam("objId") long objId) throws Exception {
        return waterEquipmentService.removeObjectByOrg(orgId, objId);
    }

}
