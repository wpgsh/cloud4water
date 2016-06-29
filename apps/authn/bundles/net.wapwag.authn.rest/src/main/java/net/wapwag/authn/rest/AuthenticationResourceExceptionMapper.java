package net.wapwag.authn.rest;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.osgi.service.component.annotations.Component;

@Provider
@Component(service=AuthenticationResourceExceptionMapper.class)
public class AuthenticationResourceExceptionMapper implements ExceptionMapper<AuthenticationResourceException> {

	@Override
	public Response toResponse(AuthenticationResourceException exception) {
		if (exception instanceof InvalidRequestException) {
			return Response.status(Response.Status.BAD_REQUEST).
					type(MediaType.APPLICATION_JSON).
					entity(ErrorResponse.newErrorResponse().
							setErrorMessage(exception.getMessage()).build()).build();	
		} else
		if (exception instanceof ResourceNotFoundException) {
			return Response.status(Response.Status.NOT_FOUND).
					type(MediaType.APPLICATION_JSON).
					entity(ErrorResponse.newErrorResponse().
							setErrorMessage(exception.getMessage()).build()).build();

		} else {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).
					type(MediaType.APPLICATION_JSON).
					entity(ErrorResponse.newErrorResponse().
							setErrorMessage(exception.getMessage()).build()).build();			
		}
	}

}
