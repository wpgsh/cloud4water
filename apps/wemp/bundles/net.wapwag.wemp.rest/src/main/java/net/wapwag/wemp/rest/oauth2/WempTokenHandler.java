package net.wapwag.wemp.rest.oauth2;

import com.thingswise.appframework.jaxrs.utils.TokenHandler;
import net.wapwag.wemp.WaterEquipmentService;
import net.wapwag.wemp.model.AccessTokenMapper;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

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
		AccessTokenMapper accessToken = service.lookupToken(handle);
		if (accessToken != null) {
			return new AccessToken(
					accessToken.userId,
                    accessToken.expiration,
					accessToken.clientId,
					accessToken.handle,
					accessToken.scope
            );
		} else {
            return null;
		}
	}

}
