package net.wapwag.wemp.rest;

import com.thingswise.appframework.jaxrs.utils.OAuth2;
import net.wapwag.wemp.WaterEquipmentService;
import net.wapwag.wemp.dao.model.ObjectData;
import net.wapwag.wemp.dao.model.permission.Group;
import net.wapwag.wemp.rest.authz.FineGrainedAuthorization;
import net.wapwag.wemp.rest.authz.Permission;
import net.wapwag.wemp.rest.oauth2.WempTokenHandler;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.ws.rs.*;
import java.util.Set;

@Component(service = OrgGroupResource.class)
@Path("/organization/{oid}")
@OAuth2(tokenHandler = WempTokenHandler.NAME)
public class OrgGroupResource {

    @Reference
    WaterEquipmentService waterEquipmentService;

    @Path("/organizationGroups")
    @GET
    @FineGrainedAuthorization(permission = Permission.READ, target = "{oid}")
    public Set<Group> getGroupsByOrg() {
        throw new RuntimeException("TODO - not implemented");
    }

    @Path("/organizationGroups")
    @POST
    @FineGrainedAuthorization(permission = Permission.WRITE, target = "{oid}")
    public String addGroupByOrg(Group group) {
        throw new RuntimeException("TODO - not implemented");
    }

    @Path("/organizationGroup/{gid}")
    @GET
    @FineGrainedAuthorization(permission = Permission.READ, target = "{oid}")
    public Group getGroupByOrg() {
        throw new RuntimeException("TODO - not implemented");
    }

    @Path("/organizationGroup/{gid}")
    @PUT
    @FineGrainedAuthorization(permission = Permission.WRITE, target = "{oid}")
    public String updateGroupByOrg() {
        throw new RuntimeException("TODO - not implemented");
    }

    @Path("/organizationGroup/{gid}")
    @DELETE
    @FineGrainedAuthorization(permission = Permission.WRITE, target = "{oid}")
    public String removeGroupByOrg() {
        throw new RuntimeException("TODO - not implemented");
    }

    @Path("/organizationGroup/{gid}/users")
    @GET
    @FineGrainedAuthorization(permission = Permission.READ, target = "{oid}")
    public ObjectData saveObjectByUser() {
        throw new RuntimeException("TODO - not implemented");
    }

    @Path("/organizationGroup/{gid}/user/{uid}")
    @DELETE
    @FineGrainedAuthorization(permission = Permission.READ, target = "{oid}")
    public ObjectData removeObjectByUser() {
        throw new RuntimeException("TODO - not implemented");
    }

    @Path("/organizationGroup/{gid}/objects")
    @GET
    @FineGrainedAuthorization(permission = Permission.READ, target = "{oid}")
    public ObjectData getGroupsByObject() {
        throw new RuntimeException("TODO - not implemented");
    }

    @Path("/organizationGroup/{gid}/checkPermissions")
    @GET
    @FineGrainedAuthorization(permission = Permission.READ, target = "{oid}")
    public ObjectData getObjectByGroup() {
        throw new RuntimeException("TODO - not implemented");
    }

    @Path("/organizationUsers")
    @POST
    @FineGrainedAuthorization(permission = Permission.READ, target = "{oid}")
    public ObjectData saveObjectByGroup() {
        throw new RuntimeException("TODO - not implemented");
    }

    @Path("/organizationUser/{uid}")
    @DELETE
    @FineGrainedAuthorization(permission = Permission.READ, target = "{oid}")
    public ObjectData removeObjectByGroup() {
        throw new RuntimeException("TODO - not implemented");
    }

    @Path("/objects")
    @DELETE
    @FineGrainedAuthorization(permission = Permission.READ, target = "{oid}")
    public ObjectData removeObjectByGroup12() {
        throw new RuntimeException("TODO - not implemented");
    }

    @Path("/object/{obid}")
    @DELETE
    @FineGrainedAuthorization(permission = Permission.READ, target = "{oid}")
    public ObjectData removeObjectByGroup2() {
        throw new RuntimeException("TODO - not implemented");
    }

}
