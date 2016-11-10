package net.wapwag.authn.rest.oauth2;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.thingswise.appframework.jaxrs.utils.TokenHandler;

import net.wapwag.authn.AuthenticationService;
import net.wapwag.authn.Ids;
import net.wapwag.authn.model.AccessTokenMapper;

@Component
public class UsersTokenHandler implements TokenHandler {
	
	public static final String NAME = "users";
	
	@Reference
	private AuthenticationService service;

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public AccessToken lookupToken(String handle) throws Exception {
		net.wapwag.authn.model.AccessTokenMapper accessToken = service.lookupToken(handle);
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
