package net.wapwag.wemp.rest;

import com.thingswise.appframework.jaxrs.utils.OAuth2;
import net.wapwag.wemp.WaterEquipmentService;
import net.wapwag.wemp.WaterEquipmentServiceException;
import net.wapwag.wemp.dao.model.ObjectData;
import net.wapwag.wemp.dao.model.permission.User;
import net.wapwag.wemp.model.ObjectView;
import net.wapwag.wemp.model.UserView;
import net.wapwag.wemp.rest.authz.FineGrainedAuthorization;
import net.wapwag.wemp.rest.authz.Permission;
import net.wapwag.wemp.rest.bindings.GetObjectPropertiesResponse;
import net.wapwag.wemp.rest.oauth2.WempTokenHandler;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.ws.rs.*;
import java.util.List;
import java.util.Set;

@Component(service = ObjectResource.class)
@Path("/object/{oid}")
//@OAuth2(tokenHandler = WempTokenHandler.NAME)
public class ObjectResource {

    @Reference
    WaterEquipmentService waterEquipmentService;

    @GET
    @FineGrainedAuthorization(permission = Permission.READ, target = "{oid}")
    public ObjectView getObject(@PathParam("oid") long objId) throws Exception {
        return new ObjectView(waterEquipmentService.getObject(objId));
    }

    @Path("/users")
    @GET
    @FineGrainedAuthorization(permission = Permission.READ, target = "{oid}")
    public List<UserView> getUsersByObject(@PathParam("oid") long objId, @QueryParam("actionId") String actionId) throws Exception {
        return waterEquipmentService.getUsersByObject(objId, actionId);
    }

    @Path("/user/{uid}")
    @GET
    @FineGrainedAuthorization(permission = Permission.READ, target = "{oid}")
    public ObjectData getObjectByUser() {
        throw new RuntimeException("TODO - not implemented");
    }

    @Path("/user/{uid}")
    @POST
    @FineGrainedAuthorization(permission = Permission.READ, target = "{oid}")
    public ObjectData saveObjectByUser() {
        throw new RuntimeException("TODO - not implemented");
    }

    @Path("/user/{uid}")
    @DELETE
    @FineGrainedAuthorization(permission = Permission.READ, target = "{oid}")
    public ObjectData removeObjectByUser() {
        throw new RuntimeException("TODO - not implemented");
    }

    @Path("/groups")
    @GET
    @FineGrainedAuthorization(permission = Permission.READ, target = "{oid}")
    public ObjectData getGroupsByObject() {
        throw new RuntimeException("TODO - not implemented");
    }

    @Path("/group/{gid}")
    @GET
    @FineGrainedAuthorization(permission = Permission.READ, target = "{oid}")
    public ObjectData getObjectByGroup() {
        throw new RuntimeException("TODO - not implemented");
    }

    @Path("/group/{gid}")
    @POST
    @FineGrainedAuthorization(permission = Permission.READ, target = "{oid}")
    public ObjectData saveObjectByGroup() {
        throw new RuntimeException("TODO - not implemented");
    }

    @Path("/group/{gid}")
    @DELETE
    @FineGrainedAuthorization(permission = Permission.READ, target = "{oid}")
    public ObjectData removeObjectByGroup() {
        throw new RuntimeException("TODO - not implemented");
    }

}
