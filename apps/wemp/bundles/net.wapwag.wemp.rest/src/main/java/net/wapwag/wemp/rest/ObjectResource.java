package net.wapwag.wemp.rest;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import net.wapwag.wemp.WaterEquipmentServiceException;
import net.wapwag.wemp.dao.model.Area;
import net.wapwag.wemp.dao.model.ObjectData;
import net.wapwag.wemp.dao.model.ObjectDict;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.thingswise.appframework.jaxrs.utils.OAuth2;

import net.wapwag.wemp.WaterEquipmentService;
import net.wapwag.wemp.rest.authz.FineGrainedAuthorization;
import net.wapwag.wemp.rest.authz.Permission;
import net.wapwag.wemp.rest.bindings.GetObjectPropertiesResponse;
import net.wapwag.wemp.rest.oauth2.WempTokenHandler;

import java.util.HashMap;
import java.util.Map;

@Component(service=ObjectResource.class)
@Path("/object")
//@OAuth2(tokenHandler=WempTokenHandler.NAME)
public class ObjectResource {

	@Reference
	private WaterEquipmentService waterEquipmentService;

	@GET
	@FineGrainedAuthorization(permission=Permission.READ, target="{oid}")
	public GetObjectPropertiesResponse getProperties() {
		// TODO implement get object properties
		throw new RuntimeException("TODO - not implemented");
	}

	@POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public ResponseMapper saveUser(String json) throws Exception {
        ObjectData ObjectData = new Gson().fromJson(json, ObjectData.class);
        return ResponseMapper.serialize().add("count", waterEquipmentService.saveObject(ObjectData));
    }

	@POST
    @Path("/dict")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseMapper saveObjectDict(String json) throws Exception {
        ObjectDict objectDict = new Gson().fromJson(json, ObjectDict.class);
		return ResponseMapper.serialize().add("count", waterEquipmentService.saveObjectDict(objectDict));
	}

	@DELETE
    @Path("/dict")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseMapper removeObjectDict(String json) throws Exception {
        ObjectDict objectDict = new Gson().fromJson(json, ObjectDict.class);
		return ResponseMapper.serialize().add("count", waterEquipmentService.removeObjectDict(objectDict));
	}

	@GET
    @Path("/area")
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseMapper getArea(@QueryParam("json") String json) {
        Area area = new Gson().fromJson(json, Area.class);
        try {
            return ResponseMapper.serialize().add("area", waterEquipmentService.getArea(area));
        } catch (WaterEquipmentServiceException e) {
            e.printStackTrace();
        }
        return ResponseMapper.serialize().add("error", "can not get area");
    }

}
