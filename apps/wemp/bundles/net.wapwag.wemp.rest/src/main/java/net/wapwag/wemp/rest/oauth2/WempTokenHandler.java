package net.wapwag.wemp.rest.oauth2;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.thingswise.appframework.jaxrs.utils.TokenHandler;
import com.thingswise.appframework.jaxrs.utils.TokenHandler.AccessToken;

import net.wapwag.wemp.WaterEquipmentService;

@Component
public class WempTokenHandler implements TokenHandler {

	public static final String NAME = "wemp";
	
	@Reference
	private WaterEquipmentService service;

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public AccessToken lookupToken(String handle) throws Exception {
		net.wapwag.wemp.model.AccessToken accessToken = service.lookupToken(handle);
		if (accessToken != null) {
			return new AccessToken(
					accessToken.userId,
					Long.MAX_VALUE, //access_token will never expire.
					accessToken.clientId,
					accessToken.handle,
					accessToken.scope);
		} else {
            return null;
		}
	}

}
