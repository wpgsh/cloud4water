package net.wapwag.wemp.rest;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import net.wapwag.wemp.WaterEquipmentServiceException;
import net.wapwag.wemp.dao.model.*;
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

	@Path("/country")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseMapper saveCountry(String json) throws Exception {
        Country country = new Gson().fromJson(json, Country.class);
        return ResponseMapper.serialize().add("count", waterEquipmentService.saveCountry(country));
    }

	@GET
    @Path("/country")
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseMapper getCountry(@QueryParam("json") String json) {
        Country country = new Gson().fromJson(json, Country.class);
        try {
            return ResponseMapper.serialize().add("country", waterEquipmentService.getCountry(country));
        } catch (WaterEquipmentServiceException e) {
            e.printStackTrace();
        }
        return ResponseMapper.serialize().add("error", "can not get country");
    }

	@GET
	@Path("/area")
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseMapper getArea(@QueryParam("json") String json) {
		Area area = new Gson().fromJson(json, Area.class);
		try {
			return ResponseMapper.serialize().add("result", waterEquipmentService.getArea(area));
		} catch (WaterEquipmentServiceException e) {
			e.printStackTrace();
		}
		return ResponseMapper.serialize().add("error", "can not get area");
	}


    @POST
    @Path("/project")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseMapper saveProject(String json) throws Exception {
        Project project = new Gson().fromJson(json, Project.class);
        return ResponseMapper.serialize().add("count", waterEquipmentService.saveProject(project));
    }

    @DELETE
    @Path("/project")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseMapper removeProject(String json) throws Exception {
        Project project = new Gson().fromJson(json, Project.class);
        return ResponseMapper.serialize().add("count", waterEquipmentService.removeProject(project));
    }

    @PUT
    @Path("/project")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseMapper updateProject(String json) throws Exception {
        Project project = new Gson().fromJson(json, Project.class);
        return ResponseMapper.serialize().add("count", waterEquipmentService.updateProject(project));
    }

    @GET
    @Path("/project")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseMapper getProject(@QueryParam("json") String json) {
        Project project = new Gson().fromJson(json, Project.class);
        try {
            return ResponseMapper.serialize().add("result", waterEquipmentService.getProject(project));
        } catch (WaterEquipmentServiceException e) {
            e.printStackTrace();
        }
        return ResponseMapper.serialize().add("error", "can not get area");
    }

    @POST
    @Path("/pumproom")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseMapper savePumpRoom(String json) throws Exception {
        PumpRoom pumpRoom = new Gson().fromJson(json, PumpRoom.class);
        return ResponseMapper.serialize().add("count", waterEquipmentService.savePumpRoom(pumpRoom));
    }

    @DELETE
    @Path("/pumproom")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseMapper removePumpRoom(String json) throws Exception {
        PumpRoom pumpRoom = new Gson().fromJson(json, PumpRoom.class);
        return ResponseMapper.serialize().add("count", waterEquipmentService.removePumpRoom(pumpRoom));
    }

    @PUT
    @Path("/pumproom")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseMapper updatePumpRoom(String json) throws Exception {
        PumpRoom pumpRoom = new Gson().fromJson(json, PumpRoom.class);
        return ResponseMapper.serialize().add("count", waterEquipmentService.updatePumpRoom(pumpRoom));
    }

    @GET
    @Path("/pumproom")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseMapper getPumpRoom(@QueryParam("json") String json) {
        PumpRoom pumpRoom = new Gson().fromJson(json, PumpRoom.class);
        try {
            return ResponseMapper.serialize().add("result", waterEquipmentService.getPumpRoom(pumpRoom));
        } catch (WaterEquipmentServiceException e) {
            e.printStackTrace();
        }
        return ResponseMapper.serialize().add("error", "can not get area");
    }

    @POST
    @Path("/pumpequipment")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseMapper savePumpEquipment(String json) throws Exception {
        PumpEquipment pumpEquipment = new Gson().fromJson(json, PumpEquipment.class);
        return ResponseMapper.serialize().add("count", waterEquipmentService.savePumpEquipment(pumpEquipment));
    }

    @DELETE
    @Path("/pumpequipment")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseMapper removePumpEquipment(String json) throws Exception {
        PumpEquipment pumpEquipment = new Gson().fromJson(json, PumpEquipment.class);
        return ResponseMapper.serialize().add("count", waterEquipmentService.removePumpEquipment(pumpEquipment));
    }

    @PUT
    @Path("/pumpequipment")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseMapper updatePumpEquipment(String json) throws Exception {
        PumpEquipment pumpEquipment = new Gson().fromJson(json, PumpEquipment.class);
        return ResponseMapper.serialize().add("count", waterEquipmentService.updatePumpEquipment(pumpEquipment));
    }

    @GET
    @Path("/pumpequipment")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseMapper getPumpEquipment(@QueryParam("json") String json) {
        PumpEquipment pumpEquipment = new Gson().fromJson(json, PumpEquipment.class);
        try {
            return ResponseMapper.serialize().add("result", waterEquipmentService.getPumpEquipment(pumpEquipment));
        } catch (WaterEquipmentServiceException e) {
            e.printStackTrace();
        }
        return ResponseMapper.serialize().add("error", "can not get area");
    }

}
