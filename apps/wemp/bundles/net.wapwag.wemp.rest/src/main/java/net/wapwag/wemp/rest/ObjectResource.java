package net.wapwag.wemp.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.thingswise.appframework.jaxrs.utils.OAuth2;

import net.wapwag.wemp.WaterEquipmentService;
import net.wapwag.wemp.rest.authz.FineGrainedAuthorization;
import net.wapwag.wemp.rest.authz.Permission;
import net.wapwag.wemp.rest.bindings.GetObjectPropertiesResponse;
import net.wapwag.wemp.rest.oauth2.WempTokenHandler;

@Component(service=ObjectResource.class)
@Path("/object/{oid}")
@OAuth2(tokenHandler=WempTokenHandler.NAME)
public class ObjectResource {

	@Reference
	WaterEquipmentService waterEquipmentService;
	
	@GET
	@FineGrainedAuthorization(permission=Permission.READ, target="{oid}")
	public GetObjectPropertiesResponse getProperties() {
		// TODO implement get object properties
		throw new RuntimeException("TODO - not implemented");
	}
	
}
