package net.wapwag.wemp.rest.authz;

import java.lang.annotation.Annotation;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriBuilder;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.thingswise.appframework.jaxrs.utils.AuthorizationException;
import com.thingswise.appframework.jaxrs.utils.AuthorizationScheme;

import net.wapwag.wemp.WaterEquipmentService;
import net.wapwag.wemp.WaterEquipmentServiceException;
import net.wapwag.wemp.rest.bindings.ErrorResponse;

@Component
@FineGrainedAuthorization(permission="", target="")
public class FineGrainedAuthorizationScheme implements AuthorizationScheme {
	
	private static Logger logger = LoggerFactory.getLogger(FineGrainedAuthorizationScheme.class);
	
	private LoadingCache<String, UriBuilder> uriBuilders = 
		CacheBuilder.newBuilder().build(new CacheLoader<String, UriBuilder>() {
			@Override
			public UriBuilder load(String key) throws Exception {
				return UriBuilder.fromUri(key);
			}			
		});
	
	@Reference
	WaterEquipmentService waterEquipmentService;

	@Override
	public void authorize(
			Annotation annotation, 
			ContainerRequestContext requestContext) throws AuthorizationException {
		
		// Check that the user has been authenticated
		SecurityContext securityContext = requestContext.getSecurityContext();
		
		if (securityContext == null || 
			securityContext.getAuthenticationScheme() == null ||
		    !securityContext.isSecure()) {
			throw new AuthorizationException(
				Response.status(Response.Status.FORBIDDEN).
					type(MediaType.APPLICATION_JSON).
					entity(ErrorResponse.newErrorResponse().
						setErrorMessage("Not authorized").build()).build());
		}				
		
		Principal userPrincipal = securityContext.getUserPrincipal(); 
		
		// Evaluate target
		FineGrainedAuthorization config = (FineGrainedAuthorization) annotation;
		
		MultivaluedMap<String, String> pathParameters =
			requestContext.getUriInfo().getPathParameters();
		
		Map<String, String> simplePathParameters = new HashMap<String, String>();
		for (String key : pathParameters.keySet()) {
			simplePathParameters.put(key, pathParameters.getFirst(key));
		}
		
		String target;
		try {
			target = uriBuilders.get(config.target()).buildFromMap(simplePathParameters).toString();
		} catch (ExecutionException e) {
			logger.error("Cannot process URI template: {}", config.target(), e);
			throw new AuthorizationException(
					Response.status(Response.Status.INTERNAL_SERVER_ERROR).
						type(MediaType.APPLICATION_JSON).
						entity(ErrorResponse.newErrorResponse().
							setErrorMessage("Internal error in authorization engine (1)").build()).build());
		}
		
		// Check that user has required permission for the given target
		try {
			if (!waterEquipmentService.isAuthorized(userPrincipal.getName(), config.permission(), target)) {
				throw new AuthorizationException(
						Response.status(Response.Status.FORBIDDEN).
							type(MediaType.APPLICATION_JSON).
							entity(ErrorResponse.newErrorResponse().
								setErrorMessage("Not authorized").build()).build());
			}
		} catch (WaterEquipmentServiceException e) {
			logger.error("Cannot check authorization", e);
			throw new AuthorizationException(
					Response.status(Response.Status.INTERNAL_SERVER_ERROR).
						type(MediaType.APPLICATION_JSON).
						entity(ErrorResponse.newErrorResponse().
							setErrorMessage("Internal error in authorization engine (2)").build()).build());
		}
	}

}
