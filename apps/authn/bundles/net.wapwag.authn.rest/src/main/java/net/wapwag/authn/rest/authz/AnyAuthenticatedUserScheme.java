package net.wapwag.authn.rest.authz;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.osgi.service.component.annotations.Component;

import com.thingswise.appframework.jaxrs.utils.AuthorizationException;
import com.thingswise.appframework.jaxrs.utils.AuthorizationScheme;

import net.wapwag.authn.rest.ErrorResponse;

@Component
@AnyAuthenticatedUser
public class AnyAuthenticatedUserScheme implements AuthorizationScheme {

	@Override
	public void authorize(ContainerRequestContext requestContext) throws AuthorizationException {
		if (requestContext.getSecurityContext() == null || 
			requestContext.getSecurityContext().getAuthenticationScheme() == null ||
		    !requestContext.getSecurityContext().isSecure()) {
			throw new AuthorizationException(
				Response.status(Response.Status.FORBIDDEN).
					type(MediaType.APPLICATION_JSON).
					entity(ErrorResponse.newErrorResponse().
						setErrorMessage("Not authorized").build()).build());
		}
	}


}
