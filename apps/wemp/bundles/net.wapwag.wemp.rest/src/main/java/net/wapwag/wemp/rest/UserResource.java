package net.wapwag.wemp.rest;

import com.thingswise.appframework.jaxrs.utils.OAuth2;
import net.wapwag.wemp.WaterEquipmentService;
import net.wapwag.wemp.dao.model.ObjectData;
import net.wapwag.wemp.rest.authz.FineGrainedAuthorization;
import net.wapwag.wemp.rest.authz.Permission;
import net.wapwag.wemp.rest.oauth2.WempTokenHandler;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.util.Set;

@Component(service = UserResource.class)
@Path("/user/{uid}")
@OAuth2(tokenHandler = WempTokenHandler.NAME)
public class UserResource {

    @Reference
    WaterEquipmentService waterEquipmentService;

    @Path("/checkPermissions")
    @POST
    @FineGrainedAuthorization(permission = Permission.READ, target = "{oid}")
    public String checkPermission() {
        throw new RuntimeException("TODO - not implemented");
    }

    @Path("/objects")
    @GET
    @FineGrainedAuthorization(permission = Permission.READ, target = "{oid}")
    public Set<ObjectData> getObjectsByUser() {
        throw new RuntimeException("TODO - not implemented");
    }

}
